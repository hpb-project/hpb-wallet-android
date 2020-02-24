package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * 获取总资产
 * Created by ztt on 2018/6/19.
 */

public class PersonalInfoRequest extends BaseOkHttpRequest {
    private String mAddress;

    private String numForNode;

    public PersonalInfoRequest(String address) {
        this.mAddress = address;
    }

    public PersonalInfoRequest(String mAddress, String numForNode) {
        this.mAddress = mAddress;
        this.numForNode = numForNode;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VotePersonalInfo.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mAddress);
        params.add(numForNode);
    }
}
