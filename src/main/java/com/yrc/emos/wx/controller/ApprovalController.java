package com.yrc.emos.wx.controller;

import cn.hutool.core.bean.BeanUtil;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.pojo.form.ApprovalTaskForm;
import com.yrc.emos.wx.pojo.form.SearchUserTaskListByPageForm;
import com.yrc.emos.wx.pojo.to.ApproveTaskTo;
import com.yrc.emos.wx.pojo.to.SearchUserTaskListByPageTo;
import com.yrc.emos.wx.service.ApprovalService;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-19-10:33
 * Time: 10:33
 */
@RestController
@Api("审批模块Web接口")
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${emos.code}")
    private String code;

    @PostMapping("/searchUserTaskListByPage")
    @ApiOperation("查询审批任务分页列表")
    @RequiresPermissions(value = {"WORKFLOW:APPROVAL"})
    public R searchUserTaskListByPage(@Validated @RequestBody SearchUserTaskListByPageForm form, @RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        SearchUserTaskListByPageTo to = new SearchUserTaskListByPageTo();
        BeanUtil.copyProperties(form, to);
        to.setCode(code);
        to.setUserId(userId);
        List result = approvalService.searchUserTaskListByPage(to);
        return R.ok().put("result", result);
    }


    @PostMapping("/approveTask")
    @ApiOperation("审批任务")
    @RequiresPermissions(value = {"WORKFLOW:APPROVAL"})
    public R approveTask(@Validated @RequestBody ApprovalTaskForm form) {
        ApproveTaskTo to = new ApproveTaskTo();
        BeanUtil.copyProperties(form,to);
        to.setCode(code);
        approvalService.approveTask(to);
        return R.ok();
    }

}
