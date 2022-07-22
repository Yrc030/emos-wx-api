package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 节假日表
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@ToString
@Getter
@Setter
@TableName("tb_holidays")
@ApiModel(value = "HolidaysEntity对象", description = "节假日表")
public class HolidaysEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("日期")
    private LocalDate date;


}
