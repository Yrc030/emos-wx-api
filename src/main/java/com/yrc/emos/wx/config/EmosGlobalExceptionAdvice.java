package com.yrc.emos.wx.config;

import com.yrc.emos.wx.exception.EmosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class EmosGlobalExceptionAdvice {

    /**
     * 处理方法参数无效异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("方法参数无效：", e);
        return e.getBindingResult().getFieldError().getDefaultMessage();
    }

    /**
     * 处理未授权异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException e) {
        log.error("请求未授权：", e);
        return "您不具备相关权限";
    }


    /**
     * 处理其它内部异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error("执行异常：", e);
        if (e instanceof EmosException) {
            return ((EmosException) e).getMsg();
        }
        return "服务器执行异常";
    }


}
