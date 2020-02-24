package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * des:红包记录 基础类
 * Created by ztt on 2018/6/15.
 */

public class RedRecordBean {
    private int total;//红包总数
    private int pages;//页数

    private List<RedBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RedBean> getList() {
        return list;
    }

    public void setList(List<RedBean> list) {
        this.list = list;
    }

    public static class RedBean implements Serializable {
        private int id;
        private String redPacketNo;//红包ID
        private String isOver;//是否结束：0-未开始，1-进行中，2-结束
        private String type;//红包类型
        private String txHash;//
        private String status;//交易状态：1-成功，0-失败，2-确认中
        private String coinSymbol;//金额单位
        private String fromAddr;//发红包地址
        private String toAddr;//领取地址
        private int totalNum;//红包总数量
        private String redPacketType;//红包类型：1-普通，2-拼手气红包
        private String enterType;//红包入口：1-红包
        private String totalCoin;//总金额（不含旷工费）！18GWE
        private String coinValue;//领取金额
        private String title;//红包主题
        private long startTime;//交易时间
        private long tradeTime;//交易时间
        private int usedNum;//已领红包数量

        public String getToAddr() {
            return toAddr;
        }

        public void setToAddr(String toAddr) {
            this.toAddr = toAddr;
        }

        public String getCoinValue() {
            return StrUtil.isNull(coinValue) ? "0" : coinValue;
        }

        public void setCoinValue(String coinValue) {
            this.coinValue = coinValue;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public int getId() {
            return id;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRedPacketNo() {
            return StrUtil.isNull(redPacketNo) ? "" : redPacketNo;
        }

        public void setRedPacketNo(String redPacketNo) {
            this.redPacketNo = redPacketNo;
        }

        public String getIsOver() {
            return StrUtil.isNull(isOver) ? "1" : isOver;
        }

        public void setIsOver(String isOver) {
            this.isOver = isOver;
        }

        public String getTxHash() {
            return StrUtil.isNull(txHash) ? "" : txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public String getStatus() {
            return StrUtil.isNull(status) ? "" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCoinSymbol() {
            return StrUtil.isNull(coinSymbol) ? "" : coinSymbol;
        }

        public void setCoinSymbol(String coinSymbol) {
            this.coinSymbol = coinSymbol;
        }

        public String getFromAddr() {
            return StrUtil.isNull(fromAddr) ? "" : fromAddr;
        }

        public void setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public String getRedPacketType() {
            return StrUtil.isNull(redPacketType) ? "" : redPacketType;
        }

        public void setRedPacketType(String redPacketType) {
            this.redPacketType = redPacketType;
        }

        public String getEnterType() {
            return StrUtil.isNull(enterType) ? "" : enterType;
        }

        public void setEnterType(String enterType) {
            this.enterType = enterType;
        }

        public String getTotalCoin() {
            return StrUtil.isNull(totalCoin) ? "0" : totalCoin;
        }

        public void setTotalCoin(String totalCoin) {
            this.totalCoin = totalCoin;
        }

        public String getTitle() {
            return StrUtil.isNull(title) ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getUsedNum() {
            return usedNum;
        }

        public void setUsedNum(int usedNum) {
            this.usedNum = usedNum;
        }
    }
}
