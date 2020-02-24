package com.zhaoxi.Open_source_Android.net.Request;

import android.content.Context;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/8/2.
 */

public class GetMsgLastIdRequest extends BaseOkHttpRequest {
    private String mDeviceId;
    private String mType;

    public GetMsgLastIdRequest(Context context,String deviceId,int languageType) {
        this.mDeviceId = deviceId;
        if(languageType == 1){
            mType = "0";
        }else{
            mType = "1";
        }
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_getMsgLast.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mDeviceId);
        params.add(mType);
    }
}
