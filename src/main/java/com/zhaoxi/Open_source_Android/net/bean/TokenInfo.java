package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;

/**
 * create by fangz
 * create date:2019/9/11
 * create time:13:30
 */

public class TokenInfo implements Serializable, Comparable<TokenInfo> {
    private static final long serialVersionUID = -7768434864426277809L;

    public TokenInfo(){

    }

    /**
     * map : {}
     * id : A04FBD076A725851600D5CF1C4F9D446
     * tokenSymbol : RPT
     * tokenSymbolImageUrl : null
     * tokenName : RedPacketToken
     * decimals : 0
     * deployTxHash : 0x9e099a673550c76ded94286bcf95e956d543c608c3a9b9023b6bb46cc5c70bdc
     * contractCreater : 0x79bcd1c3e9a514ce83abf2b62f20c1fccedcb6f9
     * contractAddress : 0xba29fa851805767609c25b7a032101cef1c2258b
     * tokenTotalSupply : 1035
     * contractType : HRC-721
     * verifiedStatus : null
     * price : null
     * changeRate : null
     * volume24h : null
     * marketCap : null
     * holders : null
     * transfersNum : null
     * status : 1
     * createTimestamp : 1566057676000
     * updateTimestamp : 1566057676000
     * cnyPrice : 0
     * usdPrice : 0
     * cnyTotalValue : 0
     * usdTotalValue : 0
     */

    private String id;
    private String tokenSymbol;
    private String tokenSymbolImageUrl;
    private String tokenName;
    private int decimals;
    private String deployTxHash;
    private String contractCreater;
    private String contractAddress;
    private String tokenTotalSupplys;
    private String contractType;
    private String verifiedStatus;
    private String price;
    private String changeRate;
    private String volume24h;
    private String marketCap;
    private String holders;
    private String transfersNum;
    private String status;
    private long createTimestamp;
    private long updateTimestamp;
    private String cnyPrice;
    private String usdPrice;
    private String cnyTotalValue;
    private String usdTotalValue;
    private String balanceOfToken;

    public String getBalanceOfToken() {
        return balanceOfToken;
    }

    public void setBalanceOfToken(String balanceOfToken) {
        this.balanceOfToken = balanceOfToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenSymbol() {
        return StrUtil.isNull(tokenSymbol) ? "" : tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenSymbolImageUrl() {
        return StrUtil.isNull(tokenSymbolImageUrl) ? "" : tokenSymbolImageUrl;
    }

    public void setTokenSymbolImageUrl(String tokenSymbolImageUrl) {
        this.tokenSymbolImageUrl = tokenSymbolImageUrl;
    }

    public String getTokenName() {
        return StrUtil.isNull(tokenName) ? "" : tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getDeployTxHash() {
        return StrUtil.isNull(deployTxHash) ? "" : deployTxHash;
    }

    public void setDeployTxHash(String deployTxHash) {
        this.deployTxHash = deployTxHash;
    }

    public String getContractCreater() {
        return StrUtil.isNull(contractCreater) ? "" : contractCreater;
    }

    public void setContractCreater(String contractCreater) {
        this.contractCreater = contractCreater;
    }

    public String getContractAddress() {
        return StrUtil.isNull(contractAddress) ? "" : contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getTokenTotalSupply() {
        return tokenTotalSupplys;
    }

    public void setTokenTotalSupply(String tokenTotalSupplys) {
        this.tokenTotalSupplys = tokenTotalSupplys;
    }

    public String getContractType() {
        return StrUtil.isNull(contractType) ? "" : contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getVerifiedStatus() {
        return StrUtil.isNull(verifiedStatus) ? "" : verifiedStatus;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getPrice() {
        return StrUtil.isNull(price) ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChangeRate() {
        return StrUtil.isNull(changeRate) ? "" : changeRate;
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public String getVolume24h() {
        return StrUtil.isNull(volume24h) ? "" : volume24h;
    }

    public void setVolume24h(String volume24h) {
        this.volume24h = volume24h;
    }

    public String getMarketCap() {
        return StrUtil.isNull(marketCap) ? "" : marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getHolders() {
        return StrUtil.isNull(holders) ? "" : holders;
    }

    public void setHolders(String holders) {
        this.holders = holders;
    }

    public String getTransfersNum() {
        return StrUtil.isNull(transfersNum) ? "" : transfersNum;
    }

    public void setTransfersNum(String transfersNum) {
        this.transfersNum = transfersNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(String cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public String getUsdPrice() {
        return StrUtil.isNull(usdPrice) ? "" : usdPrice;
    }

    public void setUsdPrice(String usdPrice) {
        this.usdPrice = usdPrice;
    }

    public String getCnyTotalValue() {
        return StrUtil.isNull(cnyTotalValue) ? "" : cnyTotalValue;
    }

    public void setCnyTotalValue(String cnyTotalValue) {
        this.cnyTotalValue = cnyTotalValue;
    }

    public String getUsdTotalValue() {
        return StrUtil.isNull(usdTotalValue) ? "" : usdTotalValue;
    }

    public void setUsdTotalValue(String usdTotalValue) {
        this.usdTotalValue = usdTotalValue;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                ", id='" + id + '\'' +
                ", tokenSymbol='" + tokenSymbol + '\'' +
                ", tokenSymbolImageUrl='" + tokenSymbolImageUrl + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", decimals=" + decimals +
                ", deployTxHash='" + deployTxHash + '\'' +
                ", contractCreater='" + contractCreater + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", tokenTotalSupplys='" + tokenTotalSupplys + '\'' +
                ", contractType='" + contractType + '\'' +
                ", verifiedStatus='" + verifiedStatus + '\'' +
                ", price='" + price + '\'' +
                ", changeRate='" + changeRate + '\'' +
                ", volume24h='" + volume24h + '\'' +
                ", marketCap='" + marketCap + '\'' +
                ", holders='" + holders + '\'' +
                ", transfersNum='" + transfersNum + '\'' +
                ", status='" + status + '\'' +
                ", createTimestamp=" + createTimestamp +
                ", updateTimestamp=" + updateTimestamp +
                ", cnyPrice='" + cnyPrice + '\'' +
                ", usdPrice='" + usdPrice + '\'' +
                ", cnyTotalValue='" + cnyTotalValue + '\'' +
                ", usdTotalValue='" + usdTotalValue + '\'' +
                '}';
    }

    @Override
    public int compareTo(TokenInfo o) {
        int res = String.CASE_INSENSITIVE_ORDER.compare(this.tokenName, o.tokenName);
        if (res == 0) {
            res = this.tokenName.compareTo(o.tokenName);
        }
        return res;
    }

    public String getTokenTotalSupplys() {
        return this.tokenTotalSupplys;
    }

    public void setTokenTotalSupplys(String tokenTotalSupplys) {
        this.tokenTotalSupplys = tokenTotalSupplys;
    }

}