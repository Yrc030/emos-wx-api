package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-07-13-18:23
 * Time: 18:23
 */
@Data
@ApiModel
public class GetUserRolesResult {

    @ApiModelProperty
    private Integer id;

    @ApiModelProperty
    private String roleName;

}
