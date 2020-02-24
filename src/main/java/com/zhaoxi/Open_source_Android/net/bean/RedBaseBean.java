package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/7/15.
 */

public class RedBaseBean {
    private String isOver;//摇一摇红包是否结束：1-进行中，2-结束
    private String redPacketNo;//红包编号：中奖才返回该字段
    private String key;//钥匙：中奖才返回该字段
    private String contractAddr;//发红包合约地址
    private List<String> valuesArr;//金额数组(金额为18GWEI)
    private String packetNo;//红包编号
    private String proxyAddr;//

    public String getProxyAddr() {
        return StrUtil.isNull(proxyAddr) ? "" : proxyAddr;
    }

    public void setProxyAddr(String proxyAddr) {
        this.proxyAddr = proxyAddr;
    }

    public String getContractAddr() {
        return StrUtil.isNull(contractAddr) ? "" : contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getRedPacketNo() {
        return StrUtil.isNull(redPacketNo) ? "" : redPacketNo;
    }

    public void setRedPacketNo(String redPacketNo) {
        this.redPacketNo = redPacketNo;
    }

    public String getKey() {
        return StrUtil.isNull(key) ? "" : key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValuesArr() {
        return valuesArr;
    }

    public void setValuesArr(List<String> valuesArr) {
        this.valuesArr = valuesArr;
    }

    public String getPacketNo() {
        return StrUtil.isNull(packetNo) ? "" : packetNo;
    }

    public void setPacketNo(String packetNo) {
        this.packetNo = packetNo;
    }
}
