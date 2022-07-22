package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-06-14-21:20
 * Time: 21:20
 */
@ApiModel("会议详情页会议详情结果类")
@Data
public class MeetingDetailResult {

    /*
    @ApiModelProperty("会议ID")
    private Long id;
    */

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("会议创建者id")
    private Long creatorId;

    @ApiModelProperty("会议标题")
    private String title;

    @ApiModelProperty("会议日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty("会议开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String start;

    @ApiModelProperty("会议结束时间")
    @DateTimeFormat(pattern = "HH:mm")
    private String end;

    @ApiModelProperty("会议类型")
    private Integer type;

    @ApiModelProperty("会议状态")
    private Integer status;

    @ApiModelProperty("会议地点")
    private String place;

    @ApiModelProperty("会议描述")
    private String desc;

    @ApiModelProperty("工作流实例ID")
    private String instanceId;

    @ApiModelProperty("会议成员列表")
    private List<MemberResult> members;

}
