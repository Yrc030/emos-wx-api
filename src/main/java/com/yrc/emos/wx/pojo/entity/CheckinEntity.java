package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 签到表
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Getter
@Setter
@TableName("tb_checkin")
@ApiModel(value = "CheckinEntity对象", description = "签到表")
public class CheckinEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("签到地址")
    private String address;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区划")
    private String district;

    @ApiModelProperty("考勤结果")
    private Integer status;

    @ApiModelProperty("风险等级")
    private Integer risk;

    @ApiModelProperty("签到日期")
    private String date;

    @ApiModelProperty("签到时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
