package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/8/21.
 */

public class VoteZLBean {
    private int pages;
    private List<VoteZlInfo> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<VoteZlInfo> getList() {
        return list;
    }

    public void setList(List<VoteZlInfo> list) {
        this.list = list;
    }

    public static class VoteZlInfo implements Serializable {
        private int id;
        private String issueNo;//题案编号
        private String voteStatus;//投票状态：0-初始化，1-投票中，2-揭晓中，3-已通过，4-被否决，5-已作废
        private String nameZh;// 投票名称中文
        private String nameEn;// 投票名称英文
        private String descZh;//  描述中文
        private String descEn;//  描述英文
        private String value1Zh;//"支持"选项一中文
        private String value2Zh;//"不支持",选项二中文
        private String value1En;//选项一英文
        private String value2En;// 选项二英文
        private String floorNum;//最少起投票数
        private long beginTime;//开始时间
        private long endTime;//结束时间
        private String gmtCreate;// 数据创建时间
        private String gmtModify;// 作废时间
        private long countDownTime;//倒计时
        private String value1Rate;//选项一占比
        private String value2Rate;//选项二占比
        private String value1Num;//选项一数量
        private String value2Num;//选项二数量
        private String peragreeNum;//选项一已投数量
        private String perdisagreeNum;//选项二已投数量
        private String totalNum;//该题案总票数
        private String aviliableNum;//可投票数
        private String issurContractAddress;//投票治理 投票合约地址

        public String getIssurContractAddress() {
            return StrUtil.isNull(issurContractAddress)?"0":issurContractAddress;
        }

        public void setIssurContractAddress(String issurContractAddress) {
            this.issurContractAddress = issurContractAddress;
        }

        public String getAviliableNum() {
            return StrUtil.isNull(aviliableNum)?"0":aviliableNum;
        }

        public void setAviliableNum(String aviliableNum) {
            this.aviliableNum = aviliableNum;
        }

        public String getPeragreeNum() {
            return StrUtil.isNull(peragreeNum)?"0":peragreeNum;
        }

        public void setPeragreeNum(String peragreeNum) {
            this.peragreeNum = peragreeNum;
        }

        public String getPerdisagreeNum() {
            return StrUtil.isNull(perdisagreeNum)?"0":perdisagreeNum;
        }

        public void setPerdisagreeNum(String perdisagreeNum) {
            this.perdisagreeNum = perdisagreeNum;
        }

        public String getTotalNum() {
            return StrUtil.isNull(totalNum)?"0":totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIssueNo() {
            return StrUtil.isNull(issueNo) ? "" : issueNo;
        }

        public void setIssueNo(String issueNo) {
            this.issueNo = issueNo;
        }

        public String getVoteStatus() {
            return StrUtil.isNull(voteStatus) ? "0" : voteStatus;
        }

        public void setVoteStatus(String voteStatus) {
            this.voteStatus = voteStatus;
        }

        public String getNameZh() {
            return StrUtil.isNull(nameZh) ? "" : nameZh;
        }

        public void setNameZh(String nameZh) {
            this.nameZh = nameZh;
        }

        public String getNameEn() {
            return StrUtil.isNull(nameEn) ? "" : nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getDescZh() {
            return StrUtil.isNull(descZh) ? "" : descZh;
        }

        public void setDescZh(String descZh) {
            this.descZh = descZh;
        }

        public String getDescEn() {
            return StrUtil.isNull(descEn) ? "" : descEn;
        }

        public void setDescEn(String descEn) {
            this.descEn = descEn;
        }

        public String getValue1Zh() {
            return StrUtil.isNull(value1Zh) ? "" : value1Zh;
        }

        public void setValue1Zh(String value1Zh) {
            this.value1Zh = value1Zh;
        }

        public String getValue2Zh() {
            return StrUtil.isNull(value2Zh) ? "" : value2Zh;
        }

        public void setValue2Zh(String value2Zh) {
            this.value2Zh = value2Zh;
        }

        public String getValue1En() {
            return StrUtil.isNull(value1En) ? "" : value1En;
        }

        public void setValue1En(String value1En) {
            this.value1En = value1En;
        }

        public String getValue2En() {
            return StrUtil.isNull(value2En) ? "" : value2En;
        }

        public void setValue2En(String value2En) {
            this.value2En = value2En;
        }

        public String getFloorNum() {
            return StrUtil.isNull(floorNum)?"0":floorNum;
        }

        public void setFloorNum(String floorNum) {
            this.floorNum = floorNum;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(String gmtModify) {
            this.gmtModify = gmtModify;
        }

        public long getCountDownTime() {
            return countDownTime;
        }

        public void setCountDownTime(long countDownTime) {
            this.countDownTime = countDownTime;
        }

        public String getValue1Rate() {
            return StrUtil.isNull(value1Rate)?"0":value1Rate;
        }

        public void setValue1Rate(String value1Rate) {
            this.value1Rate = value1Rate;
        }

        public String getValue2Rate() {
            return StrUtil.isNull(value2Rate)?"0":value2Rate;
        }

        public void setValue2Rate(String value2Rate) {
            this.value2Rate = value2Rate;
        }

        public String getValue1Num() {
            return StrUtil.isNull(value1Num)?"0":value1Num;
        }

        public void setValue1Num(String value1Num) {
            this.value1Num = value1Num;
        }

        public String getValue2Num() {
            return StrUtil.isNull(value2Num)?"0":value2Num;
        }

        public void setValue2Num(String value2Num) {
            this.value2Num = value2Num;
        }
    }
}
