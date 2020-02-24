package com.zhaoxi.Open_source_Android.common.bean;

import android.graphics.Bitmap;

/**
 * des:
 * Created by ztt on 2018/11/12.
 */

public class ShareAddressBean {
    private String rid;
    private Bitmap bm;
    private Bitmap logo;
    private String address;
    private int showDes;
    private boolean isShare = false;

    private String mKxId;
    private String mTime;
    private String mContent;

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getKxId() {
        return mKxId;
    }

    public void setKxId(String mKxId) {
        this.mKxId = mKxId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getShowDes() {
        return showDes;
    }

    public void setShowDes(int showDes) {
        this.showDes = showDes;
    }
}
