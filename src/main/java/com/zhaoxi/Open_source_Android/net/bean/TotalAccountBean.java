package com.zhaoxi.Open_source_Android.net.bean;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/11
 * create time:18:00
 */
public class TotalAccountBean {


    /**
     * hpbBalance : 1032550692000000000
     * ptCnyValue : 2149151010328800000
     * ptUsdValue : 303982923724800000
     * list : [{"map":{},"id":"HPB","tokenSymbol":"HPB","tokenSymbolImageUrl":null,"tokenName":null,"contractCreater":"0x79bcd1c3e9a514ce83abf2b62f20c1fccedcb6f9","contractAddress":null,"tokenTotalSupply":"1032550692000000000","contractType":"HPB","transfersNum":null,"cnyPrice":"2.0814","usdPrice":"0.2944","cnyTotalValue":"2149151010328800000","usdTotalValue":"303982923724800000"},{"map":{},"id":"A04FBD076A725851600D5CF1C4F9D446","tokenSymbol":"RPT","tokenSymbolImageUrl":null,"tokenName":"RedPacketToken","contractCreater":"0x79bcd1c3e9a514ce83abf2b62f20c1fccedcb6f9","contractAddress":"0xba29fa851805767609c25b7a032101cef1c2258b","tokenTotalSupply":"1035","contractType":"HRC-721","transfersNum":null,"cnyPrice":null,"usdPrice":null,"cnyTotalValue":"0","usdTotalValue":"0"}]
     */

    private String hpbBalance;
    private String ptCnyValue;
    private String ptUsdValue;
    private List<AssetsInfo> list;

    public String getHpbBalance() {
        return hpbBalance;
    }

    public void setHpbBalance(String hpbBalance) {
        this.hpbBalance = hpbBalance;
    }

    public String getPtCnyValue() {
        return ptCnyValue;
    }

    public void setPtCnyValue(String ptCnyValue) {
        this.ptCnyValue = ptCnyValue;
    }

    public String getPtUsdValue() {
        return ptUsdValue;
    }

    public void setPtUsdValue(String ptUsdValue) {
        this.ptUsdValue = ptUsdValue;
    }

    public List<AssetsInfo> getList() {
        return list;
    }

    public void setList(List<AssetsInfo> list) {
        this.list = list;
    }

    public static class AssetsInfo {
        /**
         * map : {}
         * id : HPB
         * tokenSymbol : HPB
         * tokenSymbolImageUrl : null
         * tokenName : null
         * contractCreater : 0x79bcd1c3e9a514ce83abf2b62f20c1fccedcb6f9
         * contractAddress : null
         * tokenTotalSupply : 1032550692000000000
         * contractType : HPB
         * transfersNum : null
         * cnyPrice : 2.0814
         * usdPrice : 0.2944
         * cnyTotalValue : 2149151010328800000
         * usdTotalValue : 303982923724800000
         */

        private String id;
        private String tokenSymbol;
        private String tokenSymbolImageUrl;
        private String tokenName;
        private String contractCreater;
        private String contractAddress;
        private String tokenTotalSupply;
        private String contractType;
        private String transfersNum;
        private String cnyPrice;
        private String usdPrice;
        private String cnyTotalValue;
        private String usdTotalValue;
        private String balanceOfToken;
        private String decimals;

        public String getBalanceOfToken() {
            return balanceOfToken;
        }

        public void setBalanceOfToken(String balanceOfToken) {
            this.balanceOfToken = balanceOfToken;
        }

        public String getDecimals() {
            return decimals;
        }

        public void setDecimals(String decimals) {
            this.decimals = decimals;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTokenSymbol() {
            return tokenSymbol;
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
            return contractAddress;
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
            return contractType;
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

//        public static class MapBean {
//        }


        @Override
        public String toString() {
            return "AssetsInfo{" +
                    "id='" + id + '\'' +
                    ", tokenSymbol='" + tokenSymbol + '\'' +
                    ", tokenSymbolImageUrl='" + tokenSymbolImageUrl + '\'' +
                    ", tokenName='" + tokenName + '\'' +
                    ", contractCreater='" + contractCreater + '\'' +
                    ", contractAddress='" + contractAddress + '\'' +
                    ", tokenTotalSupply='" + tokenTotalSupply + '\'' +
                    ", contractType='" + contractType + '\'' +
                    ", transfersNum='" + transfersNum + '\'' +
                    ", cnyPrice='" + cnyPrice + '\'' +
                    ", usdPrice='" + usdPrice + '\'' +
                    ", cnyTotalValue='" + cnyTotalValue + '\'' +
                    ", usdTotalValue='" + usdTotalValue + '\'' +
                    '}';
        }
    }
}
