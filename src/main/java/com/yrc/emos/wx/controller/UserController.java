package com.yrc.emos.wx.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yrc.emos.wx.config.shiro.JWTProperties;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.config.tencent.TLSSigAPIv2;
import com.yrc.emos.wx.constants.RoleEnum;
import com.yrc.emos.wx.constraints.group.DeleteGroup;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.pojo.form.*;
import com.yrc.emos.wx.pojo.result.DeptWithMembersResult;
import com.yrc.emos.wx.pojo.result.GetContactsListResult;
import com.yrc.emos.wx.pojo.result.MemberResult;
import com.yrc.emos.wx.pojo.result.UsernameAndPhotoResult;
import com.yrc.emos.wx.service.UserService;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Description:
 * User: joker
 * Date: 2022-05-19-10:17
 * Time: 10:17
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api("用户模块Web接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private JWTProperties jwtProperties;

    @Value("${trtc.appid}")
    private Integer appid;

    @Value("${trtc.key}")
    private String key;

    @Value("${trtc.expire}")
    private Integer expire;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public R login(@Validated @RequestBody LoginForm form, @RequestHeader("token") String token) {
        // 获取用户权限列表
        Integer id = null;
        if (StrUtil.isNotBlank(token)) {
            try {
                jwtUtil.verifyToken(token);
            } catch (TokenExpiredException e) {
                // token 过期
                id = userService.login(form.getCode(), token);
                token = jwtUtil.createToken(id);
                userService.saveCacheToken(token, id, jwtProperties.getCacheExpire());
            }
            id = jwtUtil.getUserId(token);
        } else {
            id = userService.login(form.getCode(), token);
            token = jwtUtil.createToken(id);
            userService.saveCacheToken(token, id, jwtProperties.getCacheExpire());
        }
        // 获取用户权限列表
        Set<String> permissions = userService.getUserPermissions(id);
        return R.ok("登录成功").put("token", token).put("permission", permissions);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public R register(@Validated @RequestBody RegisterForm registerForm) {

        // 1. 注册用户
        Integer userId = userService.register(registerForm);
        if (userId == null) {
            throw new RuntimeException("注册失败");
        }
        // 2. 创建 token，并保存至缓存
        String token = jwtUtil.createToken(userId);
        userService.saveCacheToken(token, userId, jwtProperties.getCacheExpire());
        // 3. 获取用户权限
        Set<String> permissions = userService.getUserPermissions(userId);
        // 4. 返回 token 和 用户权限
        return R.ok("注册成功").put("token", token).put("permission", permissions);
    }


    @GetMapping("/getUserSummary")
    @ApiOperation("获取用户简要信息")
    public R getUserSummary(@RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        UserSummaryForm userSummary = userService.getUserSummary(userId);
        return R.ok().put("result", userSummary);
    }

    @PostMapping("/searchMembersByKeyword")
    @ApiOperation("通过关键字检索用户")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:SELECT"}, logical = Logical.OR)
    public R searchMembersByKeyword(@Validated @RequestBody SearchMembersByKeywordForm keywordForm) {
        List<DeptWithMembersResult> result = userService.searchMembersByKeyword(keywordForm.getKeyword());
        return R.ok().put("result", result);
    }


    @PostMapping("/searchMembersByIds")
    @ApiOperation("通过ids批量查询用户信息")
    @RequiresPermissions(value = {"ROOT", "MEETING:INSERT", "MEETING:UPDATE"}, logical = Logical.OR)
    public R searchMembersByIds(@Validated @RequestBody IdsForm idsForm) {
        List<MemberResult> result = userService.searchMembersByIds(idsForm.getIds());
        return R.ok().put("result", result);
    }


    @PostMapping("/getUsernameAndPhoto")
    @ApiOperation("通过ids批量查询用户（终审人）的用户名和头像")
    @RequiresPermissions(value = {"WORKFLOW:APPROVAL"})
    public R getUsernameAndPhoto(@Validated @RequestBody IdsForm idsForm) {
        List<UsernameAndPhotoResult> result = userService.getUserNameAndPhoto(idsForm.getIds());
        return R.ok().put("result", result);
    }

    @GetMapping("genUserSig")
    @ApiOperation("生成用户签名")
    public R genUserSig(@RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        TLSSigAPIv2 api = new TLSSigAPIv2(appid, key);
        String userSig = api.genUserSig(userId + "", expire);
        return R.ok().put("userSig", userSig).put("id", userId);
    }

    @PostMapping("/saveUser")
    @ApiOperation("保存用户信息")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:INSERT"}, logical = Logical.OR)
    public R saveUser(@Validated(InsertGroup.class) @RequestBody UserEntity userEntity) {
        String role = userEntity.getRole();
        if (!JSONUtil.isTypeJSONArray(role)) {
            throw new EmosException("角色不是数组格式");
        }
        JSONArray roleArray = JSONUtil.parseArray(role);
        if (roleArray.contains(0)) {
            throw new EmosException("无法创建超级管理员");
        }
        userEntity.setRoot(false);
        if (roleArray.contains(1) && userService.hasGM()) {
            throw new EmosException("创建总经理失败，已经存在");
        }
        if (roleArray.contains(2) && userService.hasManager(userEntity.getDeptId())) {
            throw new EmosException("创建部门经理失败，指定部门已经存在部门经理");
        }
        userService.saveUser(userEntity);
        return R.ok("保存成功");
    }

    @PostMapping("/updateUser")
    @ApiOperation("保存用户信息")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:UPDATE"}, logical = Logical.OR)
    public R updateUser(@Validated({UpdateGroup.class}) @RequestBody UserEntity userEntity) {
        String role = userEntity.getRole();
        if (!JSONUtil.isTypeJSONArray(role)) {
            throw new EmosException("角色不是数组格式");
        }

        JSONArray roleArray = JSONUtil.parseArray(role);
        Integer userId = userEntity.getId();
        if (roleArray.contains(RoleEnum.ROOT.getCode())) {
            userEntity.setRoot(true);
            if (userService.hasNotSpecifiedUserRoot(userId)) {
                throw new EmosException("无法修改身份为超级管理员，已存在");
            }
        } else {
            userEntity.setRoot(false);
        }
        userEntity.setRoot(false);
        if (roleArray.contains(RoleEnum.GM.getCode()) && userService.hasNotSpecifiedUserGM(userId)) {
            throw new EmosException("创建总经理失败，已存在");
        }
        if (roleArray.contains(RoleEnum.MANAGER.getCode()) && userService.hasManagerAndNotSpecifiedUser(userId, userEntity.getDeptId())) {
            throw new EmosException("创建部门经理失败，指定部门已经存在部门经理");
        }
        userService.updateUser(userEntity);
        return R.ok("保存成功");
    }

    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:DELETE"}, logical = Logical.OR)
    public R deleteUser(@Validated(DeleteGroup.class) @RequestBody UserEntity userEntity) {
        Integer userId = userEntity.getId();
        userService.deleteUserById(userId);
        return R.ok("删除成功");
    }

    @GetMapping("getUser")
    @ApiOperation("获取用户信息")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:SELECT"}, logical = Logical.OR)
    public R getUser(@RequestParam("userId") Integer userId) {
        UserEntity result = userService.getUserById(userId);
        return R.ok().put("result", result);
    }


    @GetMapping("/getContactList")
    @ApiOperation("获取联系人列表")
    public R getContactList() {
        GetContactsListResult resultWrap = userService.getContactList();
        return R.ok().put("result", resultWrap.getContactListMap());
    }




}
