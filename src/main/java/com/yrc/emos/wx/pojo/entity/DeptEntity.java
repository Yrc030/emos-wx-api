package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.yrc.emos.wx.constraints.group.DeleteGroup;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
@TableName("tb_dept")
@ApiModel(value = "DeptEntity对象", description = "")
public class DeptEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
    @Null(groups = InsertGroup.class)
    private Integer id;

    @ApiModelProperty("部门名称")
    @NotBlank(groups =  {UpdateGroup.class, InsertGroup.class})
    private String deptName;


}
