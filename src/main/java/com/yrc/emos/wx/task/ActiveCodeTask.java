package com.yrc.emos.wx.task;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: joker
 * Date: 2022-07-15-8:37
 * Time: 8:37
 */
@Component
@Scope("prototype")
public class ActiveCodeTask {


    @Autowired
    private EmailTask emailTask;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Async("asyncTaskExecutor")
    public void sendActiveCodeAsync(int userId, String mail) {
        // 生成激活码
        String prefix = "activeCode:";
        String code = null;
        String key = null;
        while (true) {
            code = RandomUtil.randomNumbers(6);
            key = prefix + code;
            if (BooleanUtil.isFalse(redisTemplate.hasKey(key))) {
                break;
            }
        }

        // 保存到redis缓存中（一天）
        redisTemplate.opsForValue().set(key, userId + "", 1, TimeUnit.DAYS);


        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject("EMOS系统新员工激活码");
        message.setText("你的激活码为：" + code);
        emailTask.sendMailAsync(message);
    }
}
