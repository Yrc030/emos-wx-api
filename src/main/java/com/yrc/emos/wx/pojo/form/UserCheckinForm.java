package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-05-28-9:21
 * Time: 9:21
 */
@Data
@ApiModel("用户考勤信息")
public class UserCheckinForm {
    // TODO UserCheckinForm -> TodayAndWeekCheckinResult

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户头像")
    private String photo;

    @ApiModelProperty("所在部门名称")
    private String deptName;

    @ApiModelProperty("签到地址")
    private String address;


    @ApiModelProperty("考勤状态")
    private String status;


    @ApiModelProperty("所在地区疫情风险等级")
    private String risk;

    @ApiModelProperty("今天日期")
    private String date;

    @ApiModelProperty("今天签到时间")
    private String checkinTime;

    @ApiModelProperty("上班时间")
    private String attendanceTime;


    @ApiModelProperty("下班时间")
    private String closingTime;

    @ApiModelProperty("考勤总天数")
    private long checkinDays;


    @ApiModelProperty("一周考勤记录 CheckinDateStatusBo")
    private List<CheckinDateForm> weekCheckin;

}
