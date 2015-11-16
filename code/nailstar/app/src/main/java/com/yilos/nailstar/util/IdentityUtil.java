package com.yilos.nailstar.util;

/**
 * Created by sisilai on 15/11/16.
 */
public class IdentityUtil {

    public static String getIdentity( int type) {
        String identity;
        //        1美甲店主
        //        2美甲师
        //        3美甲从业者
        //        4美甲消费者
        //        5美甲老师
        //        6其他
        switch (type) {
            case 1:
                identity = "美甲店主";
                break;
            case 2:
                identity = "美甲师";
                break;
            case 3:
                identity = "美甲从业者";
                break;
            case 4:
                identity = "美甲消费者";
                break;
            case 5:
                identity = "美甲老师";
                break;
            case 6:
                identity = "其他";
                break;
            default:
                identity = "身份";
                break;
        }
        return identity;
    }
}
