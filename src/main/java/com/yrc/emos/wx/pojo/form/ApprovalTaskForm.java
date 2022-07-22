package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Description:
 * User: joker
 * Date: 2022-07-19-12:11
 * Time: 12:11
 */
@Data
@ApiModel
public class ApprovalTaskForm {

    @NotBlank(message = "taskId不能为空")
    @ApiModelProperty("工作流任务id")
    private String taskId;

    @NotBlank(message = "approval不能为空")
    @Pattern(regexp = "^同意$|^不同意$",message = "approval内容错误")
    @ApiModelProperty("审批意见（同意 or 不同意）")
    private String approval;
}
