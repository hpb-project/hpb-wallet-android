package com.zhaoxi.Open_source_Android.net.BaseBet;

import android.content.Context;
import android.util.Log;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;
import com.zhaoxi.Open_source_Android.net.OkHttpClientManager;

/**
 * Created by 51973 on 2018/5/9.
 */

public abstract class BaseOkHttpGetRequest {

    protected abstract String toRequestURL();

    private String mRequestUrl;

    private void initRequest() {
        mRequestUrl = toGetUrl(toRequestURL());
    }

    public void doRequest(Context context, NetResultCallBack callBack) {
        initRequest();
        //判断网络是否连接
        if (!NetUtil.isNetworkAvalible(DappApplication.getInstance())) {
            callBack.onError(context.getResources().getString(R.string.exception_netword_error));
            return;
        }
        OkHttpClientManager.getInstance().httpGet(mRequestUrl, callBack);
    }


    private String toGetUrl(String url) {
        StringBuilder urlSB = new StringBuilder();
//        if (entity != null && !entity.isEmpty()) {
//            for (String key : entity.keySet()) {
//                if (urlSB.length() != 0) {
//                    urlSB.append("&");
//                }
//                Log.e("GsonRequest",
//                        "request" + JSON.toJSONString(entity.get(key)));
//                urlSB.append(key + "="
//                        + Uri.encode(JSON.toJSONString(entity.get(key))));
//            }
//        }
        if (urlSB == null || "".equals(urlSB.toString())) {
            return url;
        }
        Log.e("GsonRequest", "[request url]" + urlSB);
        return url + "?" + urlSB.toString();
    }
}
