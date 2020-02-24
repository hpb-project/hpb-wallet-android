package com.zhaoxi.Open_source_Android.net.Request;

import android.content.Context;

import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:版本检查
 * Created by ztt on 2018/7/30.
 */

public class CheckAPPVersionRequest extends BaseOkHttpRequest {
    private String mType;
    public CheckAPPVersionRequest(Context context) {
        int type = ChangeLanguageUtil.languageType(context);
        if(type == 1){
            mType = "0";
        }else{
            mType = "1";
        }
    }
    @Override
    protected String toRequestURL() {
        return UrlContext.Url_checkVersion.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add("0");
        params.add(mType);
    }
}
