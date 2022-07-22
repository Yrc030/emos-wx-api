package com.yrc.emos.wx.config.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Shiro 配置类：配置Realm，Filter，SecurityManager... 到 SpringBoot
 */
@Configuration
public class ShiroConfig {

    /**
     * 注入 ShiroFilterFactoryBean，配置拦截规则
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, OAuth2Filter oAuth2Filter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 注册 OAuth2Filter
        String filterName = "oAuth2";
        HashMap<String, Filter> filters = new HashMap<>();
        filters.put(filterName, oAuth2Filter);
        shiroFilterFactoryBean.setFilters(filters);

        // 配置拦截规则
        // TODO 一定要用 LinkedHashMap 因为精确的路径要放在前面，否则失效
        HashMap<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");  // anon：AnonymousFilter，指定 URL 可匿名访问，即不拦截
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/user/register", "anon");
        filterMap.put("/user/login", "anon");
        filterMap.put("/test/**", "anon");
        filterMap.put("/**", filterName);
        filterMap.put("/meeting/receiveNotify", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 注入安全管理器，用于封装 Realm
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(AuthorizingRealm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        defaultWebSecurityManager.setRememberMeManager(null); // 设置服务端不保存凭证（客户端保存）
        return defaultWebSecurityManager;
    }


    /**
     * 注入 OAuth2Realm（具体的认证和授权类）
     */
    @Bean
    public OAuth2Realm oAuth2Realm() {
        OAuth2Realm oAuth2Realm = new OAuth2Realm();
        // TODO 配置凭证信息匹配器
        //HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //hashedCredentialsMatcher.setHashIterations(1024);
        //oAuth2Realm.setCredentialsMatcher(hashedCredentialsMatcher);
        return oAuth2Realm;
    }

    @Bean
    @Scope("prototype")
    public OAuth2Filter oAuth2Filter() {
        return new OAuth2Filter();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
