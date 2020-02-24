package com.zhaoxi.Open_source_Android.net.bean;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/10
 * create time:11:57
 */
public class TokenIdMoreBean {


    /**
     * total : 329
     * pages : 9
     * list : [{"tokenId":"418","tokenURI":"","count":2},{"tokenId":"382","tokenURI":"","count":2},{"tokenId":"381","tokenURI":"","count":2},{"tokenId":"380","tokenURI":"","count":2},{"tokenId":"462","tokenURI":"","count":2},{"tokenId":"411","tokenURI":"","count":2},{"tokenId":"460","tokenURI":"","count":2},{"tokenId":"429","tokenURI":"","count":2},{"tokenId":"436","tokenURI":"","count":2}]
     * pageNum : 33
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

    public static class TokenIdInfo {
        /**
         * tokenId : 418
         * tokenURI :
         * count : 2
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
