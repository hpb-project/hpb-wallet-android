package com.zhaoxi.Open_source_Android.libs.utils;

import com.alibaba.fastjson.JSONArray;

import java.util.Arrays;

/**
 * create by fangz
 * create date:2019/10/21
 * create time:13:59
 */
public class ColdWalletUtil {

    /**
     * 转账
     */
    public static final String TYPE_TRANSFER = "0";
    /**
     * 投票授权
     */
    public static final String TYPE_AUTHOR = "1";
    /**
     * 治理投票
     */
    public static final String TYPE_VOTE = "2";
    /**
     * 字符串
     */
    public static final String TYPE_STR = "3";
    /**
     * 同步资产
     */
    public static final String TYPE_SYNC_ASSETS = "4";

    /**
     * 竞选投票 - 投票
     */
    public static final String TYPE_VOTE_V = "5";

    private ColdWalletUtil() {

    }


    /**
     * 冷钱包二维码数据
     *
     * @return
     */
    public static String toJson(Object... params) {
        JSONArray jsonArray = new JSONArray();
        if (params != null && params.length > 0) {
            jsonArray.addAll(Arrays.asList(params));
        }
        return jsonArray.toJSONString();
    }
}
