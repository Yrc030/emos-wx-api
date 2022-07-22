package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-05-29-11:10
 * Time: 11:10
 */


@Data
@ApiModel("用户概要信息")
public class UserSummaryForm {

    //TODO UserSummaryForm -> UserSummaryResult

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("头像")
    private String photo;

    @ApiModelProperty("部门名称")
    private String deptName;


}
