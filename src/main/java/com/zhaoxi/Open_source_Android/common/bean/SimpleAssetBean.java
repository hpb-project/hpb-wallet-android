package com.zhaoxi.Open_source_Android.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * create by fangz
 * create date:2019/10/21
 * create time:13:28
 */
@Entity
public class SimpleAssetBean {
    @Id(autoincrement = true)
    private Long id;
    private String from;
    private String tokenSymbol;
    private String contractType;
    private String balanceOfToken;

    @Generated(hash = 31276948)
    public SimpleAssetBean(Long id, String from, String tokenSymbol,
            String contractType, String balanceOfToken) {
        this.id = id;
        this.from = from;
        this.tokenSymbol = tokenSymbol;
        this.contractType = contractType;
        this.balanceOfToken = balanceOfToken;
    }

    @Generated(hash = 1300247212)
    public SimpleAssetBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTokenSymbol() {
        return this.tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getContractType() {
        return this.contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getBalanceOfToken() {
        return this.balanceOfToken;
    }

    public void setBalanceOfToken(String balanceOfToken) {
        this.balanceOfToken = balanceOfToken;
    }


}
