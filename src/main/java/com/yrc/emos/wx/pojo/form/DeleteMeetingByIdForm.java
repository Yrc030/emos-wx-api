package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: joker
 * Date: 2022-06-30-11:40
 * Time: 11:40
 */
@Data
@ApiModel("通过id删除会议的Form类")
public class DeleteMeetingByIdForm {
    @NotNull
    @Min(1)
    private Long id;
}
