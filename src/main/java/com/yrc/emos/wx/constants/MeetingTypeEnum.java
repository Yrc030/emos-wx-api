package com.yrc.emos.wx.constants;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-15:16
 * Time: 15:16
 */
public enum MeetingTypeEnum {
    ONLINE(1, "线上会议"),
    OFFLINE(2, "线下会议");

    MeetingTypeEnum(Integer code, String desc) {
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
