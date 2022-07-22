package com.yrc.emos.wx.pojo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

/**
 * Description:
 * User: joker
 * Date: 2022-05-31-10:36
 * Time: 10:36
 */
@Data
@Document(collection = "message_ref")
@ApiModel("message_ref集合映射类")
public class MessageRefEntity {
    @Id
    @ApiModelProperty("主键")
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Indexed
    @ApiModelProperty("关联到的message实体的主键")
    @Field(targetType = FieldType.STRING)
    private String messageId;

    @Indexed
    @ApiModelProperty("接受这的userId")
    @Field(targetType = FieldType.INT32)
    private Integer receiverId;

    @ApiModelProperty("消息是否已读")
    @Field(targetType = FieldType.BOOLEAN)
    private Boolean hasRead;

    @Indexed
    @ApiModelProperty("是否为最新消息")
    @Field(targetType = FieldType.BOOLEAN)
    private Boolean isLatest;


}
