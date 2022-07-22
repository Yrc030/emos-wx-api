package com.yrc.emos.wx.service.impl;

import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.mapper.MessageDao;
import com.yrc.emos.wx.pojo.result.MessageResult;
import com.yrc.emos.wx.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-22:00
 * Time: 22:00
 */
@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private MessageDao messageDao;

    @Override
    public String insert(MessageEntity message) {
        return messageDao.insert(message);
    }

    @Override
    public List<MessageResult> page(int userId, long skip, long limit) {
        return messageDao.pageByAggregation(userId, skip, limit);
    }

    @Override
    public MessageEntity findById(String id) {
        return messageDao.findById(id);
    }
}
