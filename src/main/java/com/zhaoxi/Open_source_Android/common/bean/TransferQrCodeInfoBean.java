package com.zhaoxi.Open_source_Android.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * create by fangz
 * create date:2019/10/16
 * create time:14:22
 */
public class TransferQrCodeInfoBean {


    /**
     * coin :
     * cointype :
     * from :
     * to :
     * gaslimt :
     * gasprice :
     * nonce :
     * money :
     * data :
     * ABI :
     */

    private String coin;
    private String cointype;
    private String from;
    private String to;
    private String gaslimt;
    private String gasprice;
    private String nonce;
    private String money;
    private String data;
    private String ABI;
    private String contractAddress;


    public TransferQrCodeInfoBean() {
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getCointype() {
        return cointype;
    }

    public void setCointype(String cointype) {
        this.cointype = cointype;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JSONField(name = "ABI")
    public String getABI() {
        return ABI;
    }

    public void setABI(String ABI) {
        this.ABI = ABI;
    }
}
