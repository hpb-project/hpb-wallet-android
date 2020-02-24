package com.zhaoxi.Open_source_Android.common.bean;

import java.io.Serializable;

/**
 * des:
 * Created by ztt on 2018/12/12.
 */

public class BandingAddressBean implements Serializable {
    private int bId;
    private String sortLetters;
    private boolean isChecked;
    private String mark;//名称
    private String addressContact;//联系地址

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAddressContact() {
        return addressContact;
    }

    public void setAddressContact(String addressContact) {
        this.addressContact = addressContact;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getBId() {
        return bId;
    }

    public void setBId(int bId) {
        this.bId = bId;
    }
}
