package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-05-29-17:46
 * Time: 17:46
 */
@Data
@ApiModel("考勤日期状态")
public class CheckinDateStatusForm {

    // TODO 删除与 CheckinDateForm 共用
    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("考勤状态（正常，早退，缺勤）")
    private String status;


}
