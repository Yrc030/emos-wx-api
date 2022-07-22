package com.yrc.emos.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrc.emos.wx.pojo.form.UserSummaryForm;
import com.yrc.emos.wx.pojo.result.GetContactsListResult;
import com.yrc.emos.wx.pojo.result.MemberResult;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    Set<String> selectUserPermissions(@Param("userId") Integer userId);

    /**
     * 如果需要 Mybatis-Plus 对实体类字段进行自动填充，则必须注解 @Param("et") UserEntity user 或者 不注解 @Param
     */
    void insertUser(UserEntity user);

    Integer selectIdByOpenId(@Param("openId") String openId);

    HashMap<String, String> selectUsernameAndDeptName(@Param("userId") Integer userId);

    String selectUserHiredate(@Param("userId") Integer userId);

    UserSummaryForm selectUserSummary(@Param("userId") Integer userId);

    List<UserEntity> selectAllWithDeptName();

    List<MemberResult> selectMembersByKeyword(@Param("keyword") String keyword);

    /**
     * 查询用户信息（保存部门名和角色名）
     */
    UserEntity selectUserInfo(@Param("userId") Integer userId);

    /**
     * 查询用户用户所在部门经理ID
     */
    Integer selectManagerIdInUserDept(@Param("userId") Integer userId);

    /**
     * 查询公司总经理ID
     */
    Integer selectGMId();

    List<MemberResult> selectMembersByMeetingId(@Param("meetingId") Long meetingId);

    Integer selectManagerIdInDept(@Param("deptId") Integer deptId);


    List<GetContactsListResult.ContactResult> selectContactList();
}
