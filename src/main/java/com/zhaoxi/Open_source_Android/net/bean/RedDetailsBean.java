package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * des:红包记录 基础类
 * Created by ztt on 2018/6/15.
 */

public class RedDetailsBean {
    private String isOver;//是否结束：0-未开始，1-进行中，2-结束
    private String type;//红包类型
    private String from;//发红包地址
    private int totalPacketNum;//红包总数量
    private int usedNum;//已领红包数量
    private String totalCoin;//总金额（不含旷工费）！18GWE
    private String title;//红包主题
    private String keyIsVaild;//钥匙是否有效：0-有效，1-无效
    private String tokenId;
    private String tokenValue;//本次可领取的红包金额（18GWEI）
    private String redStatus;
    private String drawStatus;//领取红包状态：1-成功，0-失败，5-确认中 (传领取地址时，返回该字段，否则不返回这个字段)

    private int pages;//页数
    private List<RedDetails> list;

    public String getTokenValue() {
        return StrUtil.isNull(tokenValue) ? "0" : tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getDrawStatus() {
        return StrUtil.isNull(drawStatus) ? "10" : drawStatus;
    }

    public void setDrawStatus(String drawStatus) {
        this.drawStatus = drawStatus;
    }

    public String getRedStatus() {
        return StrUtil.isNull(redStatus) ? "" : redStatus;
    }

    public void setRedStatus(String redStatus) {
        this.redStatus = redStatus;
    }

    public String getTokenId() {
        return StrUtil.isNull(tokenId) ? "" : tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getKeyIsVaild() {
        return StrUtil.isNull(keyIsVaild) ? "1" : keyIsVaild;
    }

    public void setKeyIsVaild(String keyIsVaild) {
        this.keyIsVaild = keyIsVaild;
    }

    public String getIsOver() {
        return StrUtil.isNull(isOver) ? "3" : isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getType() {
        return StrUtil.isNull(type) ? "1" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return StrUtil.isNull(from) ? "1" : from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getTotalPacketNum() {
        return totalPacketNum;
    }

    public void setTotalPacketNum(int totalPacketNum) {
        this.totalPacketNum = totalPacketNum;
    }

    public int getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RedDetails> getList() {
        return list;
    }

    public void setList(List<RedDetails> list) {
        this.list = list;
    }

    public static class RedDetails implements Serializable {
        private String redPacketNo;//红包编号
        private long tradeTime;//交易时间
        private String fromAddr;//发红包地址
        private String toAddr;//领红包地址
        private String coinValue;//金额18GWEI
        private String maxFlag;//“1”-手气最佳
        private long gmtCreate;

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getRedPacketNo() {
            return StrUtil.isNull(redPacketNo) ? "" : redPacketNo;
        }

        public void setRedPacketNo(String redPacketNo) {
            this.redPacketNo = redPacketNo;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getFromAddr() {
            return StrUtil.isNull(fromAddr) ? "" : fromAddr;
        }

        public void setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
        }

        public String getToAddr() {
            return StrUtil.isNull(toAddr) ? "" : toAddr;
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

        public String getMaxFlag() {
            return StrUtil.isNull(maxFlag) ? "2" : maxFlag;
        }

        public void setMaxFlag(String maxFlag) {
            this.maxFlag = maxFlag;
        }
    }
}
