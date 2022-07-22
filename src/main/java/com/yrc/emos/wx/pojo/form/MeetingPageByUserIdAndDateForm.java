package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Description:
 * User: joker
 * Date: 2022-07-09-12:34
 * Time: 12:34
 */
@ApiModel
@Data
public class MeetingPageByUserIdAndDateForm {

    @Range(min = 2000, max = 3000)
    private Integer year;

    @Range(min = 1, max = 12)
    private Integer month;

    @NotNull
    @Min(1)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer limit;
}
