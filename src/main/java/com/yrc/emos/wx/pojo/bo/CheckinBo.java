package com.yrc.emos.wx.pojo.bo;


import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-05-25-15:52
 * Time: 15:52
 */
@Data
public class CheckinBo {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 签到详细地址
     */
    private String address;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 人脸图片地址
     */
    private String photoPath;
}
