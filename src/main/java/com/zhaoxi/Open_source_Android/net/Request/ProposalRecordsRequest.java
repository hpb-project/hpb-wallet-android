package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * Created by ztt on 2018/7/11.
 */

public class ProposalRecordsRequest extends BaseOkHttpRequest {
    private String mAddress;

    public ProposalRecordsRequest(String address) {
        this.mAddress = address;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_ProposalPersonalRecords.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
    }
}
