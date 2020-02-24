package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.net.BaseBet.BaseOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;

/**
 * des:查询投票排名
 * Created by ztt on 2018/7/11.
 * 参数1：搜索关键字word(中英文大小写数字),参数2：分类cate(-1:全部,0:公司;1:机构;2:个人);参数3: 页码数 >0
 */

public class VoteRankingRequest extends BaseOkHttpRequest {
    private String mKeyword;
    private String mCate;
    private int mPageNum;

    public VoteRankingRequest(String keyword,String cate, int pageNum) {
        this.mKeyword = keyword;
        this.mCate = cate;
        this.mPageNum = pageNum;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_VoteRanking.getContext();
    }

    @Override
    protected void toHttpRequestParams(List<String> params) {
        params.add(mKeyword);
        params.add(mCate);
        params.add(String.valueOf(mPageNum));
    }
}
