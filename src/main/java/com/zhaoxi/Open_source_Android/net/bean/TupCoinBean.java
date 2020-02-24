package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/9/17.
 */

public class TupCoinBean {
    private List<TupBean> list;

    public List<TupBean> getList() {
        return list;
    }

    public void setList(List<TupBean> list) {
        this.list = list;
    }

    public static class TupBean {
        private String contractAddress;
        private String tokenSymbol;

        public String getContractAddress() {
            return StrUtil.isNull(contractAddress) ? "" : contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getTokenSymbol() {
            return StrUtil.isNull(tokenSymbol) ? "" : tokenSymbol;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }
    }
}
