package com.yrc.emos.wx.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.pojo.to.ApproveTaskTo;
import com.yrc.emos.wx.pojo.to.SearchUserTaskListByPageTo;
import com.yrc.emos.wx.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-18-12:52
 * Time: 12:52
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Value("${workflow.url.base}")
    private String workflowBaseUrl;

    @Value("${workflow.url.search-user-task-list}")
    private String searchUserTaskListUrl;

    @Value("${workflow.url.approval-task}")
    private String approvalTask;



    @Override
    public List searchUserTaskListByPage(SearchUserTaskListByPageTo to) {

        String url = workflowBaseUrl + searchUserTaskListUrl;
        HttpResponse response = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(to), "application/json")
                .execute();

        if (!response.isOk()) {
            throw new EmosException("查询工作流审批任务失败");
        }

        /* response.body.result 的格式
        [
            {
                "date":"2022-09-18",
                "hours":0,
                "start":"09:47",
                "photo":"https://...",
                "type":1,
                "uuid":"81c6...",
                "members":"单松行、侯香柔",
                "name":"李四",
                "sameDept":true,
                "end":"10:17",
                "id":1429,
                "place":"",
                "processType":"meeting",
                "taskId":"98f8...",
                "desc":"..."
            },
            {...},
            {...},
        ]
         */

        return JSONUtil.parseObj(response.body()).get("result", ArrayList.class);
    }


    @Override
    public void approveTask(ApproveTaskTo to) {
        String url = workflowBaseUrl +  approvalTask;
        HttpResponse resp = HttpUtil.createPost(url)
        .body(JSONUtil.toJsonStr(to),"application/json")
        .execute();
        if (resp.getStatus() != 200) {
            throw new EmosException("审批工作流审批任务失败");
        }
    }
}
