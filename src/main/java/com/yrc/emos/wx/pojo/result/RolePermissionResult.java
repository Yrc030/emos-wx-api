package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-13-10:40
 * Time: 10:40
 */
@ApiModel
@Data
public class RolePermissionResult {

    @ApiModelProperty
    private Integer moduleId;
    @ApiModelProperty
    private String moduleName;
    @ApiModelProperty
    private List<ActionResult> action;


    @Data
    @ApiModel
    public static class ActionResult {

        @ApiModelProperty
        private Integer permissionId;

        @ApiModelProperty
        private String actionName;

        @ApiModelProperty
        private Boolean selected;

        @ApiModelProperty("当前操作是否为角色默认（如果是则不能取消该操作）")
        private Boolean acquiescent;
    }
}
