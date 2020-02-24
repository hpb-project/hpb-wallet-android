package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class WalletTransferRequest extends BaseOkHttpRequest {

    private String accountAddress;//账户地址
    private String tokenType;// 币种类型 HPB、HRC-20、HRC-721


    public WalletTransferRequest(String accountAddress, String tokenType) {
        this.accountAddress = accountAddress;
        this.tokenType = tokenType;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_walletTransfer.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(accountAddress);
        params.add(tokenType);
    }
}
