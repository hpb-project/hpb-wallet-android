package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

public class VoteTransationReceiptRequest extends BaseOkHttpRequest {
    private String mTransationHax;

    public VoteTransationReceiptRequest(String transationHax) {
        this.mTransationHax = transationHax;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.URL_VoteTrabsationReceipt.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mTransationHax);
    }
}
