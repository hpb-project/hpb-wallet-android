package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class TokenIdDetailRequest extends BaseOkHttpRequest {

    private int page;//起始页码
    private String tokenId;//代币ID
    private String contractAddress;//代币ID

    public TokenIdDetailRequest(int page, String tokenId,String contractAddress) {
        this.page = page;
        this.tokenId = tokenId;
        this.contractAddress = contractAddress;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_tokenIdDetail.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(page));
        params.add(tokenId);
        params.add(contractAddress);
    }
}
