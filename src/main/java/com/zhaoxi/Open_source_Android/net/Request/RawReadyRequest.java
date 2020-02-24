package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:广告
 * Created by ztt on 2018/7/11.
 */

public class RawReadyRequest extends BaseOkHttpRequest {
    private String mFromAddress;
    private String mRedMoney;
    private String mRedNum;
    private String mRedRukou;//红包入口：1-红包（钱包1.4），2-官方
    private String mRedType;//1-普通红包，2-拼手气红包
    private String mRedDes;

    public RawReadyRequest(String formAddress,String redMoney,String redNum,
                           String redRukou,String redType,String redDes) {
        mFromAddress = formAddress;
        mRedMoney = redMoney;
        mRedNum = redNum;
        mRedRukou = redRukou;
        mRedType = redType;
        mRedDes = redDes;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_RAWREADY.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mFromAddress);
        params.add(mRedMoney);
        params.add(mRedNum);
        params.add(mRedRukou);
        params.add(mRedType);
        params.add(mRedDes);
    }
}
