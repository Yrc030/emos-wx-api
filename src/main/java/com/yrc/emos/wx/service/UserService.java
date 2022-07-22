package com.yrc.emos.wx.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrc.emos.wx.pojo.form.RegisterForm;
import com.yrc.emos.wx.pojo.form.UserSummaryForm;
import com.yrc.emos.wx.pojo.result.DeptWithMembersResult;
import com.yrc.emos.wx.pojo.result.GetContactsListResult;
import com.yrc.emos.wx.pojo.result.MemberResult;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.pojo.result.UsernameAndPhotoResult;
import com.yrc.emos.wx.util.R;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface UserService extends IService<UserEntity> {

    Integer register(RegisterForm registerForm);

    Set<String> getUserPermissions(Integer userId);

    Integer login(String code, String token);

    void saveCacheToken(String token, Integer userId, Integer cacheExpire);

    DateTime getUserHiredate(Integer userId);

    UserSummaryForm getUserSummary(Integer userId);

    List<DeptWithMembersResult> searchMembersByKeyword(String keyword);

    List<MemberResult> searchMembersByIds(List<Integer> ids);

    List<UsernameAndPhotoResult> getUserNameAndPhoto(List<Integer> ids);

    void saveUser(UserEntity userEntity);

    boolean hasGM();

    boolean hasManager(Integer deptId);

    UserEntity getUserById(Integer userId);

    boolean hasNotSpecifiedUserRoot(Integer id);

    boolean hasNotSpecifiedUserGM(Integer userId);

    boolean hasManagerAndNotSpecifiedUser(Integer userId, Integer deptId);

    void updateUser(UserEntity userEntity);

    void deleteUserById(Integer userId);

    GetContactsListResult getContactList();
}
