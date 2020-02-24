package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:红包记录
 * Created by ztt on 2018/7/11.
 */

public class GetRedRecordsRequest extends BaseOkHttpRequest {
    private String mRedType;//类型：1-发红包，0-领取红包
    private String mRedAddress;//发(领)红包地址与参数1匹配
    private int mPage;//起始页数
    public GetRedRecordsRequest(String redType, String redAddress, int page) {
        mRedType = redType;
        mRedAddress = redAddress;
        mPage = page;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_RED_RECORDS.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mRedType);
        params.add(mRedAddress);
        params.add(String.valueOf(mPage));
    }
}
