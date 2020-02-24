package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:领取红包详情
 * Created by ztt on 2018/7/11.
 */

public class GetRedDesRequest extends BaseOkHttpRequest {
    private String mRedId;
    private String mRedKey;
    private int mPage;
    private String mGetAddress;

    public GetRedDesRequest(String redId, String redKey,
                            int page,String address) {
        mRedId = redId;
        mRedKey = redKey;
        mPage = page;
        mGetAddress = address;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_BEFOREDRAW_CHECK.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedId);
        params.add(mRedKey);
        params.add(String.valueOf(mPage));
        params.add(mGetAddress);
    }
}
