package com.point.clddock.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DDateUtil {
    public static final ThreadLocal<SimpleDateFormat> DATETIME_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final ThreadLocal<SimpleDateFormat> DAY_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyMMddHHmmss"));
    public static final ThreadLocal<SimpleDateFormat> YDAY_FORMATT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    public static final ThreadLocal<SimpleDateFormat> DATETIME_EDIFORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy年MM月dd日 HH:mm"));
    public static final ThreadLocal<SimpleDateFormat> NYR_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy年MM月dd日"));


    public static String format(Date date) {
        return DATETIME_FORMATTER.get().format(date);
    }
}
