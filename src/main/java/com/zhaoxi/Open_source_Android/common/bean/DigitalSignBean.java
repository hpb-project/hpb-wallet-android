package com.zhaoxi.Open_source_Android.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * create by fangz
 * create date:2019/10/14
 * create time:14:14
 */
@Entity
public class DigitalSignBean implements Serializable {

    private static final long serialVersionUID = 776981918680925165L;

    @Id(autoincrement = true)
    private Long id;
    /** 字符串内容*/
    private String strContent;
    /** 签名内容*/
    private String signContent;
    /** 签名时间*/
    private String signDateTime;

    @Generated(hash = 346698432)
    public DigitalSignBean(Long id, String strContent, String signContent,
            String signDateTime) {
        this.id = id;
        this.strContent = strContent;
        this.signContent = signContent;
        this.signDateTime = signDateTime;
    }

    @Generated(hash = 1738714382)
    public DigitalSignBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrContent() {
        return this.strContent;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }

    public String getSignContent() {
        return this.signContent;
    }

    public void setSignContent(String signContent) {
        this.signContent = signContent;
    }

    public String getSignDateTime() {
        return this.signDateTime;
    }

    public void setSignDateTime(String signDateTime) {
        this.signDateTime = signDateTime;
    }

}
