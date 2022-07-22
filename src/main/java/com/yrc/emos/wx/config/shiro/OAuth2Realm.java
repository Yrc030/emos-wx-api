package com.yrc.emos.wx.config.shiro;

import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 认证与授权类
 */
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * 判断当前类是否支持给定的令牌类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserEntity user = (UserEntity) principalCollection.getPrimaryPrincipal();
        // 查询用户的权限列表
        Set<String> perms = userService.getUserPermissions(user.getId());
        // 把权限列表添加到info对象中
        authorizationInfo.addStringPermissions(perms);
        return authorizationInfo;
    }

    /**
     * 认证(验证登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从封装的Token类中获取前端传递的 token 令牌
        Object token = authenticationToken.getPrincipal();
        // 从 token 令牌中获取 userId
        Integer userId = jwtUtil.getUserId((String) token);
        // 根据 userId 查询用户信息
        UserEntity user = userService.getById(userId);
        if (user == null) {
            throw new LockedAccountException("账号已被锁定，请联系管理员");
        }
        // 往 info 对象中添加用户信息、Token字符串、Real类的名字
        return new SimpleAuthenticationInfo(user, token, this.getName());
    }
}
