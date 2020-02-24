package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * 获取总资产
 * Created by ztt on 2018/6/19.
 */

public class WalletBalanceRequest extends BaseOkHttpRequest {
    private String mAddress;

    public WalletBalanceRequest(String address) {
        this.mAddress = address;
    }


    @Override
    protected String toRequestURL() {
        return UrlContext.Url_walletBalance.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
    }
}
