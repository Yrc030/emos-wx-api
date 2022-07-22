package com.yrc.emos.wx.constants;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-13:51
 * Time: 13:51
 */
public enum RoleEnum {
    ROOT(0, "超级管理员"),
    GM(1, "总经理"),
    MANAGER(2, "部门经理"),
    EMPLOYEE(3, "普通员工");


    RoleEnum(Integer code, String desc) {
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
