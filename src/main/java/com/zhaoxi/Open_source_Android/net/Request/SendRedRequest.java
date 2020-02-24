package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:发送红包
 * Created by ztt on 2018/7/11.
 */

public class SendRedRequest extends BaseOkHttpRequest {
    private String mRedId;
    private String mRedSign;

    public SendRedRequest(String redId, String redSign) {
        mRedId = redId;
        mRedSign = redSign;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_SENDRAW.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedId);
        params.add(mRedSign);
    }
}
