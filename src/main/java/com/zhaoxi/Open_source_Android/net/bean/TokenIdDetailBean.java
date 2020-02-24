package com.zhaoxi.Open_source_Android.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:17:21
 */
public class TokenIdDetailBean {


    /**
     * total : 2
     * pages : 1
     * list : [{"map":{},"txHash":"0x729d4c98bcd396f696f86960e3490ef2e67105ec0477fb647271fb358eac9d83","blockHash":"0x6023672c870aba85118e2fba1290c804b561fec4f0f095ce51db32881946bb26","blockNumber":3536242,"blockTimestamp":1565863755,"contractAddress":"0xba29fa851805767609c25b7a032101cef1c2258b","fromAddress":"0x0000000000000000000000000000000000000000","toAddress":"0xe12fb2f2c399d2a514d64836131a97a515c4fcfb","quantity":null,"tokenType":null,"logIndex":16,"tokenId":382,"createTimestamp":1567426782000,"updateTimestamp":1567426782000},{"map":{},"txHash":"0x729d4c98bcd396f696f86960e3490ef2e67105ec0477fb647271fb358eac9d83","blockHash":"0x6023672c870aba85118e2fba1290c804b561fec4f0f095ce51db32881946bb26","blockNumber":3536242,"blockTimestamp":1565863755,"contractAddress":"0xba29fa851805767609c25b7a032101cef1c2258b","fromAddress":"0x0000000000000000000000000000000000000000","toAddress":"0xe12fb2f2c399d2a514d64836131a97a515c4fcfb","quantity":null,"tokenType":null,"logIndex":16,"tokenId":382,"createTimestamp":1567426782000,"updateTimestamp":1567426782000}]
     * pageNum : 1
     */

    private int total;
    private int pages;
    private int pageNum;
    private List<TokenIdInfo> list;

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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<TokenIdInfo> getList() {
        return list;
    }

    public void setList(List<TokenIdInfo> list) {
        this.list = list;
    }

    public static class TokenIdInfo implements Serializable {
        /**
         * map : {}
         * txHash : 0x729d4c98bcd396f696f86960e3490ef2e67105ec0477fb647271fb358eac9d83
         * blockHash : 0x6023672c870aba85118e2fba1290c804b561fec4f0f095ce51db32881946bb26
         * blockNumber : 3536242
         * blockTimestamp : 1565863755
         * contractAddress : 0xba29fa851805767609c25b7a032101cef1c2258b
         * fromAddress : 0x0000000000000000000000000000000000000000
         * toAddress : 0xe12fb2f2c399d2a514d64836131a97a515c4fcfb
         * quantity : null
         * tokenType : null
         * logIndex : 16
         * tokenId : 382
         * createTimestamp : 1567426782000
         * updateTimestamp : 1567426782000
         */

        private String txHash;
        private String blockHash;
        private int blockNumber;
        private int blockTimestamp;
        private String contractAddress;
        private String fromAddress;
        private String toAddress;
        private String quantity;
        private String tokenType;
        private int logIndex;
        private int tokenId;
        private long createTimestamp;
        private long updateTimestamp;

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public int getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            this.blockNumber = blockNumber;
        }

        public int getBlockTimestamp() {
            return blockTimestamp;
        }

        public void setBlockTimestamp(int blockTimestamp) {
            this.blockTimestamp = blockTimestamp;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public Object getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public Object getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public int getLogIndex() {
            return logIndex;
        }

        public void setLogIndex(int logIndex) {
            this.logIndex = logIndex;
        }

        public int getTokenId() {
            return tokenId;
        }

        public void setTokenId(int tokenId) {
            this.tokenId = tokenId;
        }

        public long getCreateTimestamp() {
            return createTimestamp;
        }

        public void setCreateTimestamp(long createTimestamp) {
            this.createTimestamp = createTimestamp;
        }

        public long getUpdateTimestamp() {
            return updateTimestamp;
        }

        public void setUpdateTimestamp(long updateTimestamp) {
            this.updateTimestamp = updateTimestamp;
        }

        @Override
        public String toString() {
            return "TokenIdInfo{" +
                    ", txHash='" + txHash + '\'' +
                    ", blockHash='" + blockHash + '\'' +
                    ", blockNumber=" + blockNumber +
                    ", blockTimestamp=" + blockTimestamp +
                    ", contractAddress='" + contractAddress + '\'' +
                    ", fromAddress='" + fromAddress + '\'' +
                    ", toAddress='" + toAddress + '\'' +
                    ", quantity=" + quantity +
                    ", tokenType=" + tokenType +
                    ", logIndex=" + logIndex +
                    ", tokenId=" + tokenId +
                    ", createTimestamp=" + createTimestamp +
                    ", updateTimestamp=" + updateTimestamp +
                    '}';
        }
    }
}
