package com.yilos.nailstar.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 检查字符串是否为空
     *
     * @param param
     * @return true, 表示字符串为null或""
     */
    public static boolean isEmpty(String param) {
        return null == param || param.isEmpty();
    }

    /**
     * 检查手机号码是否合法
     *
     * @param mobileNumber 要检查的手机号码
     * @return true合法，false不合法
     */
    public static boolean isMobileNumber(String mobileNumber) {
        Pattern p = Pattern.compile("^((13)|(14)|(15)|(17)|(18))\\d{9}$");
        Matcher m = p.matcher(mobileNumber);
        return m.matches();
    }


    public static ArrayList<String> findNumber(String str) {
        ArrayList<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile("\\d{7,}");
        Matcher m = p.matcher(str);

        while (m.find()) {
            if (!isEmpty(m.group())) {
                result.add(m.group());
            }
        }
        return result;
    }

    /**
     * 检查是否为http://
     *
     * @param url 要检查的手机号码
     */
    public static boolean isHttpUrl(String url) {
        return !isEmpty(url) && url.startsWith("http://");
    }

}
