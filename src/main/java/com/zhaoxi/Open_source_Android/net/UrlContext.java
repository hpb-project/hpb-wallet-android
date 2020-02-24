package com.zhaoxi.Open_source_Android.net;

import com.zhaoxi.Open_source_Android.Config;

/**
 * Created by 51973 on 2018/5/9.
 */

public enum UrlContext {
    /* 查询nonce */
    Url_GetNonce("/personal/getNonce"),//获取nonce
    Url_CreateTrade("/transaction/sendRawTransaction"),//发送交易
    Url_TotalAccount("/personal/getBalance"),//获取账户余额
    Url_Total2Account("/personal/getLegalTenderBalance"),//获取单个账户法币余额
    Url_getBannerNews("/cms/carousel"),//获取轮播讯息
    Url_getArticleNews("/cms/article"),//获取最新资讯
    Url_getQuicklyNews("/cms/express"),//获取快讯消息
    Url_getMassgeData("/cms/notification/page"),//获取最新系统消息
    Url_suggestion("/cms/suggestion"),//意见反馈
    Url_getTransactionHistory("/transaction/getTransactionHistory"),//查询交易记录
    Url_checkVersion("/cms/version"),//版本检查
    Url_getHpbFee("/personal/getNonce"),//获取gasLimit gasPrice
    Url_transferEthBalance("/transfer/ethBalance"),//查询个人持有Hpb Token
    Url_transferEthNonce("/transfer/ethNonce"),//查询个人Nonce
    Url_transferFee("/transfer/fee"),//查询映射手续费
    Url_sendTransfer("/transfer/sendTransfer"),//发送映射交易
    Url_transferList("/transfer/transferList"),//查询映射列表
    Url_VoteRanking("/vote/ranking"),//查询投票排名
    Url_VotePersonalInfo("/vote/personalInfo"),//投票人信息
    Url_VoteVote("/vote/vote"),//投票
    Url_VoteVoteInfo("/vote/voteInfo"),//查询候选人信息
    Url_VoteSetHolderAddr("/vote/setHolderAddr"),//修改持币地址/授权
    Url_VoteCancel("/vote/cancel"),//撤销投票
    Url_VoteState("/vote/state"),//投票是否开启
    URL_VoteTrabsationReceipt("/transaction/getTransactionReceipt"),// 根据Hash查询成功Or失败状态
    Url_VoteHistory("/vote/history"),//我的投票记录
    Url_listBalance("/personal/listBalance"),//批量获取账户余额
    Url_BookPullAll("/cms/book/pullall"),//获取云数据
    Url_BookPushAll("/cms/book/pushall"),//提交全部绑定地址
    Url_PostDevice("/cms/device"),//上传本机相关钱包信息
    Url_getDapps("/cms/app/page"),//获取全部的DApps
    Url_updateMassege("/cms/notification/update"),//更新消息阅读
    Url_getMsgLast("/cms/notification/latest"),//获取最新消息
    Url_VitertifyMerchant("/merchant/vitertify"),//商户收款白名单校验接口
    Url_LogtxinfoMerchant("/merchant/logtxinfo"),//交易信息记录接口
    Url_CallBackMerchant("/merchant/callback"),//商户回调通知接口
    Url_RAWREADY("/packet/rawReady"),//准备发红包
    Url_SENDRAW("/packet/sendRaw"),//发送红包
    Url_REFRESHRAD("/packet/refresh"),//刷新红包
    Url_REDKEY_CHECK("/packet/keyCheck"),//进入红包钥匙校验
    Url_RED_DRAW("/packet/draw"),//领红包
    Url_BEFOREDRAW_CHECK("/packet/beforeDrawCheck"),//领取红包前校验
    Url_RED_RECORDS("/packet/records"),//红包记录
    Url_RED_DETAIL("/packet/detail"),//红包详情
    Url_Advertise("/cms/advertise"),//开屏广告
    Url_ProposalList("/proposal/list"),//题案列表
    Url_ProposalDetail("/proposal/detail"),//题案详情
    Url_ProposalPersonalRecords("/proposal/personalRecords"),//题案个人投票记录
    Url_ProposalVote("/proposal/vote"),//题案投票
    Url_PacketShakePacket("/packet/shakePacket"),// 摇一摇红包

    //2.0.0接口
    Url_stock("/token721/stock"),// HRC-721库存代币详情
    Url_transferRecord("/token721/transferRecord"),// HRC-721交易记录
    Url_tokenIdDetail("/token721/idDetail"),// 代币ID详情
    Url_tokenAllId("/token721/idsByTxHash"),// 当前账户下所有代币ID
    Url_tokenManage("/token/manage"),// 代币管理
    Url_token20Record("/token20/list"),// 代币20交易记录
    Url_token721Detail("/token721/txDetail"),// 代币721交易详情
    Url_transactionList("/transaction/list"),// 交易记录：HPB/HRC-20/HRC-721
    Url_walletBalance("/personal/getLegalTenderBalance"),//获取单个账户法币余额
    Url_listLegalBalances("/personal/listLegalBalances"),//多个账户资产
    Url_walletTransfer("/personal/walletTransfer"),//钱包转账

    Url_ListLegalBalances("/personal/listLegalBalances"),//多个账户资产
    Url_tokenTypelist("/transaction/typeList"),//获取币种

    //节点分红
    Url_NodeBonus("/node/bonus"),//节点分红
    Url_NodeVoteDetail("/node/voteDetail"),//投票详情
    Url_NodeRecords("/node/records"),//分红记录
    Url_NodeRecordDetail("/node/detail"),//分红记录详情
    Url_NodeBounsBalance("/node/bonusBalance"),//节点分红预设 金额
    ;



    private String context;
    UrlContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return Config.COMMON_URL + context;
    }

    @Override
    public String toString() {
        return this.context;
    }
}
