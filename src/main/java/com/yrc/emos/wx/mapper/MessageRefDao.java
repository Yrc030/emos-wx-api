package com.yrc.emos.wx.mapper;

import com.yrc.emos.wx.pojo.entity.MessageRefEntity;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-21:33
 * Time: 21:33
 */
public interface MessageRefDao {


    String insert(MessageRefEntity messageRef);


    long countUnread(Integer userId);


    long countLatest(Integer userId);

    boolean updateToReadById(String id);

    boolean deleteById(String id);


    boolean deleteAllByUserId(Integer userId);

}
