package com.yrc.emos.wx.service.impl;

import com.yrc.emos.wx.pojo.entity.MessageRefEntity;
import com.yrc.emos.wx.mapper.MessageRefDao;
import com.yrc.emos.wx.service.MessageRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-22:03
 * Time: 22:03
 */
@Service
public class MessageRefServiceImpl implements MessageRefService {

    @Autowired
    private MessageRefDao messageRefDao;


    @Override
    public String insert(MessageRefEntity messageRef) {
        return messageRefDao.insert(messageRef);
    }


    @Override
    public long countUnread(Integer userId) {
        return messageRefDao.countUnread(userId);
    }

    @Override
    public long countLatest(Integer userId) {
        return messageRefDao.countLatest(userId);
    }

    @Override
    public boolean updateToReadById(String id) {
        return messageRefDao.updateToReadById(id);
    }

    @Override
    public boolean deleteById(String id) {
        return messageRefDao.deleteById(id);
    }

    @Override
    public boolean deleteAllByUserId(Integer userId) {
        return messageRefDao.deleteAllByUserId(userId);
    }
}
