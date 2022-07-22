package com.yrc.emos.wx.constants;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-18:43
 * Time: 18:43
 */
public enum MeetingStatusEnum {

    PENDING(1, "待审批"),
    REFUSED(2, "已拒绝"),
    NOT_STARTED(3, "未开始"),
    IN_PROGRESS(4, "进行中"),
    END(5,"已结束");


    MeetingStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
