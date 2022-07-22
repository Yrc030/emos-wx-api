package com.yrc.emos.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.pojo.entity.CityEntity;
import com.yrc.emos.wx.mapper.CityMapper;
import com.yrc.emos.wx.service.CityService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 疫情城市列表 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, CityEntity> implements CityService {

    /**
     * 根据城市名称查询城市编码
     */
    @Override
    public String getCode(String city) {
        CityEntity cityEntity = baseMapper.selectOne(new QueryWrapper<CityEntity>()
                .select("code").eq("city", city));
        return cityEntity == null ? null : cityEntity.getCode();
    }
}
