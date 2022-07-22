package com.yrc.emos.wx.service;

import com.yrc.emos.wx.pojo.entity.CityEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 疫情城市列表 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface CityService extends IService<CityEntity> {

    String getCode(String city);
}
