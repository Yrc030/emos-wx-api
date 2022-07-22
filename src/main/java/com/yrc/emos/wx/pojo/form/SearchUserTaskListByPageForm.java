package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: joker
 * Date: 2022-07-18-12:46
 * Time: 12:46
 */
@Data
@ApiModel("查询审批任务分页列表的form类")
public class SearchUserTaskListByPageForm {

    @NotBlank(message = "type不能为空")
    @ApiModelProperty
    private String type;

    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page不能小于1")
    @ApiModelProperty
    private Integer page;

    @NotNull(message = "length不能为空")
    @Range(min = 1, max = 60,message = "length范围要在1~60之间")
    @ApiModelProperty
    private Integer length;
}
