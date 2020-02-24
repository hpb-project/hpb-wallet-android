package com.zhaoxi.Open_source_Android.web3.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/8/1.
 */

public class Utils {
    private Utils() {}

    @SuppressWarnings("unchecked")
    public static List<TypeReference<Type>> convert(List<TypeReference<?>> input) {
        List<TypeReference<Type>> result = new ArrayList<TypeReference<Type>>(input.size());

        for (TypeReference<?> typeReference:input) {
            result.add((TypeReference<Type>) typeReference);
        }

        return result;
    }
}
