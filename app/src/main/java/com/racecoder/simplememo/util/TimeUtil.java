package com.racecoder.simplememo.util;

import android.text.TextUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class TimeUtil {
    private static final String DEFAULT_PATTERN = "yyyy年MM月dd日  HH:mm:ss";

    public static String getCurrentTime() {
        return getCurrentTime(DEFAULT_PATTERN);
    }

    public static String getCurrentTime(String pattern) {
        assert !TextUtils.isEmpty(pattern);
        return DateTimeFormatter.ofPattern(pattern).format(ZonedDateTime.now(ZoneId.systemDefault()));
    }

    public static String formatPattern(ZonedDateTime time) {
        return DateTimeFormatter.ofPattern(DEFAULT_PATTERN).format(time);
    }

    /**
     * 将当前时间格式化为ISO格式: 2021-02-07T14:05:44.8464995+08:00
     */
    public static String currentDateISO() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now());
    }

    /**
     * 将时间格式化为ISO格式: 2021-02-07T14:05:44.8464995+08:00
     */
    public static String formatDateISO(ZonedDateTime dateTime) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(dateTime);
    }

    /**
     * 将ISO格式字符串格式化为时间: 2021-02-07T14:05:44.8464995+08:00
     */
    public static ZonedDateTime formatISOString(String isoDate) {
        return ZonedDateTime.parse(isoDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}