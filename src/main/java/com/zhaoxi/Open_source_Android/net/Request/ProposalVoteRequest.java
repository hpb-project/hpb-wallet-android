package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:版本检查
 * Created by ztt on 2018/7/30.
 */

public class ProposalVoteRequest extends BaseOkHttpRequest {
    private String mIsseuseId;//题案编号
    private String mAddress;//投票人地址
    private int isSuprot;//选项：1-支持，0-不支持
    private String mPollNum;//投票人票数
    private String mSignData;//签名数据

    public ProposalVoteRequest(String isseuseId,String address,int type,String pollNum,String signData) {
        this.mIsseuseId = isseuseId;
        this.mAddress = address;
        this.isSuprot = type;
        this.mPollNum = pollNum;
        this.mSignData = signData;
    }
    @Override
    protected String toRequestURL() {
        return UrlContext.Url_ProposalVote.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mIsseuseId);
        params.add(mAddress);
        params.add(String.valueOf(isSuprot));
        params.add(mPollNum);
        params.add(mSignData);
    }
}
