package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-05-23-21:17
 * Time: 21:17
 */

@Data
@ApiModel("人脸签到的Form类")
public class CheckinForm {

    @ApiModelProperty("签到详细地址")
    private String address;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区、县")
    private String district;

}
