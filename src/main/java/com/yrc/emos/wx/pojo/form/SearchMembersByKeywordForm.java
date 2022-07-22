package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Description:
 * User: joker
 * Date: 2022-06-09-20:07
 * Time: 20:07
 */
@ApiModel
@Data
public class SearchMembersByKeywordForm {

    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,15}$")
    private String keyword;
}
