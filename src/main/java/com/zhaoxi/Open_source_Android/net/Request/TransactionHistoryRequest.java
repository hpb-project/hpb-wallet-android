package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;

import java.util.List;

/**
 * des://查询交易记录
 * Created by ztt on 2018/7/11.
 */

public class TransactionHistoryRequest extends BaseOkHttpRequest {
    private String mAddress;
    private int mPageNum;
    private String mUrl;
    private int mType;

    public TransactionHistoryRequest(String address, int pageNum,
                                     String url,int type) {
        this.mAddress = address;
        this.mPageNum = pageNum;
        this.mUrl = url;
        this.mType = type;
    }

    @Override
    protected String toRequestURL() {
        return mUrl;
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
        if(mType == 0){
            params.add("0");
        }
        params.add(String.valueOf(mPageNum));
    }
}
