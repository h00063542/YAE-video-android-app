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

    public static String buildTelNumberHtmlText(String text) {
        String commentContent = StringUtil.isEmpty(text) ? Constants.EMPTY_STRING : text;
        ArrayList<String> phoneNumbers = findNumber(commentContent);
        if (CollectionUtil.isEmpty(phoneNumbers)) {
            return commentContent;
        } else {
            StringBuilder stringBuilder = new StringBuilder(text);
            int index;
            String number = null;
            for (int i = 0, size = phoneNumbers.size(); i < size; i++) {
                number = phoneNumbers.get(i);
                index = commentContent.indexOf(number);
                stringBuilder.replace(index, number.length() + index, "<a color=\"#529E84\" href=\"tel:" + number + "\">" + number + "</a>");
            }
            return stringBuilder.toString();
        }
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
}
