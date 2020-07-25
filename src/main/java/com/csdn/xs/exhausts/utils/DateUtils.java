package com.csdn.xs.exhausts.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author YJJ
 * @Date: Created in 15:07 2019-12-08
 */
public class DateUtils {

    /**
     * 将java.sql.Timestamp对象转化为String字符串
     * @param time
     *            要格式的java.sql.Timestamp对象
     * @param strFormat
     *            输出的String字符串格式的限定（如："yyyy-MM-dd HH:mm:ss"）
     * @return 表示日期的字符串
     */
    public static String dateToStr(java.sql.Timestamp time, String strFormat) {
        DateFormat df = new SimpleDateFormat(strFormat);
        String str = df.format(time);
        return str;
    }

    /**
     * 获取距离下次年检的天数
     * @param time
     * @return
     */
    public static int getNextMeasureDays(Timestamp time) {
        Date date = time;
        Date date1 = new Date();
        date.setYear(date.getYear() + 1);
        date = getLastDayOfMonth(date.getYear() + 1900, date.getMonth());
        long delta = (date.getTime() - date1.getTime()) / (24 * 3600 * 1000);
        return (int)delta;
    }

    /**
     * 获得该月最后一天
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return cal.getTime();
    }

    /**
     * 判断年检是否逾期
     * @return
     */
    public static boolean isOutOfDate(Timestamp time) {
        Date dateNow = time;
        Date date = getLastDayOfMonth(dateNow.getYear() + 1901, dateNow.getMonth());
        if (date.getTime() < System.currentTimeMillis())
            return true;
        return false;
    }

    public static Timestamp dateStringToTimestamp(String time) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(time);

        return new java.sql.Timestamp(date.getTime());
    }

    public static Date dateStringToDate(String time, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = df.parse(time);
        return date;
    }

    public static Date getDateYearsAgo(int year) {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, nowYear - year);
        return calendar.getTime();
    }
}
