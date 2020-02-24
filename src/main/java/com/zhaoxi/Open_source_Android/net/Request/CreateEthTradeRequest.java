package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:交易
 * Created by ztt on 2018/6/19.
 */

public class CreateEthTradeRequest extends BaseOkHttpRequest {
    private String mHashCode;

    public CreateEthTradeRequest(String hashcode) {
        mHashCode = hashcode;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_sendTransfer.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mHashCode);
    }
}
