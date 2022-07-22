package com.yrc.emos.wx.service;


import com.yrc.emos.wx.pojo.to.ApproveTaskTo;
import com.yrc.emos.wx.pojo.to.SearchUserTaskListByPageTo;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-18-12:42
 * Time: 12:42
 */
public interface ApprovalService {
   List searchUserTaskListByPage(SearchUserTaskListByPageTo to);
   void approveTask(ApproveTaskTo to);
}
