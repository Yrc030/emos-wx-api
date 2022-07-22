package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-21:21
 * Time: 21:21
 */
@Data
@ApiModel("响应给前端的消息对象")
public class MessageResult {

    @ApiModelProperty("消息实体主键")
    private String id;

    @ApiModelProperty("消息引用的主键")
    private String refId;

    @ApiModelProperty("消息发送者的名称")
    private String senderName;

    @ApiModelProperty("消息发送者的头像")
    private String senderPhoto;

    @ApiModelProperty("消息发送时间")
    private Date sendTime;

    @ApiModelProperty("消息")
    private String msg;

    @ApiModelProperty("消息是否已读")
    private Boolean hasRead;


}
