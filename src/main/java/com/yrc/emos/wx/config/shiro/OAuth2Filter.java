package com.yrc.emos.wx.config.shiro;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: joker
 * Date: 2022-05-14-21:53
 * Time: 21:53
 */
//@Component
//@Scope("prototype")
public class OAuth2Filter extends AuthenticatingFilter {


    @Autowired
    private TokenHolder tokenHolder;

    @Autowired
    private JWTUtil jwtUtil;


    @Autowired
    private JWTProperties jwtProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /*
    流程图：
              request
                 ↓
    -------------------------------------------
    |     AuthenticatingFilter                |
    |            ↓                            |
    |  判断是否允许放行 isAccessAllowed()       |   → 允许，继续执行 FilterChain#doFilter()
    |            ↓                            |
    |  拒绝，判断token合法性 onAccessDenied()   |   → 非法 token 返回 false，拦截请求
    |            ↓                            |
    |  合法 token，执行认证 executeLogin()     |   → 认证失败，执行 onLoginFailure() ，返回 false 拦截请求
    |            ↓                            |
    |  认证成功，执行 onLoginSuccess() 返回true |
    |            ↓                            |
    |  继续执行 FilterChain#doFilter()         |
    |            ↓                            |
    -------------------------------------------

     */


    /*AuthenticatingFilter#onPreHandle() 方法:

        public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 先判断是否允许放行，如果不允许放行则进行认证
        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }
     */

    /**
     * 判断是否放行请求
     *
     * @return true 放行请求，false 拦截请求并进行认证
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // options 请求已经被 CorsFilter 拦截，此处暂时将所有请求拦截，并交给 shiro 进行认证
        return false;
    }


    /**
     * 当请求被拦截时调用，判断 token 合法性，并进行身份认证
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        tokenHolder.clear();  // 清除 threadLocal 中保存的 token

        String token = this.getRequestToken(req);
        if (StrUtil.isBlank(token)) {
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);  // 返回错误信息
            resp.getWriter().print("无效的令牌");
            return false;
        }

        try {
            // 验证令牌
            jwtUtil.verifyToken(token);
        } catch (TokenExpiredException e) {
            // 令牌过期
            if (stringRedisTemplate.hasKey(token)) {
                // 缓存中令牌未过期，则刷新令牌
                // 删除缓存中的旧令牌
                stringRedisTemplate.delete(token);
                // 创建新令牌
                Integer userId = jwtUtil.getUserId(token);
                String newToken = jwtUtil.createToken(userId);
                // 保存到缓存
                stringRedisTemplate.opsForValue().set(newToken, userId.toString(), jwtProperties.getCacheExpire(), TimeUnit.DAYS);
                // 保存到 threadLocal
                tokenHolder.setToken(token);
            } else {
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
                resp.getWriter().print("缓存令牌已过期");
                return false;
            }
        } catch (SignatureVerificationException e) {
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print("无效的令牌");
            return false;
        } catch (JWTException e) {
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print("验证令牌时发生错误，请联系管理员");
            return false;
        }

        // 执行认证，间接调用 Realm 类
        return executeLogin(request, response);
    }

    /*AuthenticatingFilter#executeLogin() 方法：

    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = this.createToken(request, response);  // 创建 token
        if (token == null) {  // 返回为null抛异常
            String msg = "错误信息...";
            throw new IllegalStateException(msg);
        } else {
            try {
                Subject subject = this.getSubject(request, response); // 获取主体
                subject.login(token);  // 使用 token 进行登录
                return this.onLoginSuccess(token, subject, request, response);  // 登录成功时执行
            } catch (AuthenticationException var5) {
                return this.onLoginFailure(token, var5, request, response);  // 登录失败时执行
            }
        }
    }*/


    /**
     * 认证时创建令牌对象
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        String token = getRequestToken((HttpServletRequest) request);
        if (StrUtil.isBlank(token)) {
            return null;  // 返回 null 则调用方法会抛异常
        }
        return new OAuth2Token(token);  // 创建 OAuth2Token 对象并返回
    }


    /**
     * 认证失败时执行
     */
    @SneakyThrows(IOException.class)
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        resp.getWriter().print("认证失败: " + e.getMessage());

        return false;
    }


    /**
     * 获取请求携带的 token
     */
    private String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader("token");   // 先从请求头中取
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");   // 请求头中没有，则再从请求参数中取
        }
        return token;
    }

}
