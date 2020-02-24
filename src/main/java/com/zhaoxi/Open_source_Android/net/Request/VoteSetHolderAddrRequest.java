package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:2、修改持币地址/授权
 * Created by ztt on 2018/7/11.
 */

public class VoteSetHolderAddrRequest extends BaseOkHttpRequest {
    private String mSignHax;

    public VoteSetHolderAddrRequest(String signHax) {
        this.mSignHax = signHax;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteSetHolderAddr.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mSignHax);
    }
}
