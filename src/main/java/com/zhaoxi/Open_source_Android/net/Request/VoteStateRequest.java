package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;


public class VoteStateRequest extends BaseOkHttpRequest {

    public VoteStateRequest() {

    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteState.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {

    }
}
