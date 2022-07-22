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
 * 模块资源表
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Getter
@Setter
@TableName("tb_module")
@ApiModel(value = "ModuleEntity对象", description = "模块资源表")
public class ModuleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("模块编号")
    private String moduleCode;

    @ApiModelProperty("模块名称")
    private String moduleName;


}
