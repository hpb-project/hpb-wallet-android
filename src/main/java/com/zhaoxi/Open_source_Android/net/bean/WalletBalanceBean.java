package com.zhaoxi.Open_source_Android.net.bean;

import java.util.List;

/**
 * create by fangz
 * create date:2019/9/15
 * create time:13:39
 */
public class WalletBalanceBean {


    /**
     * cnyTotalValue : 1962502612710140800000
     * usdTotalValue : 276760752847823400000
     * list : [{"map":{},"address":"0xc2a25725e92d817e3aeed4dcc5be2b03d883bf26","cnyTotalValue":"1908222347537750400000","usdTotalValue":"269105911036864200000"},{"map":{},"address":"0xf9684ca6ea22935350440d5c1948b99ca6a1c286","cnyTotalValue":"54280265172390400000","usdTotalValue":"7654841810959200000"}]
     */

    private String cnyTotalValue;
    private String usdTotalValue;
    private List<BalanceInfo> list;

    public String getCnyTotalValue() {
        return cnyTotalValue;
    }

    public void setCnyTotalValue(String cnyTotalValue) {
        this.cnyTotalValue = cnyTotalValue;
    }

    public String getUsdTotalValue() {
        return usdTotalValue;
    }

    public void setUsdTotalValue(String usdTotalValue) {
        this.usdTotalValue = usdTotalValue;
    }

    public List<BalanceInfo> getList() {
        return list;
    }

    public void setList(List<BalanceInfo> list) {
        this.list = list;
    }

    public static class BalanceInfo {
        /**
         * map : {}
         * address : 0xc2a25725e92d817e3aeed4dcc5be2b03d883bf26
         * cnyTotalValue : 1908222347537750400000
         * usdTotalValue : 269105911036864200000
         */

        private MapBean map;
        private String address;
        private String cnyTotalValue;
        private String usdTotalValue;

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCnyTotalValue() {
            return cnyTotalValue;
        }

        public void setCnyTotalValue(String cnyTotalValue) {
            this.cnyTotalValue = cnyTotalValue;
        }

        public String getUsdTotalValue() {
            return usdTotalValue;
        }

        public void setUsdTotalValue(String usdTotalValue) {
            this.usdTotalValue = usdTotalValue;
        }

        public static class MapBean {
        }
    }
}
