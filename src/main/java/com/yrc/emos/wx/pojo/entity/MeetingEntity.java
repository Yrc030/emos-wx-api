package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 会议表
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Getter
@Setter
@TableName("tb_meeting")
@ApiModel(value = "MeetingEntity对象", description = "会议表")
public class MeetingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty("会议题目")
    private String title;

    @ApiModelProperty("创建人ID")
    private Long creatorId;

    @ApiModelProperty("日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty("开会地点")
    private String place;

    @ApiModelProperty("开始时间")
    @TableField("`start`")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime start;

    @ApiModelProperty("结束时间")
    @TableField("`end`")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime end;

    @ApiModelProperty("会议类型（1在线会议，2线下会议）")
    private Integer type;

    @ApiModelProperty("参与者")
    private String members;

    @ApiModelProperty("会议内容")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty("工作流实例ID")
    private String instanceId;


    @ApiModelProperty("出席人员名单")
    private String present;

    @ApiModelProperty("缺席人员名单")
    private String unpresent;

    @ApiModelProperty("状态（1待审批，2已拒绝，3未开始，4进行中，5已结束）")
    @TableField("`status`")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("创建人名称（非表字段）")
    @TableField(exist = false)
    private String name;

}
