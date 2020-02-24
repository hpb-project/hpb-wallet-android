package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class TokenMoreRequest extends BaseOkHttpRequest {

    private int page;//起始页码
    private String contractAddress;//合约地址
    private String transactionHash;//交易hash

    public TokenMoreRequest(int page,String transactionHash,String contractAddress) {
        this.page = page;
        this.transactionHash = transactionHash;
        this.contractAddress= contractAddress;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_tokenAllId.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(page));
        params.add(transactionHash);
        params.add(contractAddress);
    }
}
