package com.yrc.emos.wx.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.constants.MeetingTypeEnum;
import com.yrc.emos.wx.constants.RoleEnum;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.mapper.MeetingMapper;
import com.yrc.emos.wx.mapper.UserMapper;
import com.yrc.emos.wx.pojo.entity.MeetingEntity;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.pojo.form.MeetingPageByUserIdAndDateForm;
import com.yrc.emos.wx.pojo.form.MeetingPageForm;
import com.yrc.emos.wx.pojo.result.GetMeetingsByUserIdAndMonthResult;
import com.yrc.emos.wx.pojo.result.MeetingDetailResult;
import com.yrc.emos.wx.pojo.result.MeetingListGroupResult;
import com.yrc.emos.wx.pojo.result.MeetingListItemResult;
import com.yrc.emos.wx.pojo.to.DeleteMeetingWorkflowTo;
import com.yrc.emos.wx.pojo.to.StartMeetingWorkflowTo;
import com.yrc.emos.wx.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会议表 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, MeetingEntity> implements MeetingService {

    @Autowired
    private MeetingMapper meetingDao;


    @Autowired
    private UserMapper userDao;


    @Autowired
    private RedisTemplate<Object, Object> template;

    @Value("${emos.code}")
    private String code;

    @Value("${emos.receiveNotify}")
    private String receiveNotifyUrl;

    @Value("${workflow.url.base}")
    private String workflowBaseUrl;


    @Value("${workflow.url.start-meeting-process}")
    private String startMeetingProcess;

    @Value("${workflow.url.delete-meeting-process}")
    private String deleteMeetingProcess;

    @Override
    @Transactional
    public void insertMeeting(MeetingEntity meetingEntity) {
        int count = meetingDao.insert(meetingEntity);
        if (count != 1) {
            throw new EmosException("添加会议记录失败");
        }
        // 开启审批工作流
        startMeetingWorkflow(meetingEntity);
    }

    /**
     * 根据会议日期分组分页查询
     */
    @Override
    public List<MeetingListGroupResult> groupPageByDate(MeetingPageForm pageForm, int userId) {
        int limit = pageForm.getLimit();
        int page = pageForm.getPage();
        int skip = (page - 1) * limit;
        List<MeetingListGroupResult> groups = meetingDao.selectGroupPageByDate(skip, limit, userId);
        ArrayList<MeetingListItemResult> empty = new ArrayList<>();
        groups.forEach(r -> {
            r.setPage(0);
            r.setMeetings(empty);
            r.setLast(false);
        });
        return groups;
    }

    /**
     * 分页查询会议记录
     */
    @Override
    public MeetingListGroupResult meetingPage(MeetingPageForm pageForm, int userId) {
        int limit = pageForm.getLimit();
        int page = pageForm.getPage();
        int skip = (page - 1) * limit;
        String date = pageForm.getDate();
        MeetingListGroupResult result = new MeetingListGroupResult();
        List<MeetingListItemResult> meetings = meetingDao.selectMeetingPage(skip, limit, date, userId);
        result.setDate(date);
        result.setMeetings(meetings);
        result.setPage(page);
        result.setLast(meetings == null || meetings.size() < limit);
        return result;
    }

    @Override
    public MeetingDetailResult getMeetingDetailById(Long id) {
        MeetingDetailResult result = meetingDao.selectMeetingDetailById(id);
        result.setMembers(userDao.selectMembersByMeetingId(id));
        return result;
    }

    @Override
    @Transactional
    public void updateMeeting(MeetingEntity meeting) {
        // 更新会议表
        int count = meetingDao.update(meeting, new UpdateWrapper<MeetingEntity>().eq("id", meeting.getId()).eq("status", 3));
        if (count != 1) {
            throw new EmosException("更新会议失败");
        }
        // 删除旧的工作流
        deleteMeetingWorkflow(meeting, "更新会议纪录");
        // 创建新的工作流
        startMeetingWorkflow(meeting);
    }

    @Override
    @Transactional
    public void deleteMeetingById(Long id) {
        // 根据会议id查询会议信息
        MeetingEntity deleteMeeting = getById(id);

        LocalDateTime offsetDateTime = deleteMeeting.getDate().atTime(deleteMeeting.getStart()).minusMinutes(20);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(offsetDateTime) || now.isEqual(offsetDateTime)) {
            throw new EmosException("距离会议开始不足20分钟，不能删除会议");
        }
        // 删除会议
        boolean deleted = this.remove(new QueryWrapper<MeetingEntity>().eq("id", id).eq("status", 3));
        if (!deleted) {
            throw new EmosException("删除会议失败");
        }
        // 删除工作流
        this.deleteMeetingWorkflow(deleteMeeting, "删除会议纪录");
    }

    @Override
    public Long getRoomIdByUUID(String uuid) {
        Object roomId = template.opsForValue().get(uuid);
        try {
            return Long.parseLong(roomId.toString());
        } catch (NumberFormatException e) {
            throw new EmosException("会议房间号错误");
        }
    }

    @Override
    public List<GetMeetingsByUserIdAndMonthResult> getMeetingsByUserIdAndMonth(MeetingPageByUserIdAndDateForm form, Integer userId) {
        Integer page = form.getPage();
        Integer limit = form.getLimit();
        Integer skip = (page - 1) * limit;
        String date = null;
        if (form.getYear() != null && form.getMonth() != null) {
            date = form.getYear() + "-" + form.getMonth();
        }
        return meetingDao.selectMeetingsByUserIdAndMonth(skip, limit, date, userId);
    }

    @Override
    public List<String> getUserMeetingDatesInMonth(Integer userId, String date) {
        return meetingDao.selectUserMeetingDatesInMonth(userId, date);
    }

    private void deleteMeetingWorkflow(MeetingEntity meeting, String reason) {

        DeleteMeetingWorkflowTo to = new DeleteMeetingWorkflowTo();
        to.setCode(code);
        to.setUuid(meeting.getUuid());
        to.setReason(reason);
        to.setInstanceId(meeting.getInstanceId());
        String url = workflowBaseUrl + deleteMeetingProcess;
        HttpResponse response = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(to), "application/json")
                .execute();

        if (response.getStatus() != 200) {
            throw new EmosException("删除工作流实例失败");
        }
    }

    /**
     * 开启会议审批工作流
     */
    private void startMeetingWorkflow(MeetingEntity meeting) {
        String uuid = meeting.getUuid();
        Integer creatorId = meeting.getCreatorId().intValue();
        String date = meeting.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String start = meeting.getStart().format(DateTimeFormatter.ISO_LOCAL_TIME);

        // 查询创建者信息
        UserEntity user = userDao.selectUserInfo(creatorId);
        // 保存到 to 中
        StartMeetingWorkflowTo to = new StartMeetingWorkflowTo();
        to.setUrl(receiveNotifyUrl);
        to.setCode(code);
        to.setUuid(uuid);
        to.setDate(date);
        to.setStart(start);
        to.setSameDept(meetingDao.isSameDept(uuid));
        // to.setOpenId(user.getOpenId()); 重构的 workflow 不需要 openId 参数
        // 新添加了下面四个参数
        to.setCreatorId(user.getId());
        to.setCreatorName(user.getName());
        to.setTitle(meeting.getTitle());
        String meetingType = MeetingTypeEnum.ONLINE.getCode().equals(meeting.getType()) ? MeetingTypeEnum.ONLINE.getDesc() : MeetingTypeEnum.OFFLINE.getDesc();
        to.setMeetingType(meetingType);

        if (!ArrayUtil.contains(user.getRoles(), RoleEnum.GM.getDesc())) {
            // 如果创建者不是总经理
            to.setManagerId(userDao.selectManagerIdInUserDept(user.getId()));
            to.setGmId(userDao.selectGMId());
        }
        // 请求工作流接口，开启工作流
        String url = workflowBaseUrl + startMeetingProcess;
        HttpResponse response = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(to), "application/json")
                .execute();

        if (response.isOk()) {
            String instanceId = JSONUtil.parseObj(response.body()).get("instanceId").toString();
            // 请求成功，更新工作流状态
            meeting.setInstanceId(instanceId);
            int count = meetingDao.updateById(meeting);
            if (count < 1) {
                throw new EmosException("更新会议工作流实例失败");
            }
        } else {
            throw new EmosException("创建会议工作流实例失败");
        }
    }
}
