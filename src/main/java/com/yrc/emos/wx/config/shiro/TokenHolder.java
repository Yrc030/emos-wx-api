package com.yrc.emos.wx.config.shiro;

import org.springframework.stereotype.Component;


/**
 * ThreadLocal 包装类，用于保存 token 字符串
 */
@Component
public class TokenHolder {
    ThreadLocal<String> tl = new ThreadLocal<>();

    /**
     * 保存 token 字符串到 threadLocal 中
     */
    public void setToken(String token) {
        tl.set(token);
    }

    /**
     * 获取 threadLocal 中的 token 字符串
     */
    public String getToken() {
        return tl.get();
    }

    /**
     * 清除 threadLocal 中的 token 字符串
     */
    public void clear() {
        tl.remove();
    }
}
