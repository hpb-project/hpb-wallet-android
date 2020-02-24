package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:交易
 * Created by ztt on 2018/6/19.
 */

public class MerchantCallBackRequest extends BaseOkHttpRequest {
    private String mHash;
    private String mOrderId;
    private String mBackUrl;

    public MerchantCallBackRequest(String hash,String backUrl,String orderId) {
        mHash = hash;
        mOrderId = backUrl;
        mBackUrl = orderId;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_CallBackMerchant.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mHash);
        params.add(StrUtil.isEmpty(mOrderId)?"":mOrderId);
        params.add(StrUtil.isEmpty(mBackUrl)?"":mBackUrl);
    }
}
