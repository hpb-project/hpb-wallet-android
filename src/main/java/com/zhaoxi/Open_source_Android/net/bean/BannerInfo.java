package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.List;

/**
 * des:广告信息
 * Created by ztt on 2018/6/6.
 */

public class BannerInfo {
    private int pages;//总页数
    private List<BaseInfo> list;//banner数据

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<BaseInfo> getList() {
        return list;
    }

    public void setList(List<BaseInfo> list) {
        this.list = list;
    }

    public static class BaseInfo {
        private int articleId;//id
        private int cateId;
        private int moduleId;
        private String source;//来源
        private String author;
        private String sourceUrl;
        private String images;//图片地址
        private String title;//标题
        private String targetUrl;
        private String targetUrlPrefix;

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

        public String getSource() {
            return StrUtil.isNull(source) ? "" : source;
        }

        public void setSource(String source) {
            this.source = source;
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
