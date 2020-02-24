package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/8/2.
 */

public class VoteBean {
    private int pages;
    private List<VoteInfo> list;
    private String bonusIsOpen;//添加分红比例是否展示，”1”-展示，0-不展示
    private MyhistoryVote extention;

    public String getBonusIsOpen() {
        return StrUtil.isNull(bonusIsOpen) ? "0" : bonusIsOpen;
    }

    public void setBonusIsOpen(String bonusIsOpen) {
        this.bonusIsOpen = bonusIsOpen;
    }

    public MyhistoryVote getExtention() {
        return extention;
    }

    public void setExtention(MyhistoryVote extention) {
        this.extention = extention;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<VoteInfo> getList() {
        return list;
    }

    public void setList(List<VoteInfo> list) {
        this.list = list;
    }

    public static class MyhistoryVote {
        private String balance;//剩余票数
        private String aviliable;//可投票数
        private BigDecimal totalVoteNum;//已投票数
        private String currentBlockNumber;//当前区块号

        public String getCurrentBlockNumber() {
            return StrUtil.isNull(currentBlockNumber) ? "0" : currentBlockNumber;
        }

        public void setCurrentBlockNumber(String currentBlockNumber) {
            this.currentBlockNumber = currentBlockNumber;
        }

        public String getAviliable() {
            return StrUtil.isNull(aviliable) ? "0" : aviliable;
        }

        public void setAviliable(String aviliable) {
            this.aviliable = aviliable;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public BigDecimal getTotalVoteNum() {
            if (totalVoteNum == null) {
                return new BigDecimal(0);
            }
            return totalVoteNum;
        }

        public void setTotalVoteNum(BigDecimal totalVoteNum) {
            this.totalVoteNum = totalVoteNum;
        }
    }

    public static class VoteInfo implements Serializable {
        private String id;//候选人ID
        private String coinbase;//候选人地址
        private String name;//名称
        private String address;//所在地址
        private String toAccount;//投票地址
        private String count;//投票数量
        private long createTime;//时间
        private int operatorId;
        private int cate;//分类
        private String linkUrl;//网站地址
        private String description;//描述
        private String rank;//排名
        private String transactionHash;//交易hash
        private BigDecimal voteNum;
        private BigDecimal countNum;
        private long tTimestap;//投票日期
        private String state;//投票状态 0撤销投票，1已撤销，2撤销中，3撤销失败
        private String blockNumber;//区块号
        private String voterId;// 投票人ID
        private String insufficientBonus;//1-预存金额充足，0-预存金额不足
        private String bonusRate;//*0-未设置，其他如”50” 为设置比例
        private String bonusFlag;//1 未设置，2 5 具体值，3 4 6警告，

        public String getBonusFlag() {
            return bonusFlag;
        }

        public void setBonusFlag(String bonusFlag) {
            this.bonusFlag = bonusFlag;
        }

        public String getInsufficientBonus() {
            return StrUtil.isNull(insufficientBonus) ? "0" : insufficientBonus;
        }

        public void setInsufficientBonus(String insufficientBonus) {
            this.insufficientBonus = insufficientBonus;
        }

        public String getBonusRate() {
            return StrUtil.isNull(bonusRate) ? "0" : bonusRate;
        }

        public void setBonusRate(String bonusRate) {
            this.bonusRate = bonusRate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVoterId() {
            return voterId;
        }

        public void setVoterId(String voterId) {
            this.voterId = voterId;
        }

        public String getBlockNumber() {
            return StrUtil.isNull(blockNumber) ? "0" : blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getToAccount() {
            return StrUtil.isNull(toAccount) ? "" : toAccount;
        }

        public void setToAccount(String toAccount) {
            this.toAccount = toAccount;
        }

        public String getState() {
            return StrUtil.isNull(state) ? "0" : state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getTimestap() {
            return tTimestap;
        }

        public void setTTimestap(long tTimestap) {
            this.tTimestap = tTimestap;
        }

        public String getTransactionHash() {
            return StrUtil.isNull(transactionHash) ? "" : transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public BigDecimal getVoteNum() {
            if (voteNum == null) {
                return new BigDecimal(0);
            }
            return voteNum;
        }

        public void setVoteNum(BigDecimal voteNum) {
            this.voteNum = voteNum;
        }

        public BigDecimal getCountNum() {
            if (countNum == null) {
                return new BigDecimal(0);
            }
            return countNum;
        }

        public void setCountNum(BigDecimal countNum) {
            this.countNum = countNum;
        }

        public String getRank() {
            return StrUtil.isNull(rank) ? "0" : rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getCoinbase() {
            return StrUtil.isNull(coinbase) ? "" : coinbase;
        }

        public void setCoinbase(String coinbase) {
            this.coinbase = coinbase;
        }

        public String getName() {
            return StrUtil.isNull(name) ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return StrUtil.isNull(address) ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCount() {
            return StrUtil.isNull(count) ? "0" : count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(int operatorId) {
            this.operatorId = operatorId;
        }

        public int getCate() {
            return cate;
        }

        public void setCate(int cate) {
            this.cate = cate;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getDescription() {
            return StrUtil.isNull(description) ? "" : description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "VoteInfo{" +
                    "id='" + id + '\'' +
                    ", coinbase='" + coinbase + '\'' +
                    ", name='" + name + '\'' +
                    ", ='" + address + '\'' +
                    ", toAccount='" + toAccount + '\'' +
                    ", count='" + count + '\'' +
                    ", createTime=" + createTime +
                    ", operatorId=" + operatorId +
                    ", cate=" + cate +
                    ", linkUrl='" + linkUrl + '\'' +
                    ", description='" + description + '\'' +
                    ", rank='" + rank + '\'' +
                    ", transactionHash='" + transactionHash + '\'' +
                    ", voteNum=" + voteNum +
                    ", countNum=" + countNum +
                    ", tTimestap=" + tTimestap +
                    ", state='" + state + '\'' +
                    ", blockNumber='" + blockNumber + '\'' +
                    '}';
        }
    }
}
