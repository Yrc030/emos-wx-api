package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.yrc.emos.wx.config.String2ArrayTypeHandler;
import com.yrc.emos.wx.constraints.annotations.IntListValue;
import com.yrc.emos.wx.constraints.group.DeleteGroup;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Data
@TableName("tb_user")
@ApiModel(value = "UserEntity对象", description = "用户表")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
    @Min(value = 1, groups = {UpdateGroup.class, DeleteGroup.class})
    @Null(groups = InsertGroup.class)
    private Integer id;

    @ApiModelProperty("长期授权字符串")
    private String openId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像网址")
    private String photo;

    @ApiModelProperty("姓名")
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class})
    private String name;

    @ApiModelProperty("性别")
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class})
    @Pattern( regexp = "^男$|^女$", groups ={UpdateGroup.class, InsertGroup.class} )
    private String sex;

    @ApiModelProperty("手机号码")
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class})
    @Pattern(regexp = "^1[0-9]{10}$", groups = {UpdateGroup.class, InsertGroup.class})
    private String tel;

    @ApiModelProperty("邮箱")
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class})
    @Pattern(regexp = "^([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$",  groups = {UpdateGroup.class, InsertGroup.class})
    private String email;

    @ApiModelProperty("入职日期")
    @NotNull(groups = {UpdateGroup.class, InsertGroup.class})
    private LocalDate hiredate;

    @ApiModelProperty("角色")
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class})
    private String role;

    @ApiModelProperty("是否是超级管理员")
    private Boolean root;

    @ApiModelProperty("部门编号")
    @NotNull(groups = {UpdateGroup.class, InsertGroup.class})
    private Integer deptId;

    @ApiModelProperty("状态（逻辑删除字段） 1：在职，0：离职")
    @TableLogic
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 部门名，表中不存在
    @TableField(exist = false)
    private String deptName;

    // 用户角色名，表中不存在
    @TableField(exist = false,typeHandler = String2ArrayTypeHandler.class)
    private String[] roles;


}
