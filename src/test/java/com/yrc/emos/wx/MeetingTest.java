package com.yrc.emos.wx;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yrc.emos.wx.pojo.form.MeetingPageForm;
import com.yrc.emos.wx.pojo.result.MeetingListGroupResult;
import com.yrc.emos.wx.pojo.entity.MeetingEntity;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.service.MeetingService;
import com.yrc.emos.wx.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Description:
 * User: joker
 * Date: 2022-06-06-15:24
 * Time: 15:24
 */
public class MeetingTest extends EmosWxApiApplicationTests {

    @Autowired
    private MeetingService meetingService;


    @Autowired
    private UserService userService;

    @Test
    public void testSaveBatch() {

        List<Integer> ids = userService.list(new QueryWrapper<UserEntity>().select("id").ne("id", 18)).stream().map(UserEntity::getId).collect(Collectors.toList());


        LocalDate date = LocalDate.now();
        LocalTime start = null;
        LocalTime end = null;
        List<MeetingEntity> list = new ArrayList<>();
        Random rdm = new Random();
        for (int i = 0; i < 500; i++) {
            if ((i % 20) == 0) {
                date = date.plus(1L, ChronoUnit.DAYS);
                start = LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME);
                end = start.plus(1, ChronoUnit.HOURS);
            }
            MeetingEntity entity = new MeetingEntity();
            entity.setUuid(IdUtil.simpleUUID());
            entity.setTitle(date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")) + "的视频会议[" + i + "]");
            entity.setCreatorId(18L);
            entity.setDate(date);
            entity.setStart(start);
            entity.setEnd(end);
            entity.setType((i & 3) == 0 ? 1 : 2);
            entity.setPlace(entity.getType() == 1 ? "网络会议室" : +i + "号会议室");

            StringBuilder members = new StringBuilder();
            members.append('[');
            for(int j = 0; j < 3; j++) {
                int idx = RandomUtil.randomInt(ids.size());
                members.append(ids.get(idx));
                if(j < 2) {
                    members.append(",");
                }
            }
            members.append(']');
            entity.setMembers(members.toString());


            entity.setDesc("会议研讨Emos线上测试");
            entity.setInstanceId(IdUtil.simpleUUID());
            entity.setStatus(rdm.nextInt(5) + 1);
            list.add(entity);
            start = start.plus(1, ChronoUnit.HOURS);
            end = start.plus(1, ChronoUnit.HOURS);
        }

        meetingService.saveBatch(list);
    }

    /**
     * 用于测试:
     */
    @Test
    public void testGroupPageByDate() {
        MeetingPageForm form = new MeetingPageForm();
        form.setPage(1);
        form.setLimit(20);
        List<MeetingListGroupResult> result = meetingService.groupPageByDate(form, 18);
        System.out.println("result = " + result);
    }

    /**
     * 用于测试:
     */
    @Test
    public void testMeetingPage() {
        MeetingPageForm form = new MeetingPageForm();
        form.setPage(1);
        form.setLimit(10);
        form.setDate("2022-06-07");
        MeetingListGroupResult result = meetingService.meetingPage(form, 18);
        System.out.println("result = " + result);
    }
}
