package com.yrc.emos.wx.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.pojo.entity.HolidaysEntity;
import com.yrc.emos.wx.mapper.HolidaysMapper;
import com.yrc.emos.wx.service.HolidaysService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 节假日表 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class HolidaysServiceImpl extends ServiceImpl<HolidaysMapper, HolidaysEntity> implements HolidaysService {
    @Override
    public Set<String> getHolidaysInRange(DateTime start, DateTime end) {
        return baseMapper.selectHolidaysInRange(start.toDateStr(), end.toDateStr());
    }
}
