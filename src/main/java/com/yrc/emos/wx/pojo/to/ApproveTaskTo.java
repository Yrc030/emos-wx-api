package com.yrc.emos.wx.pojo.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-07-19-12:07
 * Time: 12:07
 */
@Data
@ApiModel
public class ApproveTaskTo {

    @ApiModelProperty("工作流任务id")
    private String taskId;

    @ApiModelProperty("审批意见（同意 or 不同意）")
    private String approval;

    @ApiModelProperty("授权字符串")
    private String code;
}
