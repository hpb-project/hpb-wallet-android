package com.zhaoxi.Open_source_Android.common.bean;

import java.io.Serializable;

/**
 * des:
 * Created by ztt on 2019/2/15.
 */

public class DappsLoadinfBean implements Serializable {
    private String protocol;// 协议名，钱包用来区分不同协议，本协议为 HPBWallet
    private String version;// 协议版本信息，如1.0
    private String blockchain;// 公链标识（HPB）
    private String dappName;// dapp名字
    private String dappIcon; // dapp图标
    private String action;// 赋值为login
    private String uuID;// dapp server生成的，用于此次登录验证的唯一标识
    private String expired;// 二维码过期时间，unix时间戳
    private String loginMemo;// 登录备注信息，钱包⽤来展示，可选
    private boolean isSend;//是否需要HPBWallet发送签名到主⽹网(默认YES)，YES为需要，NO为不不需 要，可选
    private String to;// 收款⼈人的hpb账号，必须
    private String amount;// 转账数量量(1 HPB 就是1, 0.05 HPB就为0.05)，必须
    private String desc;// 交易易的说明信息，钱包在付款UI展示给⽤用户，最⻓长不不要超过128个字节， 可选
    private String orderId;//dapp 订单唯⼀一编号  必须
    private String notifyUrl;//dapp sever 通知地址 可选

    private String from;//发送者地址
    private String gas;//gasLimit
    private String gasPrice;//
    private String value;//金额
    private String data;//data

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setIsSend(boolean send) {
        isSend = send;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(String blockchain) {
        this.blockchain = blockchain;
    }

    public String getDappName() {
        return dappName;
    }

    public void setDappName(String dappName) {
        this.dappName = dappName;
    }

    public String getDappIcon() {
        return dappIcon;
    }

    public void setDappIcon(String dappIcon) {
        this.dappIcon = dappIcon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUuID() {
        return uuID;
    }

    public void setUuID(String uuID) {
        this.uuID = uuID;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getLoginMemo() {
        return loginMemo;
    }

    public void setLoginMemo(String loginMemo) {
        this.loginMemo = loginMemo;
    }
}
