package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class StockDetailRequest extends BaseOkHttpRequest {

    private int page;//起始页码
    private String contractAddress;//合约地址
    private String accountAddress;//账户地址

    public StockDetailRequest(int page, String contractAddress, String accountAddress) {
        this.page = page;
        this.contractAddress = contractAddress;
        this.accountAddress = accountAddress;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_stock.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(page));
        params.add(contractAddress);
        params.add(accountAddress);
    }
}
