package com.yrc.emos.wx.config.shiro;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.CustomLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类
 */
@Component
@Slf4j
public class JWTUtil {

    @Autowired
    private JWTProperties properties;

    private static final String KEY = "userId";


    /**
     * 根据给定得 userId 创建 token，格式: "header.payload.signature"
     */
    public String createToken(Integer userId) {
        return JWT.create()
                .withClaim(KEY, userId)  // payLoad
                .withExpiresAt(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, properties.getExpire()))  // 过期时间
                .sign(Algorithm.HMAC256(properties.getSecret()));  // signature

    }

    /**
     * 根据给定得 token 获取 userId
     */
    public Integer getUserId(String token) {
        return JWT.decode(token).getClaim(KEY).asInt();
    }

    /**
     * 验证给定的 token，验证成功无返回值，验证失败则抛出异常。
     */
    public void verifyToken(String token) {
        JWT.require(Algorithm.HMAC256(properties.getSecret())).build().verify(token);
    }





}
