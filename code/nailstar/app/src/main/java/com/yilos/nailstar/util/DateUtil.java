package com.yilos.nailstar.util;

import java.text.SimpleDateFormat;

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
}
