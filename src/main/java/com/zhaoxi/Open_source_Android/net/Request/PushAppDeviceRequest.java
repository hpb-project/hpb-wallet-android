package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * 提交全部绑定地址
 * Created by ztt on 2018/12/19.
 */

public class PushAppDeviceRequest extends BaseOkHttpRequest {
    private String mData;

    public PushAppDeviceRequest(String data) {
        this.mData = data;
    }


    @Override
    protected String toRequestURL() {
        return UrlContext.Url_PostDevice.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mData);
    }
}
