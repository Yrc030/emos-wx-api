package com.yrc.emos.wx;

import cn.hutool.core.date.DateUtil;
import com.mongodb.client.result.DeleteResult;
import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.entity.MessageRefEntity;
import com.yrc.emos.wx.pojo.result.MessageResult;
import com.yrc.emos.wx.service.MessageRefService;
import com.yrc.emos.wx.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-17:01
 * Time: 17:01
 */
public class MongoTest extends EmosWxApiApplicationTests {

    @Autowired
    private MessageService messageService;


    /*************************MessageService*************************/


    /**
     * 用于测试: MessageService#insert()
     */
    @Test
    public void testMessageInsert() {
        MessageEntity entity = new MessageEntity();
        entity.setSenderId(0);
        entity.setSenderPhoto("xxxx");
        entity.setSenderName("emos系统");
        entity.setMsg("Hello,SpringBoot!!");
        entity.setSendTime(new Date());
        messageService.insert(entity);
    }


    /**
     * 用于测试:
     */
    @Test
    public void testMessageFindById() {
        MessageEntity entity = messageService.findById("6296236e08ea2c31e3d870c6");

        System.out.println("entity = " + entity);
    }


    /**
     * 用于测试:
     */
    @Test
    public void testMessagePage() {
        List<MessageResult> results = messageService.page(14, 0, 10);

        System.out.println("results = " + results);
    }


    /*************************MessageRefService*************************/
    @Autowired
    private MessageRefService messageRefService;

    /**
     * 用于测试: MessageRefService#insert()
     */
    @Test
    public void testMessageRefInsert() {
        MessageRefEntity entity = new MessageRefEntity();
        entity.setMessageId("629626bfdda5f00bf42d3044");
        entity.setReceiverId(14);
        entity.setHasRead(false);
        entity.setIsLatest(true);
        messageRefService.insert(entity);
    }


    /**
     * 用于测试:
     */
    @Test
    public void testMessageCountUnread() {
        Long count = messageRefService.countUnread(14);
        System.out.println("count = " + count);
    }


    /**
     * 用于测试:
     */
    @Test
    public void testUpadteToRead() {
        boolean b = messageRefService.updateToReadById("629627dca3774b20e8a88446");
    }


    /**
     * 用于测试:
     */
    @Test
    public void testCountLatest() {
        long count = messageRefService.countLatest(14);
        System.out.println("count = " + count);
    }


    @Test
    public void testDeleteById() {
        boolean delete = messageRefService.deleteById("629627dca3774b20e8a88446");
    }


    /**
     * 用于测试:
     */
    @Test
    public void test() {
        boolean delete = messageRefService.deleteAllByUserId(14);
    }


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 用于测试:
     */
    @Test
    public void delete() {
        List<MessageEntity> entities = mongoTemplate.findAllAndRemove(new Query(Criteria.where("sendTime").gt(DateUtil.parse("2022-06-05 00:00:00", "yyyy-MM-dd HH:mm:ss"))), MessageEntity.class);


        List<String> ids = entities.stream().map(MessageEntity::getId).collect(Collectors.toList());

        DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("messageId").in(ids)), MessageRefEntity.class);

        System.out.println("result = " + result);

    }
}


