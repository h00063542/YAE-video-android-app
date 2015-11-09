package com.yilos.nailstar.util;

/**
 * Created by sisilai on 15/11/9.
 */
public class LevelUtil {

    //返回等级数
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
        return 0;
    }

    //返回相差下一等级的经验值
    public static int calcExpDiffer (int exp) {
        if (exp >= 0 && exp < 50) {
            return 50 - exp;
        } else if (exp >= 50 && exp < 100) {
            return 100 -exp;
        } else if (exp >= 100 && exp < 200) {
            return 200 - exp;
        } else if (exp >= 200 && exp < 1000) {
            return 1000 - exp;
        } else if (exp >= 1000) {
            return exp;
        }
        return 0;
    }

    //返回相邻等级的进度
    public static float calcNextExpDiffer (int exp,int max) {
        if (exp >= 0 && exp < 50) {
            return (float)exp/50 * max;
        } else if (exp >= 50 && exp < 100) {
            return (float)(exp - 50)/50 * max;
        } else if (exp >= 100 && exp < 200) {
            return (float)(exp - 100)/100 * max;
        } else if (exp >= 200 && exp < 1000) {
            return (float)(exp - 200)/800 * max;
        } else if (exp >= 1000) {
            return (float)(exp - 1000)/1000 * max;
        }
        return 0;
    }
}
