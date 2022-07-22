package com.yrc.emos.wx;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Description:
 * User: joker
 * Date: 2022-05-25-20:29
 * Time: 20:29
 */
@Log4j2
@SpringBootTest
public class MailTest {

    // 注入 JavaMailSender
    @Autowired
    private JavaMailSender mailSender;


    @Value("${emos.email.system}")
    private String sender;

    @Value("${emos.email.hr}")
    private String receiver;


    @Test
    public void sendMailTest() {
        // 创建 MailMessage对象，设置参数
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1030104176@qq.com");  // 发件人邮箱
        message.setTo("liu1030104176@163.com");  // 收件人邮箱
        message.setSubject("纯文本邮件标题");  // 邮箱标题
        message.setText("你好，我是拜登，借我200块买尿不湿，我让你做阿妹负总捅，不借小心我滋你，respect~!");  // 邮箱文本内容
        try {
            // 发送邮件
            mailSender.send(message);
            log.info("-------邮件发送成功-------");
        } catch (MailException e) {
            log.error("邮件发送失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
