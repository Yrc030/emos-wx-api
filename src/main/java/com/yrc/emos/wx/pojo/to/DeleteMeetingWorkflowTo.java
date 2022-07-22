package com.yrc.emos.wx.pojo.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-06-29-17:06
 * Time: 17:06
 */
@Data
@ApiModel("用于向删除工作流实例的接口传递的数据")
public class DeleteMeetingWorkflowTo {

    @ApiModelProperty("工作流实例id")
    private String instanceId;

    @ApiModelProperty("删除原因")
    private String reason;

    @ApiModelProperty("慕课授权字符串")
    private String code;

    @ApiModelProperty("在线会议房间号")
    private String uuid;

}
