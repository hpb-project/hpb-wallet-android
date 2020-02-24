package com.zhaoxi.Open_source_Android.common.bean;

/**
 * @author ztt
 * @des
 * @date 2019/11/4.
 */
public class VoteSignBean {
    /* 授权签名 */
    private String from;
    private String contractAddress;
    private String content;//
    private String gaslimt;
    private String gasprice;
    private String nonce;
    private String data;
    private String pollNum;
    private String suportStatus;//0 撤销 1投票
    private String money;//

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGaslimt() {
        return gaslimt;
    }

    public void setGaslimt(String gaslimt) {
        this.gaslimt = gaslimt;
    }

    public String getGasprice() {
        return gasprice;
    }

    public void setGasprice(String gasprice) {
        this.gasprice = gasprice;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPollNum() {
        return pollNum;
    }

    public void setPollNum(String pollNum) {
        this.pollNum = pollNum;
    }

    public String getSuportStatus() {
        return suportStatus;
    }

    public void setSuportStatus(String suportStatus) {
        this.suportStatus = suportStatus;
    }
}
