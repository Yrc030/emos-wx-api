package com.yrc.emos.wx.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: joker
 * Date: 2022-05-14-10:06
 * Time: 10:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "emos.jwt")
public class JWTProperties {
    /**
     * 密钥
     */
    private String secret;


    /**
     * 过期时间（天）
     */
    private int expire;


    /**
     * 缓存过期时间（天）
     */
    private int cacheExpire;
}
