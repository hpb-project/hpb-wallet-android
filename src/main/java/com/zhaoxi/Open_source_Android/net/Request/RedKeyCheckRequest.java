package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:验证key
 * Created by ztt on 2018/7/11.
 */

public class RedKeyCheckRequest extends BaseOkHttpRequest {
    private String mRedId;
    private String mRedKey;

    public RedKeyCheckRequest(String redId,String redKey) {
        mRedId = redId;
        mRedKey = redKey;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_REDKEY_CHECK.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedId);
        params.add(mRedKey);
    }
}
