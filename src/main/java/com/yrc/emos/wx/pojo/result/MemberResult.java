package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-06-09-20:47
 * Time: 20:47
 */
@Data
@ApiModel
public class MemberResult {

    @ApiModelProperty("用户隶属部门编号")
    private Integer deptId;

    @ApiModelProperty("用户隶属部门")
    private String deptName;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("用户头像")
    private String photo;

}
