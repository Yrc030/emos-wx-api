package com.yrc.emos.wx;

import com.yrc.emos.wx.pojo.to.SearchUserTaskListByPageTo;
import com.yrc.emos.wx.service.ApprovalService;
import com.yrc.emos.wx.service.impl.ApprovalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Description:
 * User: joker
 * Date: 2022-07-19-10:21
 * Time: 10:21
 */
@SpringBootTest
public class WorkflowTest {

    @Autowired
    ApprovalService service;


    /**
     * 用于测试:
     */
    //@Test
    ////public void test(){
    ////    SearchUserTaskListByPageTo to = new SearchUserTaskListByPageTo();
    ////    to.setPage(1);
    ////    to.setLength(20);
    ////    to.setType("待审批");
    ////    service.searchUserTaskListByPage(to);
    //}

}
