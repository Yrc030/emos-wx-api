package com.yrc.emos.wx.pojo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-18-8:33
 * Time: 8:33
 */
@Data
@ApiModel
public class GetContactsListResult {

    @ApiModelProperty
    private LinkedHashMap<String, List<ContactResult>> contactListMap;

    @Data
    @ApiModel
    public static class ContactResult {
        @ApiModelProperty
        private String name;

        @ApiModelProperty
        private String dept;

        @ApiModelProperty
        private String tel;
    }
}
