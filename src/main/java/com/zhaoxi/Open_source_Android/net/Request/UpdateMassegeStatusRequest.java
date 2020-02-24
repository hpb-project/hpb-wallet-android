package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/8/2.
 */

public class UpdateMassegeStatusRequest extends BaseOkHttpRequest {
    private String mDeviceId;
    private int mNotificationId;

    public UpdateMassegeStatusRequest(String deviceId, int notificationId) {
        this.mDeviceId = deviceId;
        this.mNotificationId = notificationId;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_updateMassege.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mDeviceId);
        params.add(String.valueOf(mNotificationId));
    }
}
