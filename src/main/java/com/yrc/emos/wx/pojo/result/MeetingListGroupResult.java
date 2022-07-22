package com.yrc.emos.wx.pojo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-06-06-16:09
 * Time: 16:09
 */
@Data
@ApiModel("会议列表页会议分组结果")
public class MeetingListGroupResult {

    @ApiModelProperty("会议日期")
    private String date;

    @ApiModelProperty("返回会议记录的页码")
    private Integer page;

    @ApiModelProperty("返回的会议记录是否在最后一页")
    @JsonProperty("isLast")
    private boolean isLast;

    @ApiModelProperty("当天日期的会议记录")
    private List<MeetingListItemResult> meetings;


}
