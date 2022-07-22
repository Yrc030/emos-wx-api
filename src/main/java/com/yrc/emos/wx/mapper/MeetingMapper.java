package com.yrc.emos.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrc.emos.wx.pojo.entity.MeetingEntity;
import com.yrc.emos.wx.pojo.result.GetMeetingsByUserIdAndMonthResult;
import com.yrc.emos.wx.pojo.result.MeetingDetailResult;
import com.yrc.emos.wx.pojo.result.MeetingListGroupResult;
import com.yrc.emos.wx.pojo.result.MeetingListItemResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会议表 Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface MeetingMapper extends BaseMapper<MeetingEntity> {

    List<MeetingListGroupResult> selectGroupPageByDate(@Param("skip") int skip, @Param("limit") int limit, @Param("userId") int userId);

    List<MeetingListItemResult> selectMeetingPage(@Param("skip") int skip, @Param("limit") int limit, @Param("date") String date, @Param("userId") int userId);

    /**
     * 查询指定uuid会议中的参会人员是否同属于一个部门
     */
    Boolean isSameDept(@Param("uuid") String uuid);


    MeetingDetailResult selectMeetingDetailById(@Param("id") Long id);

    List<GetMeetingsByUserIdAndMonthResult> selectMeetingsByUserIdAndMonth(@Param("skip") Integer skip, @Param("limit") Integer limit, @Param("date") String date, @Param("userId") Integer userId);

    List<String> selectUserMeetingDatesInMonth(@Param("userId") Integer userId, @Param("date") String date);
}
