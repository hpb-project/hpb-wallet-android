package com.zhaoxi.Open_source_Android.net.bean;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * @author ztt
 * @des
 * @date 2019/7/25.
 */

public class GetRedStatusBean {
    private String status;//1-成功，0-失败，2-已结束，3-已领取，4-已失效，5-领取中
    private String coinValue;//只有status=5时，该返返回该字段 （18GWEI）

    public String getStatus() {
        return StrUtil.isNull(status) ? "0" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoinValue() {
        return StrUtil.isNull(coinValue) ? "0" : coinValue;
    }

    public void setCoinValue(String coinValue) {
        this.coinValue = coinValue;
    }
}
