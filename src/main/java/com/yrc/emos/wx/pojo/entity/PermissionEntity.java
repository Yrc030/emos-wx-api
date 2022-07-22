package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Getter
@Setter
@TableName("tb_permission")
@ApiModel(value = "PermissionEntity对象", description = "")
public class PermissionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("权限")
    private String permissionName;

    @ApiModelProperty("模块ID")
    private Integer moduleId;

    @ApiModelProperty("行为ID")
    private Integer actionId;


}
