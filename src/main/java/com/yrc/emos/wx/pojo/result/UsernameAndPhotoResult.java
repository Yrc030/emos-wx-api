package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-07-07-8:31
 * Time: 8:31
 */
@Data
@ApiModel
public class UsernameAndPhotoResult {
    @ApiModelProperty
    private Integer id;

    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private String photo;
}
