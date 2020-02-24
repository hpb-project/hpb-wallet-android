package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * create by fangz
 * create date:2019/9/17
 * create time:14:27
 */
@Entity
public class AssetsBean implements Serializable, Comparable<AssetsBean> {


    private static final long serialVersionUID = -1046557302515570483L;

    @Generated(hash = 901117572)
    public AssetsBean() {
    }


    public AssetsBean(String id, String tokenSymbol, String tokenSymbolImageUrl,
                      String tokenName, String contractCreater, String contractAddress, String tokenTotalSupply,
                      String contractType, String transfersNum, String cnyPrice, String usdPrice,
                      String cnyTotalValue, String usdTotalValue, boolean isSelected, String balanceOfToken, int decimals) {
        this.id = id;
        this.tokenSymbol = tokenSymbol;
        this.tokenSymbolImageUrl = tokenSymbolImageUrl;
        this.tokenName = tokenName;
        this.contractCreater = contractCreater;
        this.contractAddress = contractAddress;
        this.tokenTotalSupply = tokenTotalSupply;
        this.contractType = contractType;
        this.transfersNum = transfersNum;
        this.cnyPrice = cnyPrice;
        this.usdPrice = usdPrice;
        this.cnyTotalValue = cnyTotalValue;
        this.usdTotalValue = usdTotalValue;
        this.isSelected = isSelected;
        this.balanceOfToken = balanceOfToken;
        this.decimals = decimals;
    }


    @Generated(hash = 1763242865)
    public AssetsBean(Long ids, String id, String tokenSymbol, String tokenSymbolImageUrl, String tokenName,
                      String contractCreater, String contractAddress, String tokenTotalSupply, String contractType,
                      String transfersNum, String cnyPrice, String usdPrice, String cnyTotalValue, String usdTotalValue,
                      boolean isSelected, String balanceOfToken, int decimals) {
        this.ids = ids;
        this.id = id;
        this.tokenSymbol = tokenSymbol;
        this.tokenSymbolImageUrl = tokenSymbolImageUrl;
        this.tokenName = tokenName;
        this.contractCreater = contractCreater;
        this.contractAddress = contractAddress;
        this.tokenTotalSupply = tokenTotalSupply;
        this.contractType = contractType;
        this.transfersNum = transfersNum;
        this.cnyPrice = cnyPrice;
        this.usdPrice = usdPrice;
        this.cnyTotalValue = cnyTotalValue;
        this.usdTotalValue = usdTotalValue;
        this.isSelected = isSelected;
        this.balanceOfToken = balanceOfToken;
        this.decimals = decimals;
    }


    /**
     * id : HPB
     * tokenSymbol : HPB
     * tokenSymbolImageUrl : null
     * tokenName : null
     * contractCreater : 0xbf4bc4459152f289a9c9d32124b3b4a6fd54c334
     * contractAddress : null
     * tokenTotalSupply : 207398920666666667
     * contractType : HPB
     * transfersNum : null
     * cnyPrice : 2.0832
     * usdPrice : 0.2939
     * cnyTotalValue : 0.432053431532800001
     * usdTotalValue : 0.060954542783933333
     * decimals : 18
     */

    @Id(autoincrement = true)
    private Long ids;
    private String id;
    private String tokenSymbol = "";
    private String tokenSymbolImageUrl;
    private String tokenName = "";
    private String contractCreater;
    private String contractAddress;
    private String tokenTotalSupply;
    private String contractType;
    private String transfersNum;
    private String cnyPrice;
    private String usdPrice;
    private String cnyTotalValue;
    private String usdTotalValue;
    private boolean isSelected;
    private String balanceOfToken;
    private int decimals;


    public String getBalanceOfToken() {
        return balanceOfToken;
    }

    public void setBalanceOfToken(String balanceOfToken) {
        this.balanceOfToken = balanceOfToken;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Long getIds() {
        return ids;
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
        return tokenSymbolImageUrl;
    }

    public void setTokenSymbolImageUrl(String tokenSymbolImageUrl) {
        this.tokenSymbolImageUrl = tokenSymbolImageUrl;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getContractCreater() {
        return contractCreater;
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
        return tokenTotalSupply;
    }

    public void setTokenTotalSupply(String tokenTotalSupply) {
        this.tokenTotalSupply = tokenTotalSupply;
    }

    public String getContractType() {
        return StrUtil.isNull(contractType) ? "" : contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getTransfersNum() {
        return transfersNum;
    }

    public void setTransfersNum(String transfersNum) {
        this.transfersNum = transfersNum;
    }

    public String getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(String cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public String getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(String usdPrice) {
        this.usdPrice = usdPrice;
    }

    public String getCnyTotalValue() {
        return cnyTotalValue;
    }

    public void setCnyTotalValue(String cnyTotalValue) {
        this.cnyTotalValue = cnyTotalValue;
    }

    public String getUsdTotalValue() {
        return usdTotalValue;
    }

    public void setUsdTotalValue(String usdTotalValue) {
        this.usdTotalValue = usdTotalValue;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    @Override
    public int compareTo(AssetsBean o) {
        int res = String.CASE_INSENSITIVE_ORDER.compare(this.tokenSymbol, o.tokenSymbol);
        if (res == 0) {
            res = this.tokenSymbol.compareTo(o.tokenSymbol);
        }
        return res;
    }


    public boolean getIsSelected() {
        return this.isSelected;
    }


    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public void setIds(Long ids) {
        this.ids = ids;
    }


    @Override
    public String toString() {
        return "AssetsBean{" +
                "ids=" + ids +
                ", tokenSymbol='" + tokenSymbol + '\'' +
                ", isSelected= " + isSelected + '\'' +
                '}';
    }
}
