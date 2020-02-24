package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class NewsBean {
    private int pages;//总页数
    private List<BaseNews> list;//banner数据

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<BaseNews> getList() {
        return list;
    }

    public void setList(List<BaseNews> list) {
        this.list = list;
    }

    public static class BaseNews {
        private int articleId;//id
        private int cateId;
        private int moduleId;
        //        private String source;//来源
        private String author;
        private String sourceUrl;
        private String images;//图片地址
        private String title;//标题
        private long createAtTime;//时间
        private long publishTime;
        private String content;//内容
        private String targetUrl;
        private String targetUrlPrefix;
        private String summary;

        private newsCount count;//总数

        private String logo;
        private String nameCn;
        private String nameEn;
        private String url;
        private int id;
        private boolean isAuthorize;
        private boolean isOuterBrowserOpen;

        public boolean isOuterBrowserOpen() {
            return isOuterBrowserOpen;
        }

        public void setIsOuterBrowserOpen(boolean outerBrowserOpen) {
            isOuterBrowserOpen = outerBrowserOpen;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAuthorize(boolean authorize) {
            isAuthorize = authorize;
        }

        public boolean isAuthorize() {
            return isAuthorize;
        }

        public void setIsAuthorize(boolean isAuthorize) {
            this.isAuthorize = isAuthorize;
        }

        public String getLogo() {
            return StrUtil.isNull(logo) ? "" : logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getNameCn() {
            return StrUtil.isNull(nameCn) ? "" : nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getNameEn() {
            return StrUtil.isNull(nameEn) ? "" : nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getUrl() {
            return StrUtil.isNull(url) ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public static class newsCount {
            private int viewNum;

            public int getViewNum() {
                return viewNum;
            }

            public void setViewNum(int viewNum) {
                this.viewNum = viewNum;
            }
        }

        public String getSummary() {
            return StrUtil.isNull(summary) ? "" : summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public newsCount getCount() {
            return count;
        }

        public void setCount(newsCount count) {
            this.count = count;
        }

        public long getCreateAtTime() {
            return createAtTime;
        }

        public void setCreateAtTime(long createAtTime) {
            this.createAtTime = createAtTime;
        }

        public String getContent() {
            return StrUtil.isNull(content) ? "" : content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }

        public int getCateId() {
            return cateId;
        }

        public void setCateId(int cateId) {
            this.cateId = cateId;
        }

        public int getModuleId() {
            return moduleId;
        }

        public void setModuleId(int moduleId) {
            this.moduleId = moduleId;
        }

        public String getAuthor() {
            return StrUtil.isNull(author) ? "" : author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getImages() {
            return StrUtil.isNull(images) ? "" : images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getTitle() {
            return StrUtil.isNull(title) ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getTargetUrlPrefix() {
            return targetUrlPrefix;
        }

        public void setTargetUrlPrefix(String targetUrlPrefix) {
            this.targetUrlPrefix = targetUrlPrefix;
        }
    }
}
