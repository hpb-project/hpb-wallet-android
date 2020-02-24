package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * 提交全部绑定地址
 * Created by ztt on 2018/12/19.
 */

public class PushBookRequest extends BaseOkHttpRequest {
    private List<String> mAddressList;
    private String mBookdata;

    public PushBookRequest(List<String> address,String bookdata) {
        this.mAddressList = address;
        this.mBookdata = bookdata;
    }


    @Override
    protected String toRequestURL() {
        return UrlContext.Url_BookPushAll.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        String param = "";
        for (int i = 0; i < mAddressList.size(); i++) {
            param += mAddressList.get(i)+",";
        }
        param = param.substring(0,param.length()-1);

        params.add(param);
        params.add(mBookdata);
    }
}
