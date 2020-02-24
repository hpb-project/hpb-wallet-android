package com.zhaoxi.Open_source_Android.common.bean;

import java.util.List;

/**
 * create by fangz
 * create date:2019/10/21
 * create time:11:11
 */
public class SyncAssetsBean {


    private List<ListDataBean> listData;
    private String from;
    private String updateDate;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public List<ListDataBean> getListData() {
        return listData;
    }

    public void setListData(List<ListDataBean> listData) {
        this.listData = listData;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public static class ListDataBean {
        /**
         * tokenSymbol :
         * contractType :
         * balanceOfToken :
         */

        private String tokenSymbol;
        private String contractType;
        private String balanceOfToken;

        public String getTokenSymbol() {
            return tokenSymbol;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        public String getBalanceOfToken() {
            return balanceOfToken;
        }

        public void setBalanceOfToken(String balanceOfToken) {
            this.balanceOfToken = balanceOfToken;
        }
    }
}
