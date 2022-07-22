package com.yrc.emos.wx;

import cn.hutool.core.util.StrUtil;
import com.yrc.emos.wx.constants.SystemConstants;
import com.yrc.emos.wx.pojo.entity.SysConfigEntity;
import com.yrc.emos.wx.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
@ServletComponentScan
public class EmosWxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmosWxApiApplication.class, args);
    }

    @Autowired
    private SystemConstants systemConstants;

    @Autowired
    private SysConfigMapper sysConfigDao;


    @Value("${emos.image-folder}")
    private String imageFolder;


    /**
     * 当 SpringBoot 项目启动完毕后初始化 systemConstants 示例
     */
    @PostConstruct
    private void init() {
        // 初始化系统常量
        List<SysConfigEntity> sysConfigs = sysConfigDao.selectSysConfigs();
        sysConfigs.forEach(config -> {
            String key = StrUtil.toCamelCase(config.getParamKey());
            try {
                Field field = systemConstants.getClass().getDeclaredField(key);
                field.setAccessible(true);
                field.set(systemConstants, config.getParamValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("初始化系统常量异常: ", e);
            }
        });


        // 创建人脸图片保存文件夹
        boolean created = new File(imageFolder).mkdirs();
    }
}
