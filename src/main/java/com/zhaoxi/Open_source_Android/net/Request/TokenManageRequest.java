package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class TokenManageRequest extends BaseOkHttpRequest {

    private String currentAddress;//当前地址

    public TokenManageRequest(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_tokenManage.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(currentAddress);
    }
}
