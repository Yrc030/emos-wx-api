package com.yrc.emos.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrc.emos.wx.pojo.entity.HolidaysEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 节假日表 Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface HolidaysMapper extends BaseMapper<HolidaysEntity> {
    /**
     * 判断当前是否为特殊的休息日（节假日调休后）
     */
    boolean selectTodayIsHoliday();

    /**
     * 查询指定日期范围内的特殊休息日
     */
    Set<String> selectHolidaysInRange(@Param("start") String start, @Param("end") String end);

}
