package com.yrc.emos.wx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.constants.RiskLevelEnum;
import com.yrc.emos.wx.constants.SystemConstants;
import com.yrc.emos.wx.pojo.form.CheckinDateForm;
import com.yrc.emos.wx.pojo.form.CheckinDateStatusForm;
import com.yrc.emos.wx.pojo.form.MonthCheckinForm;
import com.yrc.emos.wx.pojo.form.UserCheckinForm;
import com.yrc.emos.wx.pojo.entity.CheckinEntity;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.mapper.CheckinMapper;
import com.yrc.emos.wx.mapper.HolidaysMapper;
import com.yrc.emos.wx.mapper.UserMapper;
import com.yrc.emos.wx.mapper.WorkdayMapper;
import com.yrc.emos.wx.mapper.dto.CheckinDateStatusDto;
import com.yrc.emos.wx.service.*;
import com.yrc.emos.wx.pojo.bo.CheckinBo;
import com.yrc.emos.wx.task.EmailTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.yrc.emos.wx.constants.EmosConstants.*;

/**
 * <p>
 * 签到表 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
@Scope("prototype")
public class CheckinServiceImpl extends ServiceImpl<CheckinMapper, CheckinEntity> implements CheckinService {


    @Autowired
    private SystemConstants systemConstants;

    @Autowired
    private CheckinMapper checkinDao;

    @Autowired
    private WorkdayMapper workdayDao;

    @Autowired
    private HolidaysMapper holidaysDao;


    @Autowired
    private UserMapper userDao;

    @Autowired
    private FaceModelService faceModelService;

    @Autowired
    private CityService cityService;

    @Autowired
    private EmailTask emailTask;

    @Value("${emos.code}")
    private String code;

    @Value("${emos.face.checkin-url}")
    private String checkinUrl;

    @Value("${emos.email.hr}")
    private String hrEmail;


    @Override
    public String canCheckin(Integer userId) {
        String dateType = DateType.WORKDAY;
        // 判断今天是否为周末
        if (DateUtil.date().isWeekend()) {
            dateType = DateType.HOLIDAY;
        }
        // 判断今天是否为特殊的工作日
        if (workdayDao.selectTodayIsWorkday()) {
            dateType = DateType.WORKDAY;
        }
        // 判断今天是否为特殊的休息日
        else if (holidaysDao.selectTodayIsHoliday()) {
            dateType = DateType.HOLIDAY;
        }
        // 判断今天日期的实际类型是否为节假日
        if (DateType.HOLIDAY.equals(dateType)) {
            return "节假日不能签到";
        }
        // 判断签到时间
        DateTime now = DateTime.now();
        DateTime start = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceStartTime);
        DateTime end = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime);
        if (now.isBefore(start)) {
            return "上班考勤时间尚未开始";
        }
        if (now.isAfter(end)) {
            return "上班考勤时间已经结束";
        }
        // 判断用户是否已经签到
        boolean hasCheckin = checkinDao.selectHasCheckin(userId, start.toString(), end.toString());
        if (hasCheckin) {
            return "用户已经签到";
        }
        return "允许考勤";
    }





    /**
     * 签到功能
     */
    @Override
    @Transactional
    public void checkin(CheckinBo checkinBo) {
        // 判断签到时间
        DateTime now = DateUtil.date();
        DateTime attendanceTime = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceTime);
        DateTime attendanceEndTime = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime);
        int status = CheckinStatus.CHECKED_IN;
        if (now.isBefore(attendanceTime)) {
            status = CheckinStatus.CHECKED_IN;
        } else if (now.isAfter(attendanceTime) && now.isBefore(attendanceEndTime)) {
            status = CheckinStatus.LATE;
        } else {
            // 签到结束时间之后不存入签到表中
            throw new EmosException("签到已结束");
        }
        // TODO 签到前在判断是否存在签到纪录？ 不检查用接口工具调用会报错，有需要再加上
        String res = canCheckin(checkinBo.getUserId());
        if (!"允许考勤".equals(res)) {
            throw new EmosException(res);
        }

        // 查询签到人脸模型
        Integer userId = checkinBo.getUserId();
        String faceModel = faceModelService.getFaceModelByUserId(userId);
        if (StrUtil.isBlank(faceModel)) {
            throw new EmosException("不存在的人脸模型");
        }
        // 向人脸识别程序发送签到请求
        HttpResponse response = checkinToFace(checkinBo, faceModel);
        if (response.getStatus() != 200) {
            throw new RuntimeException("人脸识别服务异常");
        }
        String body = response.body();
        if (FaceModelResponse.UNRECOGNIZED_FACE.equals(body) || FaceModelResponse.MULTIPLE_FACES.equals(body)) {
            throw new EmosException(body);
        } else if (FaceModelResponse.FALSE.equals(body)) {
            throw new EmosException("签到无效，非本人签到");
        } else if (FaceModelResponse.TURE.equals(body)) {
            //获取签到地区新冠疫情风险等级
            String city = checkinBo.getCity();
            String district = checkinBo.getDistrict();
            int risk = 0;
            if (StrUtil.isNotBlank(city) && StrUtil.isNotBlank(district)) {
                String cityCode = cityService.getCode(city);
                if (cityCode == null) {
                    throw new EmosException("未找到所在城市");
                }
                try {
                    risk = getRisk(cityCode, district);
                } catch (IOException e) {
                    log.error("执行异常", e);
                    throw new RuntimeException("获取风险等级失败");
                }
            }
            // 发送告警邮件
            if (risk == RiskLevelEnum.HIGH.getCode()) {
                sendWarningMailAsync(userId, checkinBo.getAddress());
            }

            // 保存签到记录
            CheckinEntity checkin = new CheckinEntity();
            BeanUtil.copyProperties(checkinBo, checkin);
            checkin.setStatus(status);
            checkin.setRisk(risk);
            checkin.setDate(DateUtil.today());
            //baseMapper.insert(checkin);
            checkinDao.insert(checkin);

        } else {
            throw new RuntimeException("未知异常，请联系管理员");
        }

    }


    /**
     * 异步发送告警邮件
     */
    private void sendWarningMailAsync(Integer userId, String address) {
        // 查询用户姓名和所在部门
        HashMap<String, String> map = userDao.selectUsernameAndDeptName(userId);
        String username = map.get("username");
        String deptName = map.get("dept_name");
        deptName = deptName == null ? "" : deptName;

        // 封装  MailMessage 对象
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(hrEmail);
        message.setSubject("员工 " + username + " 身处高风险疫情地区警告");
        message.setText(deptName + "员工" + username + "，" + DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm ") + "处于" + address + "，属于新冠疫情高风险地区，请及时与该员工联系，核实情况！");
        emailTask.sendMailAsync(message);
    }

    /**
     * 向本地宝发送 get 请求，并解析 HTML 页面中的风险等级
     */
    private int getRisk(String cityCode, String district) throws IOException {
        int risk = RiskLevelEnum.LOW.getCode();
        String url = "http://m." + cityCode + ".bendibao.com/news/yqdengji/?qu=" + district;
        Document document = Jsoup.connect(url).get();
        Elements elems = document.getElementsByClass("list-detail");
        if (CollectionUtil.isEmpty(elems)) {
            return risk;
        }
        /**
         * 查询区县的疫情风险等级可能会得到不同街道的疫情风险等级，因为无法知道本地宝如何划分街道，所以疫情风险等级只能精确到
         * 区县级别，为了保险起见，如果区县下存在一个高风险地区，则判定员工所在为高风险地区。
         */
        for (Element elem : elems) {
            String level = elem.text().split(" ")[1];
            if (RiskLevelEnum.HIGH.getName().equals(level)) {
                risk = RiskLevelEnum.HIGH.getCode();
                break;
            } else if (RiskLevelEnum.MEDIUM.getName().equals(level)) {
                risk = RiskLevelEnum.MEDIUM.getCode();
            }
        }
        return risk;
    }

    /**
     * 向人脸识别程序发送人脸签到模型
     */
    private HttpResponse checkinToFace(CheckinBo checkinBo, String faceModel) {
        HttpRequest request = HttpUtil.createPost(checkinUrl);
        request.form("photo", FileUtil.file(checkinBo.getPhotoPath()), "targetModel", faceModel);
        request.form("code", code);
        return request.execute();
    }


    /**
     * 获取用户基本信息和当天考勤
     */
    @Override
    public UserCheckinForm getTodayCheckinInfo(Integer userId) {
        return checkinDao.selectTodayCheckinInfo(userId);
    }


    @Autowired
    private WorkdayService workdayService;


    @Autowired
    private HolidaysService holidaysService;

    /**
     * 获取用户一周考勤情况
     */
    @Override
    public List<CheckinDateForm> getWeekCheckin(Integer userId, DateTime start, DateTime end) {
        // 获取用户本周的考勤信息。
        List<CheckinDateStatusDto> checkinList = checkinDao.selectWeekCheckinStatus(userId, start.toDateStr(), end.toDateStr());
        // 转换为 map， date -> status
        Map<String, String> checkinMap = checkinList.stream().collect(Collectors.toMap(CheckinDateStatusDto::getDate, CheckinDateStatusDto::getStatus));
        // 获取本周的特殊节假日
        Set<String> holidaySet = holidaysService.getHolidaysInRange(start, end);
        // 获取本周的特殊工作日
        Set<String> workdaySet = workdayService.getWorkdaysInRange(start, end);

        // 今天考勤截止时间
        DateTime todayAttendanceEndTime = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime);

        List<CheckinDateForm> res = new ArrayList<>();
        // 创建 [start, end]的日期对象
        DateRange range = DateUtil.range(start, end, DateField.DAY_OF_WEEK);
        range.forEach(one -> {
            CheckinDateForm checkinDateInfo = new CheckinDateForm();
            // 判断当天的类型
            String type = one.isWeekend() ? "节假日" : "工作日";
            if (holidaySet != null && holidaySet.contains(one.toDateStr())) {
                type = "节假日";
            } else if (workdaySet != null && workdaySet.contains(one.toDateStr())) {
                type = "工作日";
            }

            /**判断当天的考勤状态
             * 1. 如果是在今天考勤截止之前的日期：
             *      当天是工作日 && 没有考勤记录，状态 = "缺勤"
             *      当天是工作日 && 存在考勤记录，状态 = 记录的状态
             *      当天是节假日 && 没有考勤记录，状态 = ""
             *      当天是节假日 && 存在考勤记录？状态 = ""
             * 2. 如果是在今天考勤截止之后的日期，数据库中不存在记录，状态 = ""
             */
            String status = "";
            if (one.isBefore(todayAttendanceEndTime)) {
                if ("工作日".equals(type)) {
                    status = checkinMap.getOrDefault(one.toDateStr(), "缺勤");
                } else {
                    // TODO  暂时未开发加班考勤，节假日期间不能考勤。
                }
            }

            // 保存信息
            checkinDateInfo.setType(type);
            checkinDateInfo.setStatus(status);
            checkinDateInfo.setDate(one.toDateStr());
            checkinDateInfo.setDay(one.dayOfWeekEnum().toChinese("周"));
            res.add(checkinDateInfo);
        });
        return res;
    }


    /**
     * 统计用户考勤总天数
     */
    @Override
    public long countCheckinDays(Integer userId) {
        return count(new QueryWrapper<CheckinEntity>().eq("user_id", userId));
    }

    /**
     * 获取月考勤信息
     */
    @Override
    public MonthCheckinForm getMonthCheckin(Integer userId, DateTime start, DateTime end) {
        MonthCheckinForm monthCheckinForm = new MonthCheckinForm();
        // 获取用户本月的考勤信息。
        List<CheckinDateStatusDto> dateStatusList = checkinDao.selectWeekCheckinStatus(userId, start.toDateStr(), end.toDateStr());
        // 转换为 map， date -> status
        Map<String, String> checkinMap = dateStatusList.stream().collect(Collectors.toMap(CheckinDateStatusDto::getDate, CheckinDateStatusDto::getStatus));
        // 获取本月的特殊节假日
        Set<String> holidaySet = holidaysService.getHolidaysInRange(start, end);
        // 获取本月的特殊工作日
        Set<String> workdaySet = workdayService.getWorkdaysInRange(start, end);
        // 今天考勤截止时间
        DateTime todayAttendanceEndTime = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime);
        int normalDays = 0;
        int lateDays = 0;
        int absenceDays = 0;
        List<CheckinDateStatusForm> list = new ArrayList<>();
        // 创建 [start, end]的日期对象
        DateRange range = DateUtil.range(start, end, DateField.DAY_OF_MONTH);
        while (range.hasNext()) {
            DateTime one = range.next();
            String date = one.toDateStr();
            // 判断当天的类型
            String type = one.isWeekend() ? "节假日" : "工作日";
            if (holidaySet != null && holidaySet.contains(date)) {
                type = "节假日";
            } else if (workdaySet != null && workdaySet.contains(date)) {
                type = "工作日";
            }
            String status = "";
            if (one.isBefore(todayAttendanceEndTime)) {
                // 在当天考勤结束之前
                if ("工作日".equals(type)) {
                    if (checkinMap.containsKey(date)) {
                        status = checkinMap.get(date);
                        if ("正常".equals(status)) {
                            normalDays++;
                        } else {
                            lateDays++;
                        }
                    } else {
                        status = "缺勤";
                        absenceDays++;
                    }
                }
            }
            CheckinDateStatusForm form = new CheckinDateStatusForm();
            form.setDate(date);
            form.setStatus(status);
            list.add(form);
        }
        monthCheckinForm.setNormalDays(normalDays);
        monthCheckinForm.setLateDays(lateDays);
        monthCheckinForm.setCheckinDays(normalDays + lateDays);
        monthCheckinForm.setAbsenceDays(absenceDays);
        monthCheckinForm.setMonthCheckinStatus(list);
        return monthCheckinForm;
    }

}
