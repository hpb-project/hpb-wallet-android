package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:刷新
 * Created by ztt on 2018/7/11.
 */

public class RedFalshRequest extends BaseOkHttpRequest {
    private String mRedId;

    public RedFalshRequest(String redId) {
        mRedId = redId;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_REFRESHRAD.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedId);
    }
}
