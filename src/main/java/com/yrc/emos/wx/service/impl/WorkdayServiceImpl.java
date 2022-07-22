package com.yrc.emos.wx.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.pojo.entity.WorkdayEntity;
import com.yrc.emos.wx.mapper.WorkdayMapper;
import com.yrc.emos.wx.service.WorkdayService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class WorkdayServiceImpl extends ServiceImpl<WorkdayMapper, WorkdayEntity> implements WorkdayService {

    @Override
    public Set<String> getWorkdaysInRange(DateTime start, DateTime end) {
        return baseMapper.selectWorkdaysInRange(start.toDateStr(), end.toDateStr());
    }
}
