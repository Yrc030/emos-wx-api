package com.yrc.emos.wx.service;

import com.yrc.emos.wx.pojo.entity.MessageRefEntity;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-22:01
 * Time: 22:01
 */
public interface MessageRefService {

    String insert(MessageRefEntity messageRef);

    long countUnread(Integer userId);

    long countLatest(Integer userId);

    boolean updateToReadById(String id);

    boolean deleteById(String id);

    boolean deleteAllByUserId(Integer userId);
}
