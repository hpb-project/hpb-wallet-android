package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:我的投票记录
 * Created by ztt on 2018/7/11.
 * 参数1：账户地址, 参数2：页码数
 */

public class VoteHistoryRequest extends BaseOkHttpRequest {
    private String mAddress;
    private int mPageNum;

    public VoteHistoryRequest(String address, int pageNum) {
        this.mAddress = address;
        this.mPageNum = pageNum;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteHistory.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
        params.add(String.valueOf(mPageNum));
    }
}
