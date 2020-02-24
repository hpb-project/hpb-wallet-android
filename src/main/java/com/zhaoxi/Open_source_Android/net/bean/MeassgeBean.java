package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/7/6.
 */

public class MeassgeBean {
    private int pages;
    private List<MeassgeInfo> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<MeassgeInfo> getList() {
        return list;
    }

    public void setList(List<MeassgeInfo> list) {
        this.list = list;
    }

    public static class MeassgeInfo implements Serializable {
        private int articleId;
        private int cateId;
        private String author;
        private String source;
        private String sourceUrl;
        private String summary;//文章摘要
        private String title;//标题
        private String subTitle;//二级标题
        private String shortTitle;//文章短标题
        private String content;
        private long createAtTime;//时间
        private long publishTime;
        private long updatedAt;
        private int readState;//是否阅读
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getReadState() {
            return readState;
        }

        public void setReadState(int readState) {
            this.readState = readState;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
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

        public String getAuthor() {
            return StrUtil.isNull(author) ? "" : author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSource() {
            return StrUtil.isNull(source) ? "" : source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getSummary() {
            return StrUtil.isNull(summary) ? "" : summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTitle() {
            return StrUtil.isNull(title) ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getShortTitle() {
            return shortTitle;
        }

        public void setShortTitle(String shortTitle) {
            this.shortTitle = shortTitle;
        }

        public String getContent() {
            return StrUtil.isNull(content) ? "" : content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateAtTime() {
            return createAtTime;
        }

        public void setCreateAtTime(long createAtTime) {
            this.createAtTime = createAtTime;
        }
    }
}
