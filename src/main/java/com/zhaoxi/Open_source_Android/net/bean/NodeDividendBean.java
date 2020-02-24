package com.zhaoxi.Open_source_Android.net.bean;

/**
 * @author ztt
 * @des
 * @date 2019/8/12.
 */
@Deprecated
public class NodeDividendBean {
    private long nodetime;
    private String money;

    private String rank;
    private String pollNUm;
    private String devidend;
    private String address;

    /* 节点投票详情 */
    private String allPersonNum;
    private String allPollNum;

    private String holdId;//ID
    private boolean isChecked = false; //本地用
    private String range;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }


    public String getAllPersonNum() {
        return allPersonNum;
    }

    public void setAllPersonNum(String allPersonNum) {
        this.allPersonNum = allPersonNum;
    }

    public String getAllPollNum() {
        return allPollNum;
    }

    public void setAllPollNum(String allPollNum) {
        this.allPollNum = allPollNum;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPollNUm() {
        return pollNUm;
    }

    public void setPollNUm(String pollNUm) {
        this.pollNUm = pollNUm;
    }

    public String getDevidend() {
        return devidend;
    }

    public void setDevidend(String devidend) {
        this.devidend = devidend;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getNodetime() {
        return nodetime;
    }

    public void setNodetime(long nodetime) {
        this.nodetime = nodetime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
