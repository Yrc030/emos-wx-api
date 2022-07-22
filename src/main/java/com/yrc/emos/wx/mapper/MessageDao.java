package com.yrc.emos.wx.mapper;

import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.result.MessageResult;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-21:31
 * Time: 21:31
 */
public interface MessageDao {

    /**
     * 新增一条消息，返回主键
     */
    String insert(MessageEntity message);

    /**
     * 通过聚合进行分页查询
     */
    List<MessageResult> pageByAggregation(int userId, long skip, long limit);

    /**
     * 通过主键查询消息
     */
    MessageEntity findById(String id);

}
