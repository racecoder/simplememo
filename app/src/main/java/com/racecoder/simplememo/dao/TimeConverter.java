package com.racecoder.simplememo.dao;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.racecoder.simplememo.util.TimeUtil;

import java.time.ZonedDateTime;

public class TimeConverter {
    @TypeConverter
    public static ZonedDateTime strToDate(String isoTime) {
        return TextUtils.isEmpty(isoTime) ? null : TimeUtil.formatISOString(isoTime);
    }

    @TypeConverter
    public static String dateToStr(ZonedDateTime dateTime) {
        return dateTime == null ? null : TimeUtil.formatDateISO(dateTime);
    }
}