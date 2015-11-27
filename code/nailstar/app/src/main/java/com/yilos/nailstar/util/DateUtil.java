package com.yilos.nailstar.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by sisilai on 15/11/21.
 */
public class DateUtil {

    /**
     * @return 长整形时间戳 13位
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }
    /**
     * @param lt 长整形时间戳
     * @return 年和月组成的字符串
     */
    public static String getYearAndMonth(long lt) {
       String commentTime = new SimpleDateFormat("MM月dd日").format(lt);
        return commentTime;
    }

    /**
     * 获取两个时间差的描述， 类似 2年前，3月前，2小时前， 3分钟前， 刚刚
     * @param from
     * @param to
     * @return
     */
    public static String getUpdateDateString(Date from, Date to) {
        DateSpace dateSpace = getMaxUnitSpace(from, to);

        switch (dateSpace.type) {
            case Calendar.YEAR:
                return dateSpace.space + "年前";

            case Calendar.MONTH:
                return dateSpace.space + "月前";

            case Calendar.DATE:
                if(dateSpace.space == 1) {
                    return "昨天";
                } else if(dateSpace.space == 2) {
                    return "前天";
                } else {
                    return dateSpace.space + "天前";
                }

            case Calendar.HOUR:
                return dateSpace.space + "小时前";

            case Calendar.MINUTE:
                return dateSpace.space + "分钟前";

            default:
                return "刚刚";
        }
    }

    private static DateSpace getMaxUnitSpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        DateSpace dateSpace = new DateSpace();

        dateSpace.space = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
        if(dateSpace.space > 0) {
            dateSpace.type = Calendar.YEAR;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        if(dateSpace.space > 0) {
            dateSpace.type = Calendar.MONTH;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.DATE) - from.get(Calendar.DATE);
        if(dateSpace.space > 0) {
            dateSpace.type = Calendar.DATE;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.HOUR) - from.get(Calendar.HOUR);
        if(dateSpace.space > 0) {
            dateSpace.type = Calendar.HOUR;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.MINUTE) - from.get(Calendar.MINUTE);
        if(dateSpace.space > 0) {
            dateSpace.type = Calendar.MINUTE;
            return dateSpace;
        }

        return dateSpace;
    }

    private static class DateSpace {
        int space;

        int type;
    }

    /**
     * 日期加减
     *
     * gc.add(1,-1)表示年份减一. gc.add(2,-1)表示月份减一. gc.add(3.-1)表示周减一. gc.add(5,-1)表示天减一.
     *
     * GregorianCalendar类的add(int field,int amount)方法表示年月日加减. field参数表示年,月.日等. amount参数表示要加减的数量.
     *
     * @return 返回时间戳
     */
    public static Long getCurrentDayAdd(int field, int amount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(field, amount);
        return gc.getTimeInMillis();
//        String DATE_FORMAT = "yyyy-MM-dd";
//        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
//        sdf.setTimeZone(TimeZone.getDefault());
//
//        return sdf.format(gc.getTime());
    }
}
