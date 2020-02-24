package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:商户交易信息记录
 * Created by ztt on 2019/2/18.
 */

public class LogtxinfoRequest extends BaseOkHttpRequest {
    private String fromAddressNonce;
    private String txDetail;

    public LogtxinfoRequest(String fromAddressNonce, String txDetail) {
        this.fromAddressNonce = fromAddressNonce;
        this.txDetail = txDetail;
    }


    @Override
    protected String toRequestURL() {
        return UrlContext.Url_LogtxinfoMerchant.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(fromAddressNonce);
        params.add(txDetail);
    }
}
