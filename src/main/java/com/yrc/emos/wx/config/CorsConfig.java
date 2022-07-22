package com.yrc.emos.wx.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Description: 跨域请求配置
 */
@Configuration
public class CorsConfig {

    /**
     * 配置 CORS 跨域过滤器注册Bean
     */
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
        // 设置跨域规则
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");      // 所有 origin
        corsConfiguration.addAllowedHeader("*");      // 所有 Header
        corsConfiguration.addAllowedMethod("*");      // 所有 方法
        corsConfiguration.setAllowCredentials(true);  // 允许传递 cookie
        corsConfiguration.setMaxAge(3600L);           // 最大存活时间

        FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        corsFilterFilterRegistrationBean.setFilter(new CorsFilter(source));
        corsFilterFilterRegistrationBean.setOrder(0);  // 设置过滤器优先级
        return corsFilterFilterRegistrationBean;
    }


}
