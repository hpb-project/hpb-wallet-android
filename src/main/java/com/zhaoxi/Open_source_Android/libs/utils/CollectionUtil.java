package com.zhaoxi.Open_source_Android.libs.utils;

import java.util.Collection;

/**
 * Created by 51973 on 2018/5/9.
 */

public class CollectionUtil {

    public static boolean isCollectionEmpty(Collection<?> c) {
        return (c == null || c.isEmpty());
    }
}
