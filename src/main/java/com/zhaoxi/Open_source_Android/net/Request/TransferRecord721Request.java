package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class TransferRecord721Request extends BaseOkHttpRequest {

    private int page;//起始页码
    private String accountAddress;//账户地址

    public TransferRecord721Request(int page, String accountAddress) {
        this.page = page;
        this.accountAddress = accountAddress;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_transferRecord.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(page));
        params.add(accountAddress);
    }
}
