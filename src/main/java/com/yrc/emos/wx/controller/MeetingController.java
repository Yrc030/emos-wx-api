package com.yrc.emos.wx.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.constants.MeetingStatusEnum;
import com.yrc.emos.wx.constants.MeetingTypeEnum;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import com.yrc.emos.wx.pojo.form.*;
import com.yrc.emos.wx.pojo.result.GetMeetingsByUserIdAndMonthResult;
import com.yrc.emos.wx.pojo.result.MeetingDetailResult;
import com.yrc.emos.wx.pojo.result.MeetingListGroupResult;
import com.yrc.emos.wx.pojo.entity.MeetingEntity;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.service.MeetingService;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-06-06-16:11
 * Time: 16:11
 */

@Api("会议模块web接口")
@RequestMapping("/meeting")
@RestController
@Slf4j
public class MeetingController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MeetingService meetingService;


    @ApiOperation("会议分组分页查询")
    @PostMapping("/groupPageByDate")
    public R groupPageByDate(@Validated @RequestBody MeetingPageForm pageForm, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        List<MeetingListGroupResult> result = meetingService.groupPageByDate(pageForm, userId);
        return R.ok().put("result", result);
    }


    @ApiOperation("会议记录分页查询")
    @PostMapping("/meetingPage")
    public R meetingPage(@Validated @RequestBody MeetingPageForm pageForm, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        MeetingListGroupResult result = meetingService.meetingPage(pageForm, userId);
        return R.ok().put("result", result);
    }

    @ApiOperation("保存会议纪录")
    @PostMapping("/insertMeeting")
    @RequiresPermissions(value = {"ROOT", "MEETING:INSERT"}, logical = Logical.OR)
    public R insertMeeting(@Validated(InsertGroup.class) @RequestBody MeetingForm form, @RequestHeader("token") String token) {
        // 校验参数
        if (MeetingTypeEnum.OFFLINE.getCode().equals(form.getType()) &&
                StrUtil.isBlank(form.getPlace())) {
            // 会议类型为线下会议，但是未指定会议地点
            throw new EmosException("线下会议必须指定会议地点");
        }
        if (form.getStart().compareTo(form.getEnd()) >= 0) {
            throw new EmosException("开始时间必须在结束时间之前");
        }
        // 获取当前登录的用户id
        Integer userId = jwtUtil.getUserId(token);
        // 实体类赋值
        MeetingEntity entity = new MeetingEntity();
        BeanUtil.copyProperties(form, entity, "members");
        entity.setUuid(UUID.randomUUID().toString(true));
        entity.setMembers(Arrays.toString(form.getMembers()));
        entity.setCreatorId(userId.longValue());
        entity.setStatus(MeetingStatusEnum.PENDING.getCode());
        // 添加会议纪录
        meetingService.insertMeeting(entity);
        return R.ok("保存会议纪录成功");
    }

    @ApiOperation("更新会议")
    @PostMapping("updateMeeting")
    @RequiresPermissions(value = {"ROOT", "MEETING:UPDATE"}, logical = Logical.OR)
    public R updateMeeting(@Validated(UpdateGroup.class) @RequestBody MeetingForm form, @RequestHeader("token") String token) {
        // 校验参数
        if (MeetingTypeEnum.OFFLINE.getCode().equals(form.getType()) &&
                StrUtil.isBlank(form.getPlace())) {
            // 会议类型为线下会议，但是未指定会议地点
            throw new EmosException("线下会议必须指定会议地点");
        }

        if (form.getStart().compareTo(form.getEnd()) >= 0) {
            throw new EmosException("开始时间必须在结束时间之前");
        }

        // 构建实体类
        MeetingEntity updateMeeting = new MeetingEntity();
        BeanUtil.copyProperties(form, updateMeeting, "members");
        updateMeeting.setMembers(Arrays.toString(form.getMembers()));
        updateMeeting.setStatus(MeetingStatusEnum.PENDING.getCode());
        // 查询会议创建者id
        MeetingEntity oldMeeting = meetingService.getById(updateMeeting.getId());
        if(oldMeeting == null) {
            throw new EmosException("不存在指定的会议");
        }
        if(oldMeeting.getCreatorId() == null) {
            throw new EmosException("会议没有创建者");
        }
        updateMeeting.setCreatorId(oldMeeting.getCreatorId());
        // 更新会议
        meetingService.updateMeeting(updateMeeting);
        return R.ok("更新会议记录成功");
    }

    @ApiOperation("查询会议详情")
    @GetMapping("/getMeetingDetailById")
    @RequiresPermissions(value = {"ROOT", "MEETING:SELECT"}, logical = Logical.OR)
    public R getMeetingDetailById(@RequestParam("id") Long id) {
        MeetingDetailResult result = meetingService.getMeetingDetailById(id);
        return R.ok().put("result", result);
    }

    @ApiOperation("删除会议纪录")
    @PostMapping("/deleteMeetingById")
    @RequiresPermissions(value = {"ROOT", "MEETING:DELETE"}, logical = Logical.OR)
    public R deleteMeetingById(@Validated @RequestBody DeleteMeetingByIdForm form) {
        meetingService.deleteMeetingById(form.getId());
        return R.ok("删除会议成功");
    }


    @PostMapping("/receiveNotify")
    @ApiOperation("接受工作流通知")
    public R receiveNotify(@Validated @RequestBody ReceiveNotifyForm form) {
        if (form.getResult().equals("同意")) {
            log.debug(form.getUuid() + "的会议审批通过");
        } else {
            log.debug(form.getUuid() + "的会议审批不通过");
        }
        return R.ok();
    }

    @GetMapping("/getRoomIdByUUID")
    @ApiOperation("获取会议房间号")
    public R getRoomIdByUUID(@RequestParam("uuid") String uuid) {
        if(StrUtil.isBlank(uuid)) {
            throw new EmosException("uuid不能为空");
        }
       Long roomId = meetingService.getRoomIdByUUID(uuid);
        return R.ok().put("result",roomId);
    }


    @PostMapping("/meetingPageByUserIdAndDate")
    @ApiOperation("分页获取用户指定日期（年月）内的会议列表")
    public R meetingPageByUserIdAndDate(@Validated @RequestBody MeetingPageByUserIdAndDateForm form, @RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        List<GetMeetingsByUserIdAndMonthResult> result = meetingService.getMeetingsByUserIdAndMonth(form, userId);
        return R.ok().put("result",result);
    }


    @PostMapping("getUserMeetingDatesInMonth")
    @ApiOperation("获取用户载指定月份内有会议的日期")
    public R getUserMeetingDatesInMonth(@Validated @RequestBody GetUserMeetingDatesInMonthForm form, @RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        String date = form.getYear() + "-" + form.getMonth();
        List<String> result = meetingService.getUserMeetingDatesInMonth(userId, date);
        return  R.ok().put("result", result);
    }


}
