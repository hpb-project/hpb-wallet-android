package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:领取红包
 * Created by ztt on 2018/7/11.
 */

public class RedDrawRequest extends BaseOkHttpRequest {
    private String mRedId;
    private String mRedKey;
    private String mToAddress;
    private String mTokenId;

    public RedDrawRequest(String redId, String redKey,String toAddress,String tokenId) {
        mRedId = redId;
        mRedKey = redKey;
        mToAddress = toAddress;
        mTokenId = tokenId;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_RED_DRAW.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedId);
        params.add(mRedKey);
        params.add(mToAddress);
        params.add(mTokenId);
    }
}
