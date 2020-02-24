package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/9/10.
 */

public class DividendRecordsBean {
    private int pages;//总页数
    private List<RecordsBean> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RecordsBean> getList() {
        return list;
    }

    public void setList(List<RecordsBean> list) {
        this.list = list;
    }

    public static class RecordsBean {
        private String id;//分红编号ID
        private String address;//发放分红地址
        private long tradeTime;//交易时间
        private String bonusValue;//分红总金额(18wei)
        private String coinSymbol;//币种
        private String txHash;//交易哈希
        private long gmtCreate;
        private long gmtModify;
        private String bonusId;//分红编号ID
        private String voteAddr;//投票地址
        private String totalVote;//投票数量(18wei)
        private String totalScore;//分红(18wei)
        private String score;//积分(18wei)
        private String bonus;//分红金额(18wei)
        private String voterAddr;//投票地址

        public String getVoterAddr() {
            return StrUtil.isNull(voterAddr)?"":voterAddr;
        }

        public void setVoterAddr(String voterAddr) {
            this.voterAddr = voterAddr;
        }

        public String getBonus() {
            return StrUtil.isNull(bonus)?"0":bonus;
        }

        public void setBonus(String bonus) {
            this.bonus = bonus;
        }

        public String getScore() {
            return StrUtil.isNull(score)?"0":score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTotalScore() {
            return StrUtil.isNull(totalScore) ? "" : totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public String getId() {
            return StrUtil.isNull(id) ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return StrUtil.isNull(address) ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getBonusValue() {
            return StrUtil.isNull(bonusValue) ? "0" : bonusValue;
        }

        public void setBonusValue(String bonusValue) {
            this.bonusValue = bonusValue;
        }

        public String getCoinSymbol() {
            return StrUtil.isNull(coinSymbol) ? "" : coinSymbol;
        }

        public void setCoinSymbol(String coinSymbol) {
            this.coinSymbol = coinSymbol;
        }

        public String getTxHash() {
            return StrUtil.isNull(txHash) ? "" : txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public long getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(long gmtModify) {
            this.gmtModify = gmtModify;
        }

        public String getBonusId() {
            return StrUtil.isNull(bonusId) ? "" : bonusId;
        }

        public void setBonusId(String bonusId) {
            this.bonusId = bonusId;
        }

        public String getVoteAddr() {
            return StrUtil.isNull(voteAddr) ? "" : voteAddr;
        }

        public void setVoteAddr(String voteAddr) {
            this.voteAddr = voteAddr;
        }

        public String getTotalVote() {
            return StrUtil.isNull(totalVote) ? "0" : totalVote;
        }

        public void setTotalVote(String totalVote) {
            this.totalVote = totalVote;
        }
    }
}

