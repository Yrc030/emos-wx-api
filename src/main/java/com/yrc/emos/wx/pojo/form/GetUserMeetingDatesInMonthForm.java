package com.yrc.emos.wx.pojo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: joker
 * Date: 2022-07-09-22:05
 * Time: 22:05
 */
@ApiModel
@Data
public class GetUserMeetingDatesInMonthForm {
    @Range(min = 2000, max = 3000)
    @NotNull
    private Integer year;

    @Range(min = 1, max = 12)
    @NotNull
    private Integer month;
}
