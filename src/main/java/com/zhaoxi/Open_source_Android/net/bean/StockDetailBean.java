package com.zhaoxi.Open_source_Android.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:14:19
 */
public class StockDetailBean {


    /**
     * total : 329
     * pages : 10
     * list : [{"tokenId":"2294","tokenURI":"","count":0},{"tokenId":"2292","tokenURI":"","count":0},{"tokenId":"2291","tokenURI":"","count":0},{"tokenId":"2290","tokenURI":"","count":0},{"tokenId":"2288","tokenURI":"","count":0},{"tokenId":"2286","tokenURI":"","count":0},{"tokenId":"2289","tokenURI":"","count":0},{"tokenId":"2287","tokenURI":"","count":0},{"tokenId":"2293","tokenURI":"","count":0},{"tokenId":"2285","tokenURI":"","count":0}]
     * pageNum : 1
     */

    private int total;
    private int pages;
    private int pageNum;
    private List<TokenInfo> list;

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

    public List<TokenInfo> getList() {
        return list;
    }

    public void setList(List<TokenInfo> list) {
        this.list = list;
    }

    public static class TokenInfo implements Serializable {
        private static final long serialVersionUID = -4827050485419343967L;
        /**
         * tokenId : 2294
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

        @Override
        public String toString() {
            return "TokenInfo{" +
                    "tokenId='" + tokenId + '\'' +
                    ", tokenURI='" + tokenURI + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
