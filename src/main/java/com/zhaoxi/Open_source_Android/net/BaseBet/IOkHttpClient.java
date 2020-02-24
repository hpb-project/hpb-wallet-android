package com.zhaoxi.Open_source_Android.net.BaseBet;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * okHttp接口类
 * Created by 51973 on 2018/5/9.
 */

public interface IOkHttpClient {
    /**
     * 发起get请求
     *
     * @param url 请求地址
     * @param callBack 回调函数
     */
    void httpGet(String url,NetResultCallBack callBack);

    /**
     * 发起post请求
     * json
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param callBack 回调函数
     */
    void httpPostJson(Context context,String url, List<String> params, NetResultCallBack callBack);

    /**
     * 发起post请求
     * 表单
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param files 文件
     * @param callBack 回调函数
     */
    void httpPostPairs(Context context,String url, Map<String,Object> params, List<String> files, NetResultCallBack callBack);
}
