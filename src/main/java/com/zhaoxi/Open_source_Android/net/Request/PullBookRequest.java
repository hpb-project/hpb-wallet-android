package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * 获取云数据
 * Created by ztt on 2018/12/19.
 */

public class PullBookRequest extends BaseOkHttpRequest {
    private List<String> mAddressData;

    public PullBookRequest(List<String> address) {
        this.mAddressData = address;
    }


    @Override
    protected String toRequestURL() {
        return UrlContext.Url_BookPullAll.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.addAll(mAddressData);
    }
}
