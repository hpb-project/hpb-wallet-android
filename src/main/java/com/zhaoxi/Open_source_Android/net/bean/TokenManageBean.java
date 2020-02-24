package com.zhaoxi.Open_source_Android.net.bean;


import java.util.List;

/**
 * create by fangz
 * create date:2019/9/10
 * create time:10:27
 */
public class TokenManageBean {


    /**
     * total : 1
     * pageNum : 1
     * pages : 1
     * list : [{"map":{},"id":"A04FBD076A725851600D5CF1C4F9D446","tokenSymbol":"RPT","tokenSymbolImageUrl":null,"tokenName":"RedPacketToken","decimals":0,"deployTxHash":"0x9e099a673550c76ded94286bcf95e956d543c608c3a9b9023b6bb46cc5c70bdc","contractCreater":"0x79bcd1c3e9a514ce83abf2b62f20c1fccedcb6f9","contractAddress":"0xba29fa851805767609c25b7a032101cef1c2258b","tokenTotalSupply":1035,"contractType":"HRC-721","verifiedStatus":null,"price":null,"changeRate":null,"volume24h":null,"marketCap":null,"holders":null,"transfersNum":null,"status":"1","createTimestamp":1566057676000,"updateTimestamp":1566057676000,"cnyPrice":"0","usdPrice":"0","cnyTotalValue":"0","usdTotalValue":"0"}]
     */

    private List<TokenInfo> list;

    public List<TokenInfo> getList() {
        return list;
    }

    public void setList(List<TokenInfo> list) {
        this.list = list;
    }

}
