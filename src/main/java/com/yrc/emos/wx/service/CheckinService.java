package com.yrc.emos.wx.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrc.emos.wx.pojo.form.CheckinDateForm;
import com.yrc.emos.wx.pojo.form.MonthCheckinForm;
import com.yrc.emos.wx.pojo.form.UserCheckinForm;
import com.yrc.emos.wx.pojo.entity.CheckinEntity;
import com.yrc.emos.wx.pojo.bo.CheckinBo;

import java.util.List;

/**
 * <p>
 * 签到表 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface CheckinService extends IService<CheckinEntity> {

    String canCheckin(Integer userId);

    void checkin(CheckinBo checkinBo);

    UserCheckinForm getTodayCheckinInfo(Integer userId);

    List<CheckinDateForm> getWeekCheckin(Integer userId, DateTime start, DateTime end);

    long countCheckinDays(Integer userId);

    MonthCheckinForm getMonthCheckin(Integer userId, DateTime start, DateTime end);
}
