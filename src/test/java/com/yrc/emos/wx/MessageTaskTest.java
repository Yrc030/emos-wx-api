package com.yrc.emos.wx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.entity.MessageRefEntity;
import com.yrc.emos.wx.service.MessageRefService;
import com.yrc.emos.wx.service.MessageService;
import com.yrc.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Description:
 * User: joker
 * Date: 2022-06-04-22:15
 * Time: 22:15
 */
@Slf4j
public class MessageTaskTest extends EmosWxApiApplicationTests {

    @Autowired
    private MessageTask messageTask;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRefService messageRefService;


    @Autowired
    private ConnectionFactory connectionFactory;


    /**
     * 用于测试:
     */
    @Test
    public void test(){
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSenderId(0);
        messageEntity.setSenderName("系统消息");
        messageEntity.setMsg("Hello World !!!");
        messageEntity.setSendTime(new Date());
        messageEntity.setSenderPhoto("https://emos-static.oss-cn-hangzhou.aliyuncs.com/img/System.jpg");

        messageTask.sendMsgAsync("message.18", messageEntity);
    }

    /**
     * 用于测试:
     */
    @Test
    public void testSend() {
        for (int i = 0; i < 100; i++) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setSenderId(0);
            messageEntity.setSenderName("系统消息");
            messageEntity.setMsg("Hello World ~ " + i);
            messageEntity.setSendTime(new Date());
            messageEntity.setSenderPhoto("https://emos-static.oss-cn-hangzhou.aliyuncs.com/img/System.jpg");

            String messageId = messageService.insert(messageEntity);

            MessageRefEntity refEntity = new MessageRefEntity();

            refEntity.setMessageId(messageId);
            refEntity.setIsLatest(true);
            refEntity.setHasRead(false);
            refEntity.setReceiverId(18);
            messageRefService.insert(refEntity);
        }
    }


    @Test
    public void producer() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare("topic.message", "topic");
            for (int i = 1; i <= 100; i++) {
                String msg = "Hello,Wolrd" + i;
                channel.basicPublish("topic.message", "message.18", null, msg.getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            log.error("错误: {}", e.getMessage());
        }
    }


    @Test
    public void consumer() throws Exception {

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("topic.message", "topic");
        channel.queueDeclare("queue", true, false, false, null);
        channel.queueBind("queue", "topic.message", "#.18");
        channel.basicConsume("queue", false, (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody());

            log.info("{}", msg);

            long deliveryTag = delivery.getEnvelope().getDeliveryTag();
            channel.basicAck(deliveryTag, false);
        }, consumerTag -> {
        });


    }

}

