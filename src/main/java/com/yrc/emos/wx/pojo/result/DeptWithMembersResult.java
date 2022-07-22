package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-06-09-20:03
 * Time: 20:03
 */
@ApiModel
@Data
public class DeptWithMembersResult {
    @ApiModelProperty("部门ID")
    private Integer id;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门人数")
    private Long count;

    @ApiModelProperty("部门员工")
    private List<MemberResult> members;
}
