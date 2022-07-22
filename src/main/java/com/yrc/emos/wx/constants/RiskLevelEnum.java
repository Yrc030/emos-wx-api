package com.yrc.emos.wx.constants;

/**
 * Description:
 * User: joker
 * Date: 2022-05-25-16:41
 * Time: 16:41
 */
public enum RiskLevelEnum {

    LOW("低风险", 1),
    MEDIUM("中风险", 2),
    HIGH("高风险", 3);



    RiskLevelEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }


    private final String name;
    private final int code;


    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
