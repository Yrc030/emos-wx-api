package com.yrc.emos.wx.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Description:
 * User: joker
 * Date: 2022-05-19-13:11
 * Time: 13:11
 */
@Configuration
// @MapperScan(basePackages = "com.yrc.emos.wx.mapper")
public class MybatisPlusConfig {


    /**
     * 设置自动填充创建时间
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 没有自动更新字段
            }
        };
    }


    @Bean
    public String2ArrayTypeHandler string2ArrayTypeHandler() {
        return new String2ArrayTypeHandler();
    }
}
