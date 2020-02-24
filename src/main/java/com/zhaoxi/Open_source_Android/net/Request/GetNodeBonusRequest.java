package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;

import java.util.List;

/**
 * des：节点分红
 * Created by ztt on 2018/7/11.
 */

public class GetNodeBonusRequest extends BaseOkHttpRequest {
    private String mAddress;
    private String mUrl;
    public GetNodeBonusRequest(String address,String url) {
        mAddress = address;
        mUrl = url;
    }

    @Override
    protected String toRequestURL() {
        return mUrl;
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
    }
}
