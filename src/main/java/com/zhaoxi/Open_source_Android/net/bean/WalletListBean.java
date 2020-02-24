package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/9/16.
 */

public class WalletListBean {
    private String cnyTotalValue;
    private String usdTotalValue;
    private List<WalletBean> list;

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

    public List<WalletBean> getList() {
        return list;
    }

    public void setList(List<WalletBean> list) {
        this.list = list;
    }
}
