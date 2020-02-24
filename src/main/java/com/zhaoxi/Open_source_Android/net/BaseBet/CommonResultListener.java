package com.zhaoxi.Open_source_Android.net.BaseBet;

import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

/**
 * des:网络请求共同方法
 * Created by ztt on 2018/6/4.
 */

public class CommonResultListener implements NetResultCallBack{
    private BaseActivity mActivity;
    public CommonResultListener(BaseActivity activity){
        mActivity = activity;
    }
    @Override
    public void onSuccess(JSONArray jsonArray) {

    }

    @Override
    public void onError(String error) {
        mActivity.dismissProgressDialog();
        DappApplication.getInstance().showToast(error);
    }
}
