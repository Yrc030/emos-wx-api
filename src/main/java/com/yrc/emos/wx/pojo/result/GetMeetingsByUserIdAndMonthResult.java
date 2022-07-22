package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Description:
 * User: joker
 * Date: 2022-07-09-11:27
 * Time: 11:27
 */
@Data
@ApiModel
public class GetMeetingsByUserIdAndMonthResult {

    @ApiModelProperty("会议id")
    private Long id;

    @ApiModelProperty("会议标题")
    private String title;


    @ApiModelProperty("会议时长")
    private String duration;

    @ApiModelProperty("会议日期")
    private String date;

    @ApiModelProperty("会议开始时间")
    private String start;

    @ApiModelProperty("会议类型")
    private String type;

    @ApiModelProperty("会议地点（仅当 type == '线下会议' 时）")
    private String place;

    @ApiModelProperty("会议创建人头像")
    private String photo;



}
