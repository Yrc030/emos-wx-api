package com.yrc.emos.wx.service;

import cn.hutool.core.date.DateTime;
import com.yrc.emos.wx.pojo.entity.HolidaysEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 节假日表 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface HolidaysService extends IService<HolidaysEntity> {

    Set<String> getHolidaysInRange(DateTime start, DateTime end);

}
