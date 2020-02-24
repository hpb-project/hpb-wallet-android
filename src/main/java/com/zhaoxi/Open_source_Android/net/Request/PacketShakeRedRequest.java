package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpGetRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

public class PacketShakeRedRequest extends BaseOkHttpGetRequest {

    public PacketShakeRedRequest() {
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_PacketShakePacket.getContext();
    }
}
