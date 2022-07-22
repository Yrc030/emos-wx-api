package com.yrc.emos.wx.config.xss;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Xss 脚本攻击过滤器
 */
@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter /*implements OrderedFilter*/ {
    //private int order = 1;  // 过滤器优先级，越小等级越高

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(requestWrapper, servletResponse);
    }

    /*@Override
    public int getOrder() {
        return order;
    }*/
}
