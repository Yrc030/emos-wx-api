package com.yrc.emos.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrc.emos.wx.pojo.entity.WorkdayEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface WorkdayMapper extends BaseMapper<WorkdayEntity> {

    /**
     * 判断当前是否为特殊的工作日（节假日调休后）
     */
    boolean selectTodayIsWorkday();

    /**
     * 查询指定日期范围内的特殊工作日
     */
    Set<String> selectWorkdaysInRange(@Param("start") String start, @Param("end") String end);
}
