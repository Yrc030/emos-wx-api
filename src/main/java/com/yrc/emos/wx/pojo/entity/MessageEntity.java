package com.yrc.emos.wx.pojo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-10:28
 * Time: 10:28
 */
@Data
@Document(collection = "message")
@ApiModel("message集合的映射类")
@ToString
public class MessageEntity {

    @Id
    @ApiModelProperty("主键")
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @ApiModelProperty("消息发送者的userId（0 表示系统）")
    @Indexed
    @Field(targetType = FieldType.INT32)
    private Integer senderId;

    @ApiModelProperty("消息发送者的名称")
    @Field(targetType = FieldType.STRING)
    private String senderName;

    @ApiModelProperty("消息发送者的头像")
    @Field(targetType = FieldType.STRING)
    private String senderPhoto;

    @ApiModelProperty("消息发送时间")
    @Indexed
    @Field(targetType = FieldType.DATE_TIME)
    private Date sendTime;

    @ApiModelProperty("消息")
    @Field(targetType = FieldType.STRING)
    private String msg;


}
