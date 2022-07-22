package com.yrc.emos.wx.pojo.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: joker
 * Date: 2022-07-18-12:49
 * Time: 12:49
 */
@Data
@ApiModel
public class SearchUserTaskListByPageTo {

    @ApiModelProperty
    private String type;
    @ApiModelProperty
    private Integer page;
    @ApiModelProperty
    private Integer length;
    @ApiModelProperty("课程授权字符串")
    private String code;
    @ApiModelProperty
    private Integer userId;

}
