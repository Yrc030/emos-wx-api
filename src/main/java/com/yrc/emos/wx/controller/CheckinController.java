package com.yrc.emos.wx.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.constants.SystemConstants;
import com.yrc.emos.wx.pojo.form.CheckinDateForm;
import com.yrc.emos.wx.pojo.form.CheckinForm;
import com.yrc.emos.wx.pojo.form.MonthCheckinForm;
import com.yrc.emos.wx.pojo.form.UserCheckinForm;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.service.CheckinService;
import com.yrc.emos.wx.service.UserService;
import com.yrc.emos.wx.pojo.bo.CheckinBo;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;


/**
 * Description:
 * User: joker
 * Date: 2022-05-23-21:15
 * Time: 21:15
 */
@Slf4j
@RestController
@RequestMapping("/checkin")
@Api("签到模块Web接口")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SystemConstants systemConstants;

    @Value("${emos.image-folder}")
    private String imageFolder;


    @GetMapping("/canCheckin")
    @ApiOperation("检查是否可以签到")
    public R canCheckin(@RequestHeader("token") String token) {
        Integer userId = jwtUtil.getUserId(token);
        String res = checkinService.canCheckin(userId);
        return R.ok(res);
    }


    @PostMapping("/checkin")
    @ApiOperation("签到接口")
    public R checkin(@Validated CheckinForm checkinForm, @RequestPart("photo") MultipartFile file, @RequestHeader("token") String token) {
        if (file.isEmpty()) {
            return R.error("没有上传图片");
        }

        String photoName = file.getOriginalFilename();
        if (!StrUtil.endWithIgnoreCase(photoName, ".jpg")) {
            return R.error("必须提交JPG格式图片");
        }

        String photoPath = imageFolder + File.separator + photoName;
        try {
            // 将图片写入文件夹中
            file.transferTo(Paths.get(photoPath));
            // 签到
            Integer userId = jwtUtil.getUserId(token);
            CheckinBo checkinBo = new CheckinBo();
            BeanUtil.copyProperties(checkinForm, checkinBo);
            checkinBo.setUserId(userId);
            checkinBo.setPhotoPath(photoPath);
            checkinService.checkin(checkinBo);
            return R.ok("签到成功");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new EmosException("保存图片错误");
        } finally {
            // 删除临时图片
            FileUtil.del(photoPath);
        }
    }


    @GetMapping("/getUserCheckin")
    @ApiOperation("获取用户签到信息")
    public R getUserCheckin(@RequestHeader("token") String token) {

        Integer userId = jwtUtil.getUserId(token);
        // 获取用户今天的签到信息
        UserCheckinForm result = checkinService.getTodayCheckinInfo(userId);

        // 获取用户签到总天数
        long checkinDays = checkinService.countCheckinDays(userId);

        // 获取用户一周考勤记录
        DateTime hiredate = userService.getUserHiredate(userId);
        DateTime today = DateUtil.date();
        DateTime start = DateUtil.beginOfWeek(today);  // 本周一
        DateTime end = DateUtil.endOfWeek(today);      // 本周日
        if (hiredate.isAfter(end)) {
            throw new EmosException("用户入职日期异常, 请联系HR修改");
        }
        if (start.isBefore(hiredate)) {
            start = hiredate;
        }
        List<CheckinDateForm> weekCheckin = checkinService.getWeekCheckin(userId, start, end);

        // 封装响应对象
        result.setCheckinDays(checkinDays);
        result.setAttendanceTime(systemConstants.attendanceTime);
        result.setClosingTime(systemConstants.closingTime);
        result.setWeekCheckin(weekCheckin);
        return R.ok().put("result", result);
    }


    @GetMapping("/getMonthCheckin/{year}/{month}")
    @ApiOperation("获取月考勤数据")
    public R getMonthCheckin(
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @RequestHeader("token") String token) {

        Integer userId = jwtUtil.getUserId(token);
        // 构建查询范围
        DateTime hiredate = userService.getUserHiredate(userId);
        // 构建指定年月的1号日期: yyyy-MM-01
        DateTime start = DateUtil.parse(year + "-" + (month < 10 ? "0" + month : month) + "-01");
        // 查询月份在入职日期月份之前 或 查询月份是下个月 直接返回
        if (start.isBefore(DateUtil.beginOfMonth(hiredate)) || start.isAfter(DateUtil.endOfMonth(new Date()))) {
            return R.error("查询的月份无考勤记录");
        }
        // 查询的月份是入职日期当月，从入职日期往后开始查询
        if (start.isBefore(hiredate)) {
            start = hiredate;
        }

        DateTime end = DateUtil.endOfMonth(start);
        // 获取月考勤数据
        MonthCheckinForm monthCheckinForm = checkinService.getMonthCheckin(userId, start, end);
        return R.ok().put("result", monthCheckinForm);
    }
}
