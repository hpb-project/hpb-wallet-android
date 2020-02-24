package com.zhaoxi.Open_source_Android.web3.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * Created by 51973 on 2018/5/23.
 */

public class WalletBean {
    private String keyStore;
    private String address;
    private String walletBName;
    private String mnemonic;
    private int imgId;//随机生成相对应的钱包图片
    private String imgPath;//生成的头像图片地址
    private String prompt;//密码提示信息
    private String walletType;//判断钱包类型
    //不能保存在数据库中
    private String money;
    private String money_c;//人民币
    private String money_u;//美元
    private boolean isChooseWallet = false;//是否是默认钱包
    private boolean mRunning = false;//是否开启线程
    private boolean isEnable = false;//是否可以进行映射
    private int isClodWallet = 0;//是否是冷钱包 默认值为0：不是  1:是

    private String cnyTotalValue;
    private String usdTotalValue;

    public String getCnyTotalValue() {
        return StrUtil.isNull(cnyTotalValue) ? "0" : cnyTotalValue;
    }

    public void setCnyTotalValue(String cnyTotalValue) {
        this.cnyTotalValue = cnyTotalValue;
    }

    public String getUsdTotalValue() {
        return StrUtil.isNull(usdTotalValue) ? "0" : usdTotalValue;
    }

    public void setUsdTotalValue(String usdTotalValue) {
        this.usdTotalValue = usdTotalValue;
    }

    public String getMoney_c() {
        return StrUtil.isNull(money_c) ? "0" : money_c;
    }

    public void setMoney_c(String money_c) {
        this.money_c = money_c;
    }

    public String getMoney_u() {
        return StrUtil.isNull(money_u) ? "0" : money_u;
    }

    public void setMoney_u(String money_u) {
        this.money_u = money_u;
    }

    public int getIsClodWallet() {
        return isClodWallet;
    }

    public void setIsClodWallet(int isClodWallet) {
        this.isClodWallet = isClodWallet;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean ismRunning() {
        return mRunning;
    }

    public void setmRunning(boolean mRunning) {
        this.mRunning = mRunning;
    }

    public boolean isChooseWallet() {
        return isChooseWallet;
    }

    public void setChooseWallet(boolean chooseWallet) {
        isChooseWallet = chooseWallet;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWalletBName() {
        return walletBName;
    }

    public void setWalletBName(String walletBName) {
        this.walletBName = walletBName;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
}
