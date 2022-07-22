package com.yrc.emos.wx.task;

import com.rabbitmq.client.*;
import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.entity.MessageRefEntity;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.service.MessageRefService;
import com.yrc.emos.wx.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * User: joker
 * Date: 2022-06-02-9:53
 * Time: 9:53
 */
@Component
@Slf4j
public class MessageTask {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRefService messageRefService;


    private String queueNamePrefix = "message.";

    /**
     * 同步发送消息
     */
    public void sendMsg(String queueName, MessageEntity message) {
        // 保存至 mongobd 的 message 集合中
        String messageId = messageService.insert(message);
        try (Connection connection = connectionFactory.newConnection();  // 建立连接
             Channel channel = connection.createChannel()) {   // 建立通道
            // 声明队列
            channel.queueDeclare(queueName, true, false, false, null);
            HashMap<String, Object> headers = new HashMap<>();
            // 保存 messageId 至 headers 中
            headers.put("messageId", messageId);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().headers(headers).build();
            // 发布消息
            channel.basicPublish("", queueName, properties, message.getMsg().getBytes());
            log.debug("消息“{}”发送成功! ", message.getMsg());
        } catch (TimeoutException e) {
            log.error("发送消息时获取 MQ 连接超时: {}", e.getMessage());
            throw new EmosException(e.getMessage());
        } catch (IOException e) {
            log.error("向 MQ 发送消息失败: {}", e.getMessage());
            throw new EmosException(e.getMessage());
        }
    }

    /**
     * 异步发送消息
     */
    @Async("asyncTaskExecutor")
    public void sendMsgAsync(String queueName, MessageEntity message) {
        sendMsg(queueName, message);
    }


    /**
     * 同步接收消息
     */
    public int receiveMsg(Integer receiveId) {
        int count = 0;
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            String queueName = queueNamePrefix + receiveId;
            while (true) {
                // 拉取一条消息
                GetResponse response = channel.basicGet(queueName, false);
                if (response == null) {
                    break;
                }
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                String messageId = response.getProps().getHeaders().get("messageId").toString();
                log.debug("接收到新消息: {}", new String(response.getBody()));
                MessageRefEntity messageRef = new MessageRefEntity();
                messageRef.setMessageId(messageId);
                messageRef.setReceiverId(receiveId);
                messageRef.setHasRead(false);
                messageRef.setIsLatest(true);
                // 插入 mongodb 的 messageRef 集合中
                messageRefService.insert(messageRef);
                // 手动 ack
                channel.basicAck(deliveryTag, false);
                count++;
            }
        } catch (TimeoutException e) {
            log.error("接收消息时获取 MQ 连接超时: {}", e.getMessage());
            throw new EmosException(e.getMessage());
        } catch (IOException e) {
            log.error("接收 MQ 消息失败: {}", e.getMessage());
            throw new EmosException(e.getMessage());
        }

        return count;
    }


    @Async("asyncTaskExecutor")
    public int receiveMsgAsync(Integer receiveId) {
        return receiveMsg(receiveId);
    }


    public void deleteQueue(Integer receiveId) {

        String queueName = queueNamePrefix + receiveId;

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDelete(queueName);
            log.debug("删除队列成功");
        } catch (TimeoutException e) {
            log.error("删除队列时获取 MQ 连接超时: {}", e.getMessage());
            throw new EmosException(e.getMessage());
        } catch (IOException e) {
            log.error("删除队列失败: {}", e.getMessage());
            throw new EmosException(e.getMessage());
        }
    }

    @Async("asyncTaskExecutor")
    public void deleteQueueAsync(Integer receiveId) {
        deleteQueue(receiveId);
    }
}
