package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * Created by ztt on 2018/8/2.
 */

public class GetTokenTypelistRequest extends BaseOkHttpRequest {
    private String mAddress;
    private String mBiz;

    public GetTokenTypelistRequest(String address,String biz) {
        this.mAddress = address;
        this.mBiz = biz;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_tokenTypelist.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
        params.add(mBiz);
    }
}
