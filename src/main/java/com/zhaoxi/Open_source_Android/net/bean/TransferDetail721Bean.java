package com.zhaoxi.Open_source_Android.net.bean;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/11
 * create time:17:02
 */
public class TransferDetail721Bean {


    /**
     * data : {}
     * transferRecords : [{"map":{},"status":true,"gasUsed":127535,"token721StockDetailInfo":[{"tokenId":"5340","tokenURI":"","count":0},{"tokenId":"4907","tokenURI":"","count":0},{"tokenId":"4921","tokenURI":"","count":0},{"tokenId":"3577","tokenURI":"","count":0},{"tokenId":"3267","tokenURI":"","count":0},{"tokenId":"3458","tokenURI":"","count":0},{"tokenId":"2360","tokenURI":"","count":0},{"tokenId":"2359","tokenURI":"","count":0},{"tokenId":"2345","tokenURI":"","count":0},{"tokenId":"2366","tokenURI":"","count":0}],"txHash":"0x831a8776448c7fa64bf3864a7413cc897be56d50ca31cb1e5b5e576d1203489d","blockHash":"0x0b3e7df43b23b28d10858125901f9c090015ae7901b8f95f3868fe5c034bb689","blockNumber":3373640,"blockTimestamp":null,"contractAddress":"0xba29fa851805767609c25b7a032101cef1c2258b","fromAddress":"0xc2a25725e92d817e3aeed4dcc5be2b03d883bf26","toAddress":"0xb37c24243299bf42b94c376a1344d695e2984210","quantity":null,"tokenType":null,"logIndex":0,"tokenId":13,"createTimestamp":1567426441000,"updateTimestamp":1567426441000}]
     */

    private DataBean data;
    private List<TransferRecordsBean> transferRecords;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<TransferRecordsBean> getTransferRecords() {
        return transferRecords;
    }

    public void setTransferRecords(List<TransferRecordsBean> transferRecords) {
        this.transferRecords = transferRecords;
    }

    public static class DataBean {
    }

    public static class TransferRecordsBean {
        /**
         * map : {}
         * status : true
         * gasUsed : 127535
         * token721StockDetailInfo : [{"tokenId":"5340","tokenURI":"","count":0},{"tokenId":"4907","tokenURI":"","count":0},{"tokenId":"4921","tokenURI":"","count":0},{"tokenId":"3577","tokenURI":"","count":0},{"tokenId":"3267","tokenURI":"","count":0},{"tokenId":"3458","tokenURI":"","count":0},{"tokenId":"2360","tokenURI":"","count":0},{"tokenId":"2359","tokenURI":"","count":0},{"tokenId":"2345","tokenURI":"","count":0},{"tokenId":"2366","tokenURI":"","count":0}]
         * txHash : 0x831a8776448c7fa64bf3864a7413cc897be56d50ca31cb1e5b5e576d1203489d
         * blockHash : 0x0b3e7df43b23b28d10858125901f9c090015ae7901b8f95f3868fe5c034bb689
         * blockNumber : 3373640
         * blockTimestamp : null
         * contractAddress : 0xba29fa851805767609c25b7a032101cef1c2258b
         * fromAddress : 0xc2a25725e92d817e3aeed4dcc5be2b03d883bf26
         * toAddress : 0xb37c24243299bf42b94c376a1344d695e2984210
         * quantity : null
         * tokenType : null
         * logIndex : 0
         * tokenId : 13
         * createTimestamp : 1567426441000
         * updateTimestamp : 1567426441000
         */

        private MapBean map;
        private boolean status;
        private int gasUsed;
        private String txHash;
        private String blockHash;
        private int blockNumber;
        private Object blockTimestamp;
        private String contractAddress;
        private String fromAddress;
        private String toAddress;
        private Object quantity;
        private Object tokenType;
        private int logIndex;
        private int tokenId;
        private long createTimestamp;
        private long updateTimestamp;
        private List<Token721StockDetailInfoBean> token721StockDetailInfo;

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(int gasUsed) {
            this.gasUsed = gasUsed;
        }

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

        public Object getBlockTimestamp() {
            return blockTimestamp;
        }

        public void setBlockTimestamp(Object blockTimestamp) {
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

        public void setQuantity(Object quantity) {
            this.quantity = quantity;
        }

        public Object getTokenType() {
            return tokenType;
        }

        public void setTokenType(Object tokenType) {
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

        public List<Token721StockDetailInfoBean> getToken721StockDetailInfo() {
            return token721StockDetailInfo;
        }

        public void setToken721StockDetailInfo(List<Token721StockDetailInfoBean> token721StockDetailInfo) {
            this.token721StockDetailInfo = token721StockDetailInfo;
        }

        public static class MapBean {
        }

        public static class Token721StockDetailInfoBean {
            /**
             * tokenId : 5340
             * tokenURI :
             * count : 0
             */

            private String tokenId;
            private String tokenURI;
            private int count;

            public String getTokenId() {
                return tokenId;
            }

            public void setTokenId(String tokenId) {
                this.tokenId = tokenId;
            }

            public String getTokenURI() {
                return tokenURI;
            }

            public void setTokenURI(String tokenURI) {
                this.tokenURI = tokenURI;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
    }
}
