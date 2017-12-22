/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.date;

import com.google.common.base.Strings;
import com.pos.common.util.exception.IllegalParamException;
import org.joda.time.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 简单的日期工具类
 *
 * @author cc
 * @version 1.0, 16/7/13
 */
public class SimpleDateUtils {

    public static final Date MIN_DATE = parseDate("0000-00-00", "yyyy-MM-dd");

    /**
     * 计算两个时间之间相隔的秒数.
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 间隔秒数
     */
    public static int secondsOfDuration(Date start, Date end) {
        LocalDateTime localDateTimeStart = new LocalDateTime(start);
        LocalDateTime localDateTimeEnd = new LocalDateTime(end);

        Seconds seconds = Seconds.secondsBetween(localDateTimeStart, localDateTimeEnd);
        return seconds.getSeconds();
    }

    /**
     * 计算两个时间之间相隔的分钟数.
     *
     * @param timeOneMillis 时间一
     * @param timeTwoMillis 时间二
     * @return 间隔分钟数
     */
    public static int durationInMinutes(long timeOneMillis, long timeTwoMillis) {
        long duration = timeTwoMillis - timeOneMillis;
        if (duration < 0L) {
            duration *= -1;
        }

        return (int) (duration / (1000L * 60L));
    }

    /**
     * 计算两个时间之间相隔的小时数.
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 间隔小时数
     */
    public static int hoursOfDuration(Date start, Date end) {
        LocalDate localDateStart = new LocalDate(start);
        LocalDate localDateEnd = new LocalDate(end);

        Hours durationHours = Hours.hoursBetween(localDateStart, localDateEnd);
        return durationHours.getHours();
    }

    /**
     * 计算两个时间之间相隔的天数.
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 间隔天数
     */
    public static int daysOfDuration(Date start, Date end) {
        LocalDate localDateStart = new LocalDate(start);
        LocalDate localDateEnd = new LocalDate(end);

        Days durationDays = Days.daysBetween(localDateStart, localDateEnd);
        return durationDays.getDays();
    }

    /**
     * 计算两个时间之间相隔的月数.
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 间隔月数
     */
    public static int monthsOfDuration(Date start, Date end) {
        LocalDate localDateStart = new LocalDate(start);
        LocalDate localDateEnd = new LocalDate(end);

        Months durationMonths = Months.monthsBetween(localDateStart, localDateEnd);
        return durationMonths.getMonths();
    }

    /**
     * 计算两个时间之间相隔的年数.
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 间隔年数
     */
    public static int yearsOfDuration(Date start, Date end) {
        LocalDate localDateStart = new LocalDate(start);
        LocalDate localDateEnd = new LocalDate(end);

        Years durationYears = Years.yearsBetween(localDateStart, localDateEnd);
        return durationYears.getYears();
    }

    /**
     * 判断指定时间是否是当天.
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        return getDateOfMidNight().equals(getDate(date, HourMinSecondFormat.MID_NIGHT));
    }

    /**
     * 获取表示当天凌晨(即00:00:00)的日期对象.
     *
     * @return
     */
    public static Date getDateOfMidNight() {
        return getDate(new Date(), HourMinSecondFormat.MID_NIGHT);
    }

    /**
     * 获取表示当天凌晨(即00:00:00)的日期对象.
     *
     * @param date 指定日期
     * @return
     */
    public static Date getDateOfMidNight(Date date) {
        return getDate(date, HourMinSecondFormat.MID_NIGHT);
    }

    /**
     * 获取表示当天截止(即23:59:59)的日期对象.
     *
     * @return
     */
    public static Date getDateOfTodayEnd() {
        return getDate(new Date(), HourMinSecondFormat.TODAY_END);
    }

    /**
     * 获取表示当天截止(即23:59:59)的日期对象.
     *
     * @param date 指定日期
     * @return
     */
    public static Date getDateOfTodayEnd(Date date) {
        return getDate(date, HourMinSecondFormat.TODAY_END);
    }

    /**
     * 格式化日期.
     *
     * @param date    指定日期
     * @param pattern 格式化模式
     * @return 格式化结果
     */
    public static String formatDate(Date date, String pattern) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }

    /**
     * 解析日期字符串到Date类型.
     *
     * @param dateStr 需要转换的String日期
     * @param pattern 日期字符串的格式，如：yyyy-MM-dd
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        return parseDate(dateStr, pattern, null);
    }

    /**
     * 解析日期字符串到Date类型.
     *
     * @param dateStr 需要转换的String日期
     * @param pattern 日期字符串的格式，如：yyyy-MM-dd
     * @param format  设置时分秒的格式化定义（可空）
     *                <br>比如MID_NIGHT表示将指定日期格式化到当天凌晨(00:00:00)</br>
     *                <br>比如TODAY_END表示将指定日期格式化到当天截止(23:59:59)</br>
     * @return
     */
    public static Date parseDate(String dateStr, String pattern, HourMinSecondFormat format) {
        if (Strings.isNullOrEmpty(dateStr) || Strings.isNullOrEmpty(pattern)) {
            return null;
        }

        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalParamException(
                    "日期转换失败, dateStr与pattern不匹配. dateStr = " + dateStr + "  pattern = " + pattern);
        }

        return getDate(date, format);
    }

    private static Date getDate(Date date, HourMinSecondFormat format) {
        if (format == null) {
            return date;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (format) {
            case MID_NIGHT:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case TODAY_END:
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            default:
                break;
        }
        return cal.getTime();
    }

    /**
     * 常用的日期模式定义.
     */
    public enum DatePattern {

        YYYYMMDD("yyyyMMdd"),

        YYYY_MM_DD("yyyy-MM-dd"),

        STANDARD_PATTERN("yyyy-MM-dd HH:mm:ss"),

        YYYYMMDDHHMMSS("yyyyMMddHHmmss");

        private final String value;

        DatePattern(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 时分秒格式化定义.
     */
    public enum HourMinSecondFormat {
        /**
         * 凌晨零点, 即00:00:00
         */
        MID_NIGHT,
        /**
         * 当天截止，即23:59:59
         */
        TODAY_END
    }

}
