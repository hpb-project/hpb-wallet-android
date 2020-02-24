package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:28
 */
public class TransactionListRequest extends BaseOkHttpRequest {

    private String accountAddress;//账户地址
    private String contractType;//合约类型
    private String contractAddress;//合约地址
    private String tokenSymbol;//代币简称
    private int page;//起始页码
    private String transType;//起始页码 0 所有 1 发送 2 接收

    public TransactionListRequest(String accountAddress, String contractType, String contractAddress, String tokenSymbol, int page,String transType) {
        this.accountAddress = accountAddress;
        this.contractType = contractType;
        this.contractAddress = contractAddress;
        this.tokenSymbol = tokenSymbol;
        this.page = page;
        this.transType = transType;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_transactionList.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(accountAddress);
        params.add(contractType);
        params.add(contractAddress);
        params.add(tokenSymbol);
        params.add(String.valueOf(page));
        params.add(transType);
    }
}
