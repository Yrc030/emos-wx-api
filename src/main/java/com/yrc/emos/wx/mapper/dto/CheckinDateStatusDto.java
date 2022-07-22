package com.yrc.emos.wx.mapper.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * Description:
 * User: joker
 * Date: 2022-05-27-10:41
 * Time: 10:41
 */
@Data
public class CheckinDateStatusDto {
    /**
     * 签到日期
     */
    private String date;
    /**
     * 签到状态
     */
    private String status;
}
