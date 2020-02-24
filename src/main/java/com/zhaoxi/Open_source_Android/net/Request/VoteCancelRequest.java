package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;
public class VoteCancelRequest extends BaseOkHttpRequest {
    private String mCancelHax;
    private String mBlockHash;

    public VoteCancelRequest(String blockHash,String cancelHax) {
        this.mCancelHax = cancelHax;
        this.mBlockHash = blockHash;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteCancel.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mBlockHash);
        params.add(mCancelHax);
    }
}
