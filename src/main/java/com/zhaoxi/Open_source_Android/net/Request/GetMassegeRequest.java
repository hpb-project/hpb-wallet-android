package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/8/2.
 */

public class GetMassegeRequest extends BaseOkHttpRequest {
    private String mDeviceId;
    private String mType;
    private int mPageNum;

    public GetMassegeRequest(String deviceId, int languageType, int pageNum) {
        this.mDeviceId = deviceId;
        this.mPageNum = pageNum;
        if(languageType == 1){
            mType = "0";
        }else{
            mType = "1";
        }
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_getMassgeData.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(mPageNum));
        params.add(mType);
        params.add(mDeviceId);
    }
}
