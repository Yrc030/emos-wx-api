package com.yrc.emos.wx.pojo.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-10:51
 * Time: 10:51
 */
@Data
@ApiModel("用于向创建工作流实例的接口传递的数据")
public class StartMeetingWorkflowTo {


    @ApiModelProperty("通知回调地址（必须）")
    private String url;


    @ApiModelProperty("视频会议室ID（必须）")
    private String uuid;


    @ApiModelProperty("创建者ID（必须）")
    private Integer creatorId;


    @ApiModelProperty("创建者姓名")
    private String creatorName;

    @ApiModelProperty("会议标题")
    private String title;

    @ApiModelProperty("部门经理ID（非必须）")
    private Integer managerId;

    @ApiModelProperty("总经理ID（非必须）")
    private Integer gmId;

    @ApiModelProperty("参会人是否为同一个部门（必须）")
    private Boolean sameDept;

    @ApiModelProperty("慕课网授权字符串（必须）")
    private String code;

    @ApiModelProperty("会议日期（必须）")
    private String date;

    @ApiModelProperty("开始时间（必须）")
    private String start;

    @ApiModelProperty("会议类型")
    private String meetingType;

}
