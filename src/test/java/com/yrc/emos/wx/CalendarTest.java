package com.yrc.emos.wx;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yrc.emos.wx.pojo.entity.HolidaysEntity;
import com.yrc.emos.wx.pojo.entity.WorkdayEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: joker
 * Date: 2022-05-27-11:12
 * Time: 11:12
 */
public class CalendarTest {


    /**
     * 用于测试:
     */
    @Test
    public void test() {
        // https://www.mxnzp.com/api/holiday/list/year/2022?ignoreHoliday=false&app_id=nuetvmiuspttrsgo&app_secret=d2F1cGl4L2F3ZVhJdzRQaExHNlVEdz09
        String url = "https://www.mxnzp.com/api/holiday/list/year/" + DateUtil.year(DateUtil.date());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ignoreHoliday", false);
        paramMap.put("app_id", "nuetvmiuspttrsgo");
        paramMap.put("app_secret", "d2F1cGl4L2F3ZVhJdzRQaExHNlVEdz09");
        String res = HttpUtil.get(url, paramMap);
        JSONObject json = JSONUtil.parseObj(res);
        System.out.println(json);
        JSONArray jsonArray = json.get("data", JSONArray.class);
        System.out.println(jsonArray);

        List<HolidaysEntity> holidays = new ArrayList<>();
        List<WorkdayEntity> workdays = new ArrayList<>();

        List<JSONObject> months = JSONUtil.toList(jsonArray, JSONObject.class);

        for (JSONObject month : months) {

            JSONArray jsonArrayDays = month.get("days", JSONArray.class);
            List<JSONObject> days = JSONUtil.toList(jsonArrayDays, JSONObject.class);

            for (JSONObject day : days) {
                String date = day.get("date", String.class);
                int weekDay = day.get("weekDay", Integer.class);
                // type	整形 类型 0 工作日 1 假日 2 节假日 如果ignoreHoliday参数为true，这个字段不返回
                int type = day.get("type", Integer.class);
                // 如果在周一至周五区间，并且是休息日，则算做特殊休息日
                if (1 <= weekDay && weekDay <= 5 && (type == 1 || type == 2)) {
                    HolidaysEntity holiday = new HolidaysEntity();
                    holiday.setDate(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
                    holidays.add(holiday);
                    continue;
                }
                // 如果是周六或周日，并且是工作日，则算做特殊工作日
                if ((6 == weekDay || 7 == weekDay) && type == 0) {
                    WorkdayEntity workday = new WorkdayEntity();
                    workday.setDate(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
                    workdays.add(workday);
                }
            }
        }

        System.out.println("holidays = " + holidays);
        System.out.println("workdays = " + workdays);



    }
}
