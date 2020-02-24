package com.zhaoxi.Open_source_Android.net.BaseBet;

import android.content.Context;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.net.OkHttpClientManager;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 51973 on 2018/5/9.
 */

public abstract class BaseOkHttpRequest {
    public static final int METHOD_POST = 1;

    protected abstract String toRequestURL();

    protected abstract void toHttpRequestParams(List<String> params);

    private String mRequestUrl;
    private List<String> mContentParams;

    private void initRequest() {
        mRequestUrl = toRequestURL();
        mContentParams = getHttpRequestContentParams();
    }

    private List<String> getHttpRequestContentParams() {
        List<String> params = new ArrayList<>();
//        params.add("android");
        toHttpRequestParams(params);
        return params;
    }

    public void doRequest(Context context,NetResultCallBack callBack) {
        initRequest();
        //判断网络是否连接
        if(!NetUtil.isNetworkAvalible(DappApplication.getInstance())){
            callBack.onError(context.getResources().getString(R.string.exception_netword_error));
            return;
        }
        OkHttpClientManager.getInstance().httpPostJson(context,mRequestUrl, mContentParams, callBack);
    }
}
