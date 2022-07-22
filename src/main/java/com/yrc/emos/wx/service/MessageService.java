package com.yrc.emos.wx.service;

import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.result.MessageResult;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-21:59
 * Time: 21:59
 */
public interface MessageService {

    /**
     * 新增一条消息，返回主键
     */
    String insert(MessageEntity message);

    /**
     * 通过聚合进行分页查询
     */
    List<MessageResult> page(int userId, long skip, long limit);

    /**
     * 通过主键查询消息
     */
    MessageEntity findById(String id);
}
