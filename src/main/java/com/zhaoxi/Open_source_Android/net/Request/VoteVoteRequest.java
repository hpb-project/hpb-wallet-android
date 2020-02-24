package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:投票
 * Created by ztt on 2018/7/11.
 * 参数1：我的地址, 参数2：候选人地址,参数3：票数
 */

public class VoteVoteRequest extends BaseOkHttpRequest {
    private String msignHax;

    public VoteVoteRequest(String signHax) {
        this.msignHax = signHax;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteVote.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(msignHax);
    }
}
