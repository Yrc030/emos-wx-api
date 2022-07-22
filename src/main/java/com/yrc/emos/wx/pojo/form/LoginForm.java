package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * User: joker
 * Date: 2022-05-22-16:39
 * Time: 16:39
 */
@Data
@ApiModel("登录用户的Form类")
public class LoginForm {
    @NotBlank(message = "临时授权凭证不能为空")
    @ApiModelProperty("临时授权凭证")
    private String code;
}
