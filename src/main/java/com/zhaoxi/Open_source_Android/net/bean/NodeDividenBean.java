package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/9/10.
 */

public class NodeDividenBean {
    private String address;//当前地址
    private String isNode;//是否为节点：1-是，0-否
    private String name;//节点名称
    private String rank;//排名
    private String num;//当前节点的投票数量（18WEI）
    private String totalCurrentNum;//本次投票总数量（18WEI）
    private String bonusRate;//分红比例：0-未设置，其他为真实比例
    private String bonusContractAddr;//分红合约地址
    private String isCanSet;//是否可设置比率：1-是，0-否（每一周）
    private String bonusBalance;//
    private String curBalance;//
    private String holderAddress;//节点持币地址

    //投票详情
    private int votersNum;//对当前节点投票总人数
    private String poolsNum;//对当前节点投票总数(18WEI)
    private List<VoteDetailsBean> list;

    public String getHolderAddress() {
        return StrUtil.isNull(holderAddress) ? "" : holderAddress;
    }

    public void setHolderAddress(String holderAddress) {
        this.holderAddress = holderAddress;
    }

    public String getIsCanSet() {
        return StrUtil.isNull(isCanSet) ? "0" : isCanSet;
    }

    public void setIsCanSet(String isCanSet) {
        this.isCanSet = isCanSet;
    }

    public int getVotersNum() {
        return votersNum;
    }

    public void setVotersNum(int votersNum) {
        this.votersNum = votersNum;
    }

    public String getPoolsNum() {
        return StrUtil.isNull(poolsNum) ? "0" : poolsNum;
    }

    public void setPoolsNum(String poolsNum) {
        this.poolsNum = poolsNum;
    }

    public List<VoteDetailsBean> getList() {
        return list;
    }

    public void setList(List<VoteDetailsBean> list) {
        this.list = list;
    }

    public static class VoteDetailsBean {
        private String address;//投票地址
        private String voteNum;//投票积分

        public String getAddress() {
            return StrUtil.isNull(address) ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getVoteNum() {
            return StrUtil.isNull(voteNum) ? "" : voteNum;
        }

        public void setVoteNum(String voteNum) {
            this.voteNum = voteNum;
        }
    }

    public String getBonusBalance() {
        return StrUtil.isNull(bonusBalance) ? "" : bonusBalance;
    }

    public void setBonusBalance(String bonusBalance) {
        this.bonusBalance = bonusBalance;
    }

    public String getCurBalance() {
        return StrUtil.isNull(curBalance) ? "" : curBalance;
    }

    public void setCurBalance(String curBalance) {
        this.curBalance = curBalance;
    }

    public String getAddress() {
        return StrUtil.isNull(address) ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsNode() {
        return StrUtil.isNull(isNode) ? "0" : isNode;
    }

    public void setIsNode(String isNode) {
        this.isNode = isNode;
    }

    public String getName() {
        return StrUtil.isNull(name) ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return StrUtil.isNull(rank) ? "0" : rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getNum() {
        return StrUtil.isNull(num) ? "0" : num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTotalCurrentNum() {
        return StrUtil.isNull(totalCurrentNum) ? "0" : totalCurrentNum;
    }

    public void setTotalCurrentNum(String totalCurrentNum) {
        this.totalCurrentNum = totalCurrentNum;
    }

    public String getBonusRate() {
        return StrUtil.isNull(bonusRate) ? "0" : bonusRate;
    }

    public void setBonusRate(String bonusRate) {
        this.bonusRate = bonusRate;
    }

    public String getBonusContractAddr() {
        return StrUtil.isNull(bonusContractAddr) ? "" : bonusContractAddr;
    }

    public void setBonusContractAddr(String bonusContractAddr) {
        this.bonusContractAddr = bonusContractAddr;
    }
}
