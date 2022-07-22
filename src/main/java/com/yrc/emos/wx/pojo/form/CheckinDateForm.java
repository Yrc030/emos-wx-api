package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-05-27-17:31
 * Time: 17:31
 */

@Data
@ApiModel("考勤日期信息")
public class CheckinDateForm {

    // TODO CheckinDateForm -> CheckinDateInfoResult

    @ApiModelProperty("考勤日期")
    private String date;

    @ApiModelProperty("日期类型（工作日，节假日）")
    private String type;

    @ApiModelProperty("考勤状态")
    private String status;

    @ApiModelProperty("日期的信息描述（周几）")
    private String day;
}
