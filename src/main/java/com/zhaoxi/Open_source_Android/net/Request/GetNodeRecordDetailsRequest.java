package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des：节点分红
 * Created by ztt on 2018/7/11.
 */

public class GetNodeRecordDetailsRequest extends BaseOkHttpRequest {
    private String mAddress;
    private String mTxtHas;
    private int mPage;
    public GetNodeRecordDetailsRequest(String txHas,String address, int page) {
        mAddress = address;
        mTxtHas = txHas;
        mPage = page;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_NodeRecordDetail.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mTxtHas);
        params.add(mAddress);
        params.add(String.valueOf(mPage));
    }
}
