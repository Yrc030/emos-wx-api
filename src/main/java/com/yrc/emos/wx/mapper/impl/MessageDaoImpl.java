package com.yrc.emos.wx.mapper.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.entity.MessageRefEntity;
import com.yrc.emos.wx.mapper.MessageDao;
import com.yrc.emos.wx.pojo.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-14:54
 * Time: 14:54
 */

@Repository
public class MessageDaoImpl implements MessageDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 新增一条消息，返回主键
     */
    @Override
    public String insert(MessageEntity message) {
        message = mongoTemplate.insert(message);
        return message.getId();
    }

    /**
     * 通过聚合进行分页查询
     *
     *
     * db.message.aggregate([
     *
     * {  $set:  { id:  { $toString: "$_id" } }  }, // 将 _id 从 objectid 转换为 string 类型
     * {  $lookup: {  // left join message_ref r on m.id = r.messageId
     *     from: "message_ref", // 连接集合
     *     localField:"id", // 本地集合字段
     *     foreignField:"messageId",  // 关联字段
     *     as:"ref" // 结果存放在 ref 数组
     *     }
     * },
     * {  $match:  { "ref.receiverId":14  } }, // where ref.receiverId = 14
     * { $sort: {  sendTime: -1 } },  // sort by sendTime desc
     * {  $skip: 0 },   // limit 0 20
     * {  $limit: 20 }
     * ])
     */
    @Override
    public List<MessageResult> pageByAggregation(int userId, long skip, long limit) {
        JSONObject json = new JSONObject();
        json.set("$toString", "$_id");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.addFields().addField("id").withValue(json).build(),
                Aggregation.lookup("message_ref", "id", "messageId", "ref"),
                Aggregation.match(Criteria.where("ref.receiverId").is(userId)),
                Aggregation.sort(Sort.by("sendTime").descending()),
                Aggregation.skip(skip),
                Aggregation.limit(limit));
        List<HashMap> maps = mongoTemplate.aggregate(aggregation, "message", HashMap.class).getMappedResults();

        if (CollectionUtil.isEmpty(maps)) {
            return null;
        }
        List<MessageResult> results = new ArrayList<>();

        maps.forEach(map -> {
            MessageResult message = new MessageResult();
            for (Field field : message.getClass().getDeclaredFields()) {
                Object value = map.get(field.getName());
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(message, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            List<MessageRefEntity> refs = (List<MessageRefEntity>) map.get("ref");
            MessageRefEntity ref = refs.get(0);

            message.setHasRead((boolean) ref.getHasRead());
            message.setRefId(ref.getId());
            //message.setIsLatest(ref.getIsLatest());

            results.add(message);
        });
        return results;
    }

    /**
     * 通过主键查询消息
     */
    @Override
    public MessageEntity findById(String id) {
        return mongoTemplate.findById(id, MessageEntity.class, "message");
    }
}
