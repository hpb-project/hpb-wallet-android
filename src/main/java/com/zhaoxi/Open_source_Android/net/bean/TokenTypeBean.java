package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/16
 * create time:14:59
 */
public class TokenTypeBean {


    private List<TokenTypeInfo> list;

    public List<TokenTypeInfo> getList() {
        return list;
    }

    public void setList(List<TokenTypeInfo> list) {
        this.list = list;
    }

    public static class TokenTypeInfo {
        /**
         * contractAddress : 0xba29fa851805767609c25b7a032101cef1c2258b
         * symbol : RPT
         * type : HRC-721
         * balance : null
         * tokenNum : 329
         * decimals : null
         */

        private String contractAddress;
        private String symbol;
        private String type;
        private String balance;
        private String tokenNum;
        private int decimals;

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getTokenNum() {
            return StrUtil.isEmpty(tokenNum) ? "0" : tokenNum;
        }

        public void setTokenNum(String tokenNum) {
            this.tokenNum = tokenNum;
        }

        public int getDecimals() {
            return StrUtil.isEmpty(decimals + "") ? 0 : decimals;
        }

        public void setDecimals(int decimals) {
            this.decimals = decimals;
        }
    }
}
