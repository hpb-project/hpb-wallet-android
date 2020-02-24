package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * des:
 * Created by ztt on 2018/9/5.
 */

public class VotePersonBean {
    private String aviliable;
    private String balance;
    private String currentBlockNumber;
    private String totalVoteNum;
    private String status;//投票结果状态值
    private String voteState;//投票是否开启
    private String voteContractAddress;//投票的合约地址
    private String voteRule;//代投中文描述
    private String voteRuleEN;//代投英文描述
    private String numForNode;//对当前节点投票数量


    /*  查询候选人 */
    private String address;//位置
    private String isRunUpStage;//是否是竞选中：1-竞选中，0-竞选结束
    private String flag;//标识：1-容许修改持币地址，2-持币地址已被授权，节点地址未被授权，0-未被授权地址
    private String holderAddr;//持币地址

    private String id;//候选人ID
    private String coinbase;//候选人地址
    private String description;//投票描述信息
    private String count;// 投票数量

    public String getId() {
        return StrUtil.isNull(id) ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoinbase() {
        return StrUtil.isNull(coinbase) ? "" : coinbase;
    }

    public void setCoinbase(String coinbase) {
        this.coinbase = coinbase;
    }

    public String getDescription() {
        return StrUtil.isNull(description) ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCount() {
        return StrUtil.isNull(count) ? "0" : count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAddress() {
        return StrUtil.isNull(address) ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHolderAddr() {
        return StrUtil.isNull(holderAddr) ? "" : holderAddr;
    }

    public void setHolderAddr(String holderAddr) {
        this.holderAddr = holderAddr;
    }

    public String getIsRunUpStage() {
        return StrUtil.isNull(isRunUpStage) ? "1" : isRunUpStage;
    }

    public void setIsRunUpStage(String isRunUpStage) {
        this.isRunUpStage = isRunUpStage;
    }

    public String getFlag() {
        return StrUtil.isNull(flag) ? "0" : flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVoteRule() {
        return StrUtil.isNull(voteRule) ? "" : voteRule;
    }

    public void setVoteRule(String voteRule) {
        this.voteRule = voteRule;
    }

    public String getVoteRuleEN() {
        return StrUtil.isNull(voteRuleEN) ? "" : voteRuleEN;
    }

    public void setVoteRuleEN(String voteRuleEN) {
        this.voteRuleEN = voteRuleEN;
    }

    public String getVoteState() {
        return StrUtil.isNull(voteState) ? "0" : voteState;
    }

    public void setVoteState(String voteState) {
        this.voteState = voteState;
    }

    public String getVoteContractAddress() {
        return StrUtil.isNull(voteContractAddress) ? "" : voteContractAddress;
    }

    public void setVoteContractAddress(String voteContractAddress) {
        this.voteContractAddress = voteContractAddress;
    }

    public String getStatus() {
        return StrUtil.isNull(status) ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCurrentBlockNumber() {
        return currentBlockNumber;
    }

    public void setCurrentBlockNumber(String currentBlockNumber) {
        this.currentBlockNumber = currentBlockNumber;
    }

    public String getTotalVoteNum() {
        return totalVoteNum;
    }

    public void setTotalVoteNum(String totalVoteNum) {
        this.totalVoteNum = totalVoteNum;
    }


    public String getNumForNode() {
        return StrUtil.isNull(numForNode) ? "0" : numForNode;
    }

    public void setNumForNode(String numForNode) {
        this.numForNode = numForNode;
    }

    @Override
    public String toString() {
        return "VotePersonBean{" +
                "aviliable='" + aviliable + '\'' +
                ", balance='" + balance + '\'' +
                ", currentBlockNumber='" + currentBlockNumber + '\'' +
                ", totalVoteNum='" + totalVoteNum + '\'' +
                ", status='" + status + '\'' +
                ", voteState='" + voteState + '\'' +
                ", voteContractAddress='" + voteContractAddress + '\'' +
                ", voteRule='" + voteRule + '\'' +
                ", voteRuleEN='" + voteRuleEN + '\'' +
                ", numForNode='" + numForNode + '\'' +
                ", address='" + address + '\'' +
                ", isRunUpStage='" + isRunUpStage + '\'' +
                ", flag='" + flag + '\'' +
                ", holderAddr='" + holderAddr + '\'' +
                ", id='" + id + '\'' +
                ", coinbase='" + coinbase + '\'' +
                ", description='" + description + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
