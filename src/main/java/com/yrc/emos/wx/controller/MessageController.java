package com.yrc.emos.wx.controller;

import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.pojo.form.MessagePageForm;
import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.result.MessageResult;
import com.yrc.emos.wx.service.MessageRefService;
import com.yrc.emos.wx.service.MessageService;
import com.yrc.emos.wx.task.MessageTask;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-22:47
 * Time: 22:47
 */
@Api("消息模块web接口")
@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRefService messageRefService;

    @Autowired
    private MessageTask messageTask;


    @ApiOperation("分页获取消息")
    @PostMapping("/page")
    public R page(@Validated @RequestBody MessagePageForm pageForm, @RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        int limit = pageForm.getLimit();
        long skip = (long) (pageForm.getPage() - 1) * limit;
        List<MessageResult> result = messageService.page(userId, skip, limit);
        return R.ok().put("result", result);
    }

    @ApiOperation("获取指定消息")
    @GetMapping("/getMessage/{msgId}")
    public R getMessage(@PathVariable("msgId") String msgId) {
        MessageEntity message = messageService.findById(msgId);
        return R.ok().put("result", message);
    }

    @ApiOperation("更新消息为已读")
    @PostMapping("/updateToRead/{refId}")
    public R updateToRead(@PathVariable("refId") String refId) {
        boolean updated = messageRefService.updateToReadById(refId);
        return updated ? R.ok() : R.error();
    }

    @ApiOperation("删除消息引用")
    @PostMapping("/deleteRef/{refId}")
    public R deleteRef(@PathVariable("refId") String refId) {
        boolean deleted = messageRefService.deleteById(refId);
        return deleted ? R.ok() : R.error();
    }

    @ApiOperation("刷新用户信息")
    @GetMapping("/refreshMessage")
    public R refreshMessage(@RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        // 从消息队列异步接收消息保存至 mongodb 中
        int count = messageTask.receiveMsgAsync(userId);
        // 从 mongodb 中获取当前最新消息的数量
        long latestCount = messageRefService.countLatest(userId);
        // 从 mongodb 中获取当前未读消息的数量
        long unreadCount = messageRefService.countUnread(userId);
        return R.ok().put("latestCount", latestCount).put("unreadCount", unreadCount);
    }
}


