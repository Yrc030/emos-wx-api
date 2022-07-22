package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-29-17:41
 * Time: 17:41
 */
@Data
@ApiModel("月考勤数据")
public class MonthCheckinForm {

    // TODO MonthCheckinForm -> MonthCheckinResult

    @ApiModelProperty("本月考勤总天数")
    private Integer checkinDays;

    @ApiModelProperty("正常考勤天数")
    private Integer normalDays;


    @ApiModelProperty("迟到考勤天数")
    private Integer lateDays;


    @ApiModelProperty("缺勤天数")
    private Integer absenceDays;


    @ApiModelProperty("月考勤状态信息")
    private List<CheckinDateStatusForm> monthCheckinStatus;


}
