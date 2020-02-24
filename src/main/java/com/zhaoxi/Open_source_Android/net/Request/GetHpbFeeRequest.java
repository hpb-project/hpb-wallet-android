package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/8/2.
 */

public class GetHpbFeeRequest extends BaseOkHttpRequest {
    private String mAddress;

    public GetHpbFeeRequest(String address) {
        this.mAddress = address;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_getHpbFee.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
    }
}
