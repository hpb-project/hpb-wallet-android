package com.zhaoxi.Open_source_Android.net.Request;

import android.content.Context;

import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;

import java.util.List;

/**
 * des:获取最新资讯 获取快讯消息 获取最新系统消息
 * Created by ztt on 2018/7/11.
 */

public class GetNewsRequest extends BaseOkHttpRequest {
    private String mUrl;
    private int mPageNum;
    private String mType;

    public GetNewsRequest(Context context,String url, int pageNum) {
        this.mUrl = url;
        this.mPageNum = pageNum;
        int type = ChangeLanguageUtil.languageType(context);
        if(type == 1){
            mType = "0";
        }else{
            mType = "1";
        }
    }

    @Override
    protected String toRequestURL() {
        return mUrl;
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(String.valueOf(mPageNum));
        params.add(mType);
    }
}
