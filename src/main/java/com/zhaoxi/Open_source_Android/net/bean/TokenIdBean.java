package com.zhaoxi.Open_source_Android.net.bean;

/**
 * create by fangz
 * create date:2019/9/6
 * create time:11:01
 */
public class TokenIdBean {

    private String headPath;
    private String tokenId;
    private String tokenTransferCount;
    private boolean isCheck;

    public TokenIdBean(String headPath, String tokenId, String tokenTransferCount) {
        this.headPath = headPath;
        this.tokenId = tokenId;
        this.tokenTransferCount = tokenTransferCount;
    }

    public TokenIdBean(String headPath, String tokenId, String tokenTransferCount, boolean isCheck) {
        this.headPath = headPath;
        this.tokenId = tokenId;
        this.tokenTransferCount = tokenTransferCount;
        this.isCheck = isCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenTransferCount() {
        return tokenTransferCount;
    }

    public void setTokenTransferCount(String tokenTransferCount) {
        this.tokenTransferCount = tokenTransferCount;
    }

    @Override
    public String toString() {
        return "TokenIdBean{" +
                "headPath='" + headPath + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", tokenTransferCount='" + tokenTransferCount + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
