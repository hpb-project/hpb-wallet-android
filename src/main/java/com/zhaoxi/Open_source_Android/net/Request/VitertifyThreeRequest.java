package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:商户收款白名单校验
 * Created by ztt on 2019/2/18.
 */

public class VitertifyThreeRequest extends BaseOkHttpRequest {
    private String account;

    public VitertifyThreeRequest(String account) {
        this.account = account;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VitertifyMerchant.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(account);
    }
}
