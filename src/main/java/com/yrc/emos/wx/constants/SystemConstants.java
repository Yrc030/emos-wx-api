package com.yrc.emos.wx.constants;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * 系统常量类，当SpringBoot项目启动后从数据库中加载 sys_config 表中的值
 * 考勤规则：
 *  attendanceStartTime -- attendanceTime 时间段属于正常上班考勤
 *  attendanceTime -- attendanceEndTime 时间段属于迟到考勤
 *  attendanceEndTime 之后属于缺勤
 *
 *  closingStartTime -- closingTime 时间段属于正常下班考勤
 *  closingTime -- closingEndTime 时间段属于加班考勤
 *  closingEndTime 之后属于早退
 */
@ToString
@Data
@Component
public class SystemConstants {
    /**
     * 上班考勤开始时间
     */
    public  String attendanceStartTime;
    /**
     * 上班时间
     */
    public String attendanceTime;
    /**
     * 上班考勤截至时间
     */
    public String attendanceEndTime;

    /**
     * 下班打卡开始时间
     */
    public String closingStartTime;
    /**
     * 下班时间
     */
    public String closingTime;
    /**
     * 下班考勤截止时间
     */
    public String closingEndTime;
}
