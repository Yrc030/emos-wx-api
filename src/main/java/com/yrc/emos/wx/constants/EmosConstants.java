package com.yrc.emos.wx.constants;

/**
 * Description:
 * User: joker
 * Date: 2022-05-19-10:29
 * Time: 10:29
 */

public class EmosConstants {


    /**
     * 管理员常量
     */
    public static class Root {
        public static final String ROOT_REGISTER_CODE = "000000";
        public static final String ROLE = "[0]";
        public static final String NAME = "超级管理员";
    }

    /**
     * 日期类型常量
     */
    public static class DateType {
        public static final String WORKDAY = "工作日";
        public static final String HOLIDAY = "节假日";

    }

    /**
     * 签到状态常量
     */
    public static class CheckinStatus {
        /**
         * 已签到
         */
        public static final int CHECKED_IN = 1;
        /**
         * 迟到
         */
        public static final int LATE = 2;
    }

    /**
     * 人脸识别响应字符串常量
     * if ("".equals(body) || "".equals(body)) {
     * throw new EmosException(body);
     * } else if ("False".equals(body)) {
     * throw new EmosException("签到无效，非本人签到");
     * } else if ("True".equals(body)) {
     */
    public static class FaceModelResponse {
        public static final String UNRECOGNIZED_FACE = "无法识别出人脸";
        public static final String MULTIPLE_FACES = "照片中存在多张人脸";
        public static final String TURE = "True";
        public static final String FALSE = "False";
    }


    public static String ACTIVE_CODE_PREFIX = "activeCode:";


}
