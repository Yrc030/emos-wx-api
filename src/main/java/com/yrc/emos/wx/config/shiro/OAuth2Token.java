package com.yrc.emos.wx.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * shiro 可识别的 token 封装类
 */
public class OAuth2Token implements AuthenticationToken {

    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    /**
     * 获取身份信息，类似于用户名
     */
    @Override
    public Object getPrincipal() {
        return token;
    }



    /**
     * 获取凭证信息，类似于密码
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
