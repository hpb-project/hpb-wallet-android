package com.zhaoxi.Open_source_Android.net.bean;

/**
 * create by fangz
 * create date:2019/10/9
 * create time:18:02
 */
public class DigitalSignRecordBean {
    private String dateTime;
    private String content;

    public DigitalSignRecordBean(String dateTime, String content) {
        this.dateTime = dateTime;
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
