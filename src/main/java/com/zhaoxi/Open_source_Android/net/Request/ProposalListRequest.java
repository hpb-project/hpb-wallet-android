package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:获取最新资讯 获取快讯消息 获取最新系统消息
 * Created by ztt on 2018/7/11.
 */

public class ProposalListRequest extends BaseOkHttpRequest {
    private int mPageNum;

    public ProposalListRequest(int pageNum) {
        this.mPageNum = pageNum;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_ProposalList.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(mPageNum));
    }
}
