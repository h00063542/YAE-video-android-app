package com.yilos.nailstar.util;

import java.util.Collection;

/**
 * Created by yangdan on 15/10/23.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection collection){
        return collection == null || collection.isEmpty();
    }
}
