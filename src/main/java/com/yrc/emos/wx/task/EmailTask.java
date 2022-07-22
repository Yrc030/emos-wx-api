package com.yrc.emos.wx.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: joker
 * Date: 2022-05-25-22:31
 * Time: 22:31
 */
@Slf4j
@Component
@Scope("prototype")
public class EmailTask {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${emos.email.system}")
    private String mailbox;


    @Async("asyncTaskExecutor")
    public void sendMailAsync(SimpleMailMessage message) {
        message.setFrom(mailbox);
        //message.setCc(mailbox); // 邮件抄送，防止被服务器认定为垃圾邮件
        javaMailSender.send(message);
        log.info("发送给邮箱: " + message.getFrom() + "的邮件\n标题: " + message.getSubject() + "\n内容:" + message.getText());
    }

}
