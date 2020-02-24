package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:批量获取账户余额
 * Created by ztt on 2018/8/2.
 */

public class GetListBalanceRequest extends BaseOkHttpRequest {
    private List<String> mListAddress;

    public GetListBalanceRequest(List<String> listAddress) {
        this.mListAddress = listAddress;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_listBalance.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.addAll(mListAddress);
    }
}
