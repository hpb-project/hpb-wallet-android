package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * des:
 * Created by ztt on 2018/8/2.
 */

public class MapEthBean {
    private BigDecimal ethBalance;
    private String hpbToken;
    private String gasLimit;
    private String gasPrice;
    private BigInteger nonce;

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getGasLimit() {
        return StrUtil.isNull(gasLimit)?"0":gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasPrice() {
        return StrUtil.isNull(gasPrice)?"0":gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getEthBalance() {
        if(ethBalance == null){
            return new BigDecimal("0");
        }
        return ethBalance;
    }

    public void setEthBalance(BigDecimal ethBalance) {
        this.ethBalance = ethBalance;
    }

    public String getHpbToken() {
        return StrUtil.isNull(hpbToken)?"0":hpbToken;
    }

    public void setHpbToken(String hpbToken) {
        this.hpbToken = hpbToken;
    }
}
