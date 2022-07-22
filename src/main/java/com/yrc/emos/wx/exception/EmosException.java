package com.yrc.emos.wx.exception;

import lombok.Data;

/**
 * Description:
 * User: joker
 * Date: 2022-05-12-23:27
 * Time: 23:27
 */
@Data
public class EmosException extends RuntimeException {
    private String msg;
    private int code;


    public EmosException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EmosException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public EmosException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public EmosException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }


}
