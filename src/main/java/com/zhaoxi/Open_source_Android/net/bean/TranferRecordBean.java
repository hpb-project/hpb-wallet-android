package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * des:交易记录 基础类
 * Created by ztt on 2018/6/15.
 */

public class TranferRecordBean {
    private int pages;//页数
    private List<TransferInfo> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<TransferInfo> getList() {
        return list;
    }

    public void setList(List<TransferInfo> list) {
        this.list = list;
    }

    public static class TransferInfo implements Serializable {
        private static final long serialVersionUID = -5617139369318480781L;
        private String transactionHash;//交易hash值
        private String fromAccount;//转账地址 来源
        private String toAccount;//转账地址 去
        private BigDecimal nonce;
        private String tValue;//转账值
        private String gas;
        private String gasPrice;
        private String blockHash;
        private String blockNumber;//区块号
        private int transactionIndex;
        private long tTimestap;
        private String gasUsed;
        private String tInput;
        private String actulTxFee;//手续费
        private String tokenType;//代币简称
        private String flag;  // 0--> from ,1-->to
        private String tokenId;
        private String contractAddress;
        private String tokenSymbol;//简称
        private String tokenURI;//代币 头像

        public String getTokenURI() {
            return StrUtil.isNull(tokenURI) ? "" : tokenURI;
        }

        public void setTokenURI(String tokenURI) {
            this.tokenURI = tokenURI;
        }

        public String getTokenSymbol() {
            return StrUtil.isNull(tokenSymbol) ? "" : tokenSymbol;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public String getContractAddress() {
            return StrUtil.isNull(contractAddress) ? "" : contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getFlag() {
            return StrUtil.isNull(flag) ? "0" : flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTokenId() {
            return StrUtil.isNull(tokenId) ? "" : tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getTokenType() {
            return StrUtil.isNull(tokenType) ? "" : tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getTransactionHash() {
            return StrUtil.isNull(transactionHash) ? "" : transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getFromAccount() {
            return StrUtil.isNull(fromAccount) ? "" : fromAccount;
        }

        public void setFromAccount(String fromAccount) {
            this.fromAccount = fromAccount;
        }

        public String getToAccount() {
            return StrUtil.isNull(toAccount) ? "" : toAccount;
        }

        public void setToAccount(String toAccount) {
            this.toAccount = toAccount;
        }

        public BigDecimal getNonce() {
            if (nonce == null) return new BigDecimal(0);
            return nonce;
        }

        public void setNonce(BigDecimal nonce) {
            this.nonce = nonce;
        }

        public String getValue() {
            return StrUtil.isNull(tValue) ? "0" : tValue;
        }

        public void setTValue(String tValue) {
            this.tValue = tValue;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return StrUtil.isNull(gasPrice) ? "0" : gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getBlockNumber() {
            return StrUtil.isNull(blockNumber) ? "" : blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public int getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(int transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public long getTimestap() {
            return tTimestap;
        }

        public void setTTimestap(long tTimestap) {
            this.tTimestap = tTimestap;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String gettInput() {
            return tInput;
        }

        public void settInput(String tInput) {
            this.tInput = tInput;
        }

        public String getActulTxFee() {
            return StrUtil.isNull(actulTxFee) ? "0" : actulTxFee;
        }

        public void setActulTxFee(String actulTxFee) {
            this.actulTxFee = actulTxFee;
        }
    }
}
