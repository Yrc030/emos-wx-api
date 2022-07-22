package com.yrc.emos.wx.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrc.emos.wx.mapper.dto.CheckinDateStatusDto;
import com.yrc.emos.wx.pojo.entity.CheckinEntity;
import com.yrc.emos.wx.pojo.form.UserCheckinForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 签到表 Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface CheckinMapper extends BaseMapper<CheckinEntity> {

    boolean selectHasCheckin(@Param("userId") Integer userId, @Param("start") String start, @Param("end") String end);

    /**
     * 查询用户基本信息与当天考勤信息
     */
    UserCheckinForm selectTodayCheckinInfo(@Param("userId") Integer userId);

    /**
     * 查询用户在 start-end 日期范围内的考勤情况
     */
    List<CheckinDateStatusDto> selectWeekCheckinStatus(@Param("userId") Integer userId, @Param("start") String start, @Param("end") String end);

    /**
     * 删除用户所有签到纪录
     */
    default Boolean deleteAllByUserId(Integer userId) {
       return this.delete(new QueryWrapper<CheckinEntity>().eq("user_id", userId)) > 0;
    }
}
