package com.yrc.emos.wx.mapper.impl;

import com.yrc.emos.wx.pojo.entity.MessageRefEntity;
import com.yrc.emos.wx.mapper.MessageRefDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-21:33
 * Time: 21:33
 */
@Repository
public class MessageRefDaoImpl implements MessageRefDao {


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入一条 messageRef
     */
    @Override
    public String insert(MessageRefEntity messageRef) {
        messageRef = mongoTemplate.insert(messageRef);
        return messageRef.getId();
    }

    /**
     * 统计用户未读消息数
     */
    @Override
    public long countUnread(Integer userId) {
        return mongoTemplate.count(
                new Query(Criteria.where("receiverId").is(userId).and("hasRead").is(false)),
                MessageRefEntity.class);
    }

    /**
     * 统计用户的最新消息数
     */
    @Override
    public long countLatest(Integer userId) {
        // 查询最新消息数量并更新为旧消息
        return mongoTemplate.updateMulti(
                new Query(Criteria.where("receiverId").is(userId).and("isLatest").is(true)),
                new Update().set("isLatest", false),
                MessageRefEntity.class).getModifiedCount();
    }

    /**
     * 更新 messageRef 关联的消息为已读
     */
    @Override
    public boolean updateToReadById(String id) {
        long modifiedCount = mongoTemplate.updateFirst(
                new Query(Criteria.where("_id").is(id)),
                new Update().set("hasRead", true),
                MessageRefEntity.class).getModifiedCount();
        return modifiedCount == 1;
    }


    /**
     * 删除一条 messageRef
     */
    @Override
    public boolean deleteById(String id) {
        long deletedCount = mongoTemplate.remove(
                new Query(Criteria.where("_id").is(id)),
                MessageRefEntity.class).getDeletedCount();
        return deletedCount == 1;
    }

    /**
     * 删除用户所有的消息
     */
    @Override
    public boolean deleteAllByUserId(Integer userId) {
        long deletedCount = mongoTemplate.remove(
                new Query(Criteria.where("receiverId").is(userId)),
                MessageRefEntity.class).getDeletedCount();
        return deletedCount > 0;
    }

}
