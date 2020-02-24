package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * des:
 * Created by ztt on 2018/7/31.
 */

public class VersionBean {
    private String verNo;//版本号
    private String downloadUrl;//下载地址
    private String isForceFlag;//是否强制更新 0:不 1:强制
    private String verContent;//版本更新内容

    public String getVerNo() {
        return StrUtil.isNull(verNo) ? "0" : verNo;
    }

    public void setVerNo(String verNo) {
        this.verNo = verNo;
    }

    public String getDownloadUrl() {
        return StrUtil.isNull(downloadUrl) ? "" : downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getIsForceFlag() {
        return StrUtil.isNull(isForceFlag) ? "" : isForceFlag;
    }

    public void setIsForceFlag(String isForceFlag) {
        this.isForceFlag = isForceFlag;
    }

    public String getVerContent() {
        return StrUtil.isNull(verContent) ? "" : verContent;
    }

    public void setVerContent(String verContent) {
        this.verContent = verContent;
    }
}
