package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:查询候选人信息
 * Created by ztt on 2018/7/11.
 * 参数1：我的地址, 参数2：候选人地址,参数3：票数
 */

public class VoteVoteInfoRequest extends BaseOkHttpRequest {
    private String mHouxuanrenId;
    private String mAddress;

    public VoteVoteInfoRequest(String houxuanrenId,String address) {
        this.mHouxuanrenId = houxuanrenId;
        this.mAddress = address;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteVoteInfo.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mHouxuanrenId);
        params.add(mAddress);
    }
}
