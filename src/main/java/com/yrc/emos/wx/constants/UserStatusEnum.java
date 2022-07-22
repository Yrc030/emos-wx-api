package com.yrc.emos.wx.constants;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-13:48
 * Time: 13:48
 */
public enum UserStatusEnum {
    ON_JOB(1, "在职"),
    DIMISSION(0, "离职");

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
