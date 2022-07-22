package com.yrc.emos.wx.aop;

import cn.hutool.core.util.StrUtil;
import com.yrc.emos.wx.config.shiro.TokenHolder;
import com.yrc.emos.wx.util.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenAspect {

    @Autowired
    private TokenHolder tokenHolder;

    /**
     * 切入点：controller 包下的所有类（不包含子包下）
     */
    @Pointcut("within(com.yrc.emos.wx.controller.*)")
    private void pointCut() {
    }

    @Around("pointCut()")
    public R around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行目标方法，获取返回值
        R r = (R) joinPoint.proceed();
        // 获取 threadLocal中的 token
        String token = tokenHolder.getToken();
        if (StrUtil.isNotBlank(token)) {
            // 如果存在 token，保存到返回值中
            r.put("token", token);
            tokenHolder.clear();
        }
        return r;
    }
}
