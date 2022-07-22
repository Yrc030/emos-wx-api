package com.yrc.emos.wx.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrc.emos.wx.pojo.entity.WorkdayEntity;

import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface WorkdayService extends IService<WorkdayEntity> {

    Set<String> getWorkdaysInRange(DateTime start, DateTime end);

}
