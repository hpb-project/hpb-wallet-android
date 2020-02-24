package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class Token20RecordRequest extends BaseOkHttpRequest {

    private int page;//起始页码
    private String contractAddress;//合约地址
    private String accountAddress;//账户地址
    private String tokenSymbol;//代币Symbol

    public Token20RecordRequest(int page, String contractAddress, String accountAddress, String tokenSymbol) {
        this.page = page;
        this.contractAddress = contractAddress;
        this.accountAddress = accountAddress;
        this.tokenSymbol = tokenSymbol;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_token20Record.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(page));
        params.add(contractAddress);
        params.add(accountAddress);
        params.add(tokenSymbol);
    }
}
