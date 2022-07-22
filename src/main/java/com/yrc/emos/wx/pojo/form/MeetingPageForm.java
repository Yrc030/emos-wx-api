package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Description:
 * User: joker
 * Date: 2022-06-06-16:16
 * Time: 16:16
 */
@Data
@ApiModel("会议分页参数")
public class MeetingPageForm {


    @ApiModelProperty("起始页")
    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page必须大于1的整数")
    private Integer page;


    @ApiModelProperty("每页显示数量")
    @NotNull(message = "limit不能为空")
    @Range(min = 1, max = 40, message = "每页显示数量范围在 [1,40]")
    private Integer limit;


    @ApiModelProperty("会议日期")
    @Pattern(regexp = "^\\d{4}-\\d{1,2}-\\d{1,2}")
    private String date;

}
