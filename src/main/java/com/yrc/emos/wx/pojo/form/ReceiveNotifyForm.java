package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * User: joker
 * Date: 2022-07-05-15:47
 * Time: 15:47
 */
@Data
@ApiModel
public class ReceiveNotifyForm {
    @NotBlank
    private String processId;
    @NotBlank
    private String uuid;
    @NotBlank
    private String result;
}

