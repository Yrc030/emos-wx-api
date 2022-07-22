package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yrc.emos.wx.config.String2ArrayTypeHandler;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Getter
@Setter
@TableName("tb_role")
@ApiModel(value = "RoleEntity对象", description = "角色表")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = UpdateGroup.class)
    @Min(value = 1, groups = UpdateGroup.class)
    @Null(groups = InsertGroup.class)
    private Integer id;

    @NotBlank(groups = InsertGroup.class)
    @Null(groups = UpdateGroup.class)
    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("权限集合")
    @NotEmpty(groups = {UpdateGroup.class, InsertGroup.class} )
    @TableField(typeHandler = String2ArrayTypeHandler.class)
    private String[] permissions;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("系统角色内置权限")
    private String defaultPermissions;

    @ApiModelProperty("是否为系统内置角色（0：否，1：是）")
    private Boolean systemic;

}
