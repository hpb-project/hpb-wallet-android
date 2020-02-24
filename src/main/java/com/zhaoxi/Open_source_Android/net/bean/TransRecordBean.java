package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * create by fangz
 * create date:2019/9/12
 * create time:16:27
 */
public class TransRecordBean {

    /**
     * total : 10
     * list : [{"map":{},"transactionHash":"0x48f665499d203e37e32ce65959537c518627c4824b6f9bdb9fe10dd14d03d169","fromAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","toAccount":"0xd7127ad5220f704e1ba0cf81b649df12a9d7927a","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583217,"transactionIndex":null,"tTimestap":1566218032,"gasUsed":"87535","remark":null,"tInput":null,"flag":"0","tokenId":"1196","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x48f665499d203e37e32ce65959537c518627c4824b6f9bdb9fe10dd14d03d169","fromAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","toAccount":"0xd7127ad5220f704e1ba0cf81b649df12a9d7927a","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583217,"transactionIndex":null,"tTimestap":1566218032,"gasUsed":"87535","remark":null,"tInput":null,"flag":"0","tokenId":"1196","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1114","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1115","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1116","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1117","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1118","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1119","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1120","tokenType":"HRC-721","actulTxFee":null},{"map":{},"transactionHash":"0x5f593dca9afb22b8e071cc24bd924f9fbd307065408f3059e8f567196672482c","fromAccount":"0x0000000000000000000000000000000000000000","toAccount":"0x6c085b38e68be32f2bd5d7fceef271c165de8d26","nonce":null,"tValue":null,"gas":null,"gasPrice":null,"status":"0x1","blockHash":null,"blockNumber":3583141,"transactionIndex":null,"tTimestap":1566217466,"gasUsed":"21526560","remark":null,"tInput":null,"flag":"1","tokenId":"1121","tokenType":"HRC-721","actulTxFee":null}]
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * startRow : 0
     * endRow : 9
     * pages : 1
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * firstPage : 1
     * lastPage : 1
     */

    private int total;
    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<TransInfo> list;
    private List<Integer> navigatepageNums;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<TransInfo> getList() {
        return list;
    }

    public void setList(List<TransInfo> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class TransInfo implements Serializable {
        private static final long serialVersionUID = 9196048031331272162L;
        /**
         * map : {}
         * transactionHash : 0x48f665499d203e37e32ce65959537c518627c4824b6f9bdb9fe10dd14d03d169
         * fromAccount : 0x6c085b38e68be32f2bd5d7fceef271c165de8d26
         * toAccount : 0xd7127ad5220f704e1ba0cf81b649df12a9d7927a
         * nonce : null
         * tValue : null
         * gas : null
         * gasPrice : null
         * status : 0x1
         * blockHash : null
         * blockNumber : 3583217
         * transactionIndex : null
         * tTimestap : 1566218032
         * gasUsed : 87535
         * remark : null
         * tInput : null
         * flag : 0
         * tokenId : 1196
         * tokenType : HRC-721
         * actulTxFee : null
         */

//        private MapBean map;
        private String transactionHash;
        private String contractAddress;
        private long blockTimestamp;
        private String fromAccount;
        private String toAccount;
        private String nonce;
        private String tValue;
        private String gas;
        private String gasPrice;
        private String status;
        private String blockHash;
        private int blockNumber;
        private String transactionIndex;
        private int tTimestap;
        private String gasUsed;
        private String remark;
        private String tInput;
        private String flag;  // 0--> from ,1-->to
        private String tokenId;
        private String tokenType;
        private String actulTxFee;


        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public long getBlockTimestamp() {
            return blockTimestamp;
        }

        public void setBlockTimestamp(long blockTimestamp) {
            this.blockTimestamp = blockTimestamp;
        }

        public String gettValue() {
            return tValue;
        }

        public void settValue(String tValue) {
            this.tValue = tValue;
        }

        public int gettTimestap() {
            return tTimestap;
        }

        public void settTimestap(int tTimestap) {
            this.tTimestap = tTimestap;
        }

        public String gettInput() {
            return tInput;
        }

        public void settInput(String tInput) {
            this.tInput = tInput;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getFromAccount() {
            return fromAccount;
        }

        public void setFromAccount(String fromAccount) {
            this.fromAccount = fromAccount;
        }

        public String getToAccount() {
            return toAccount;
        }

        public void setToAccount(String toAccount) {
            this.toAccount = toAccount;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getTValue() {
            return StrUtil.isEmpty(tValue) ? "0" : tValue;
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
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public int getTTimestap() {
            return tTimestap;
        }

        public void setTTimestap(int tTimestap) {
            this.tTimestap = tTimestap;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTInput() {
            return tInput;
        }

        public void setTInput(String tInput) {
            this.tInput = tInput;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getActulTxFee() {
            return StrUtil.isEmpty(actulTxFee) ? "0" : actulTxFee;
        }

        public void setActulTxFee(String actulTxFee) {
            this.actulTxFee = actulTxFee;
        }

//        public static class MapBean {
//        }


    }
}
