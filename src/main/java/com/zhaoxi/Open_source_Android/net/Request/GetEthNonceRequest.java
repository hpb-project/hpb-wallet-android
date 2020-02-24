package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:查询nonce
 * Created by ztt on 2018/6/19.
 */

public class GetEthNonceRequest extends BaseOkHttpRequest {
    private String mAddress;

    public GetEthNonceRequest(String address) {
        this.mAddress = address;
    }


    @Override
    protected String toRequestURL() {
        return UrlContext.Url_transferEthNonce.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
    }
}
