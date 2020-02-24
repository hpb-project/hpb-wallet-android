package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:红包详情
 * Created by ztt on 2018/7/11.
 */

public class GetRedDetailsRequest extends BaseOkHttpRequest {
    private String mRedType;
    private String mRedId;
    private String mRedAddress;
    private int mPage;

    public GetRedDetailsRequest(String redType, String redId, String redAddress,
                                int page) {
        mRedType = redType;
        mRedId = redId;
        mRedAddress = redAddress;
        mPage = page;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_RED_DETAIL.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedType);
        params.add(mRedId);
        params.add(mRedAddress);
        params.add(String.valueOf(mPage));
    }
}
