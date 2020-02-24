package com.zhaoxi.Open_source_Android.net.Request;

import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.BasePairsOkHttpRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;

import java.util.List;
import java.util.Map;

/**
 * des:意见反馈
 * Created by ztt on 2018/7/11.
 */

public class PostFeekbackRequest extends BasePairsOkHttpRequest {
    private int mType;//意见类型 0-问题 1-意见 2-其它
    private String mTitle;//意见标题
    private String mContent;//意见内容
    private String mContact;//联系方式
    private List<String> mFiles;

    public PostFeekbackRequest(int type, String title, String content, String contact, List<String> files) {
        this.mType = type;
        this.mTitle = title;
        this.mContent = content;
        this.mContact = contact;
        this.mFiles = files;
    }

    @Override
    protected String toRequestURL() {
        return UrlContext.Url_suggestion.getContext();
    }

    @Override
    protected void toHttpRequestParams(Map<String, Object> params) {
        params.put("type",mType);
        params.put("title",mTitle);
        params.put("content",mContent);
        params.put("contact",mContact);
    }

    @Override
    protected List<String> toUploadFilePath() {
        if(!CollectionUtil.isCollectionEmpty(mFiles)){
            return mFiles;
        }
        return null;
    }

}
