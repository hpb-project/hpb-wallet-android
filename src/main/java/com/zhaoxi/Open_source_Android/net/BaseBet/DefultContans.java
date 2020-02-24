package com.zhaoxi.Open_source_Android.net.BaseBet;

import okhttp3.MediaType;

/**
 * Created by 51973 on 2018/5/9.
 */

public class DefultContans {
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static final int NET_ERROR = 0;
    public static final int NET_SUCCESE = 1;
    public static final String NET_TYPE_KEY = "type";
    public static final String NET_ERROR_KEY = "error";
    public static final String NET_CALLBACK_KEY = "key";

    public static final int HANDLER_MSG_WHAT = 1001;
}
