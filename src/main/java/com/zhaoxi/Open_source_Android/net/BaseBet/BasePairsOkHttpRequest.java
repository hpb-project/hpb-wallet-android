package com.zhaoxi.Open_source_Android.net.BaseBet;

import android.content.Context;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;
import com.zhaoxi.Open_source_Android.net.OkHttpClientManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 51973 on 2018/5/9.
 */

public abstract class BasePairsOkHttpRequest {
    protected abstract String toRequestURL();
    protected abstract void toHttpRequestParams(Map<String,Object> params);
    protected abstract List<String> toUploadFilePath();

    private String mRequestUrl;
    private Map<String,Object> mMapParams;
    private List<String> mUploadFilesPath;

    private void ininPairsRequest(){
        mRequestUrl = toRequestURL();
        mMapParams = getHttpRequestMapParams();
        mUploadFilesPath = toUploadFilePath();
    }

    private Map<String,Object> getHttpRequestMapParams(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appSource", "android");
        toHttpRequestParams(params);
        return params;
    }

    public void doRequestPairs(Context context,NetResultCallBack callBack){
        ininPairsRequest();
        //判断网络是否连接
        if(!NetUtil.isNetworkAvalible(DappApplication.getInstance())){
            callBack.onError(context.getResources().getString(R.string.exception_netword_error));
            return;
        }
        OkHttpClientManager.getInstance().httpPostPairs(context,mRequestUrl, mMapParams,mUploadFilesPath, callBack);
    }
}
