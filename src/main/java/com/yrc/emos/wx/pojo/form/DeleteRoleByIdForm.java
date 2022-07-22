package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: joker
 * Date: 2022-07-13-18:43
 * Time: 18:43
 */
@ApiModel
@Data
public class DeleteRoleByIdForm {
    @NotNull
    @Min(value = 3,message = "禁止删除系统内置角色")
    private Integer id;
}
