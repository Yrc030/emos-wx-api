package com.yrc.emos.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrc.emos.wx.pojo.form.MeetingPageByUserIdAndDateForm;
import com.yrc.emos.wx.pojo.form.MeetingPageForm;
import com.yrc.emos.wx.pojo.result.GetMeetingsByUserIdAndMonthResult;
import com.yrc.emos.wx.pojo.result.MeetingDetailResult;
import com.yrc.emos.wx.pojo.result.MeetingListGroupResult;
import com.yrc.emos.wx.pojo.entity.MeetingEntity;

import java.util.List;

/**
 * <p>
 * 会议表 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface MeetingService extends IService<MeetingEntity> {


    void insertMeeting(MeetingEntity meetingEntity);

    List<MeetingListGroupResult> groupPageByDate(MeetingPageForm pageForm, int userId);

    MeetingListGroupResult meetingPage(MeetingPageForm pageForm, int userId);

    MeetingDetailResult getMeetingDetailById(Long id);

    void updateMeeting(MeetingEntity meeting);

    void deleteMeetingById(Long id);

    Long getRoomIdByUUID(String uuid);


   List< GetMeetingsByUserIdAndMonthResult> getMeetingsByUserIdAndMonth(MeetingPageByUserIdAndDateForm form, Integer userId);

    List<String> getUserMeetingDatesInMonth(Integer userId, String date);
}
