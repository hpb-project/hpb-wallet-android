package com.zhaoxi.Open_source_Android.net.BaseBet;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by 51973 on 2018/5/9.
 */

public interface NetResultCallBack {
    /**
     * 网络请求成功
     * @param jsonArray
     */
    void onSuccess(JSONArray jsonArray);

    /**
     * 网络请求失败
     * @param error
     */
    void onError(String error);
}
