package com.yilos.nailstar.util;

/**
 * Created by sisilai on 15/11/9.
 */
public class LevelUtil {
    public static int calcLevel (int exp) {
        if (exp >= 0 && exp < 50) {
            return 1;
        } else if (exp >= 50 && exp < 100) {
            return 2;
        } else if (exp >= 100 && exp < 200) {
            return 3;
        } else if (exp >= 200 && exp < 1000) {
            return 4;
        } else if (exp >= 1000) {
            return 5;
        }
        return 1;
    }
}
