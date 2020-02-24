package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * des:
 * Created by ztt on 2018/7/31.
 */

public class AdvertiseBean {
    private String name;//标题
    private String picUrl;
    private String skipType;//"2",跳转类型：1-内部，2-H5
    private String skipUrl;//"http://www.hpbscan.org/nodes",跳转地址
    private String nextPage;//null,跳转页面：1-红包，2-我的投票，3-转账
    private String state;//"2",状态：1-待上架，2-已上架，3-已下架，4-已结束

    public String getName() {
        return StrUtil.isNull(name) ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return StrUtil.isNull(picUrl) ? "" : picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSkipType() {
        return StrUtil.isNull(skipType) ? "" : skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }

    public String getSkipUrl() {
        return StrUtil.isNull(skipUrl) ? "" : skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    public String getNextPage() {
        return StrUtil.isNull(nextPage) ? "" : nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getState() {
        return StrUtil.isNull(state) ? "" : state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
