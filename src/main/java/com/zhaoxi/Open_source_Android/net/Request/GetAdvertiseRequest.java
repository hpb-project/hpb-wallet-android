package com.zhaoxi.Open_source_Android.net.Request;

import android.content.Context;

import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:广告
 * Created by ztt on 2018/7/11.
 */

public class GetAdvertiseRequest extends BaseOkHttpRequest {
    private String mType;
    public GetAdvertiseRequest(Context context) {
        int type = ChangeLanguageUtil.languageType(context);
        if(type == 1){
            mType = "1";
        }else{
            mType = "2";
        }
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_Advertise.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add("2");
        params.add("2");
        params.add(mType);
    }
}
