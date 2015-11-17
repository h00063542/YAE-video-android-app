package com.yilos.nailstar.util;

import java.util.UUID;

/**
 * Created by sisilai on 15/11/17.
 */
public class UUIDUtil {
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
}
