package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Description:
 * User: joker
 * Date: 2022-06-06-18:25
 * Time: 18:25
 */
@ToString
@Data
@ApiModel("会议列表页会议项结果")
public  class MeetingListItemResult {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("会议标题")
    private String title;

    @ApiModelProperty("会议发起人")
    private String name;

    @ApiModelProperty("会议类型")
    private String type;

    @ApiModelProperty("会议地点")
    private String place;

    @ApiModelProperty("会议状态")
    private String status;

    @ApiModelProperty("会议描述")
    private String desc;

    @ApiModelProperty("会议日期")
    private String date;

    @ApiModelProperty("会议开始时间")
    private String start;

    @ApiModelProperty("会议结束时间")
    private String end;

    @ApiModelProperty("参会人数")
    private Integer num;
}
