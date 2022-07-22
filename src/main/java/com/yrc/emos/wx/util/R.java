package com.yrc.emos.wx.util;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * web返回对象
 */
public class R extends HashMap<String, Object> {

    private R() {
        // 默认值
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }


    /**
     * 重载的 put 方法，用于链式调用
     */
    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 静态工厂，创建默认的 ok 对象
     * code = 200，msg = "success"
     */
    public static R ok() {
        return new R();
    }

    /**
     * 静态工厂，根据指定的 msg 创建 ok 对象，code = 200
     */
    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    /**
     * 静态工厂，根据指定 map 创建 ok 对象，code = 200
     */
    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    /**
     * 静态工厂，根据指定的 code 和 msg 创建 error 对象
     */
    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    /**
     * 静态工厂，根据指定的 msg 创建 error 对象，code = 500
     */
    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * 静态工厂，创建默认的 error 对象
     */
    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

}
