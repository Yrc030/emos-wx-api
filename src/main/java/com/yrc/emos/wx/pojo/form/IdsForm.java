package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-06-10-9:06
 * Time: 9:06
 */
@Data
@ApiModel
public class IdsForm {
    @NotEmpty(message = "参数 ids 不能为空")
    @ApiModelProperty
    private List<Integer> ids;
}
