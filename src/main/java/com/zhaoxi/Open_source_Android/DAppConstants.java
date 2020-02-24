package com.zhaoxi.Open_source_Android;

import android.content.Context;
import android.os.Environment;

import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

/**
 * des:
 * Created by ztt on 2018/7/9.
 */

public class DAppConstants {
    public static final String SHARE_APP_DOWNURL = "https://www.Open_source_Android.io/wallet";
    public static final String SHARE_APP_DOWNURL_name = "hpbwallet_down_url";
    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final String DATA_DIRECTORY_PATH = Environment.getDataDirectory().getAbsolutePath() + "/data/";
    // Image path
    public static final String PATH_IMAGE_CACHE = "/HPBWallet/images/compress/";
    //图片文件 重命名
    public static final String IMAGE_FILE_NAME_NEW_RRPORT = "create_new_compress_";

    // 文件地址
    public static final String PATH_DOWNLOAD_FILE = "/HPBWallet/download/";
    public static final String PATH_PIC_FILE = "/HPBWallet/images/createPic/";
    public static final String PATH_PIC_HEADER_FILE = "/HPBWallet/images/header/";
    public static final String PATH_PIC_FILE_SMALL = "/HPBWallet/images/createPic_small/";
    public static final String PATH_EXPROT_FILE = "/HPBWallet/data/";

    public static final String VOTE_CONTRACT_ADDRESS = "vote_contract_address";

    public static final String VOTE_RULE_AGAIN_ZH = "vote_rule_agagin_zh";
    public static final String VOTE_RULE_AGAIN_EN = "vote_rule_agagin_en";

    public static final String MONEY_EYES_STATUS = "money_eyes_status";//0 显示，1 隐藏
    public static final String RECEIVE_ADDRESS_WARN_STATUS = "receive_address_warn_status";// 0 弹出，1 不弹出

    //意见反馈 图片设置大小
    public static final int DEFULT_SELECT_PHOTO_NUM = 3;

    //映射 智能合约地址
    public static final String MAIN_MAP_TOKEN_ADDRESS = "0x38c6a68304cdefb9bec48bbfaaba5c5b47818bb2";

    public static final String MAIN_MAP_TO_ADDRESS = "0x594823177a7041cd7969722f529a47276cd5b18c";
    //dapps 第三方支付 合约地址
    public static final String THREE_PAY_TO_ADDRESS = "0xf2afad01844d276a7fccf8e268c4a78b97da9bdd";

    public static final String TYPE_HRC_721 = "HRC-721";
    public static final String TYPE_HRC_20 = "HRC-20";
    public static final String TYPE_HPB = "HPB";

    public static final String ETHER_SACN_URL = "https://etherscan.io/tx/";
    public static final String HPB_SACN_URL = "https://hpbscan.org/tx/";
    public static final String HPB_ACOUNT_URL = "https://hpbscan.org/tx/";
    //HPB区块链浏览器获取账户信息
    public static final String HPB_INFO_URL = "http://hpbscan.org/address/";

    public static final String NEWS_DETAILS = "/articles?id=";//文章详情
    public static final String HELP_CENTER_EN = "/home?language=1";//常见问题 英文
    public static final String HELP_CENTER_ZH = "/home?language=0";//常见问题 中文
    public static final String WHAT_PRIVATE_EN = "/privatesen";//什么是私钥 英文
    public static final String WHAT_PRIVATE_ZH = "/privates";//什么是私钥 中文
    public static final String WHAT_KEYSTORE_EN = "/keystoreen";//什么是keystore 英文
    public static final String WHAT_KEYSTORE_ZH = "/keystore";//什么是keystore 中文
    public static final String WHAT_MNEONICE_EN = "/mnemonicen";//什么是助记词 英文
    public static final String WHAT_MNEONICE_ZH = "/mnemonic";//什么是助记词 中文
    public static final String SECERT_SERVICES_EN = "/servicesen";//服务隐私条款 英文
    public static final String SECERT_SERVICES_ZH = "/services";//服务隐私条款 中文
    public static final String CONTACT_US_EN = "/contacten";//联系我们-英文
    public static final String CONTACT_US_ZH = "/contact";//联系我们-中文
    public static final String WHAT_COPY_WALLET_ZH = "/wallets";//什么是备份钱包-中文
    public static final String WHAT_COPY_WALLET_EN = "/walletsen";//什么是备份钱包-英文
    public static final String USE_SERVICES_ZH = "/agreement";//使用协议-中文
    public static final String USE_SERVICES_EN = "/agreementen";//使用协议-英文
    public static final String GAS_PRICE_EN = "/gaspriceen";//交易费用-英文
    public static final String GAS_PRICE_ZH = "/gasprice";//交易费用-中文
    public static final String RULE_VOTE_ZH = "/voterule";//投票规则-中文
    public static final String RULE_VOTE_EN = "/voteruleen";//投票规则-英文
    public static final String HOW_USE_COLDWALLET_ZH = "/userguide";//如何使用冷钱包-中文
    public static final String HOW_USE_COLDWALLET_EN = "/userguideen";//如何使用冷钱包-英文
    public static final String RULE_VOTE_ZL_ZH = "/votingRulesDk";//链上投票规则-中文
    public static final String RULE_VOTE_ZL_EN = "/votingRulesDken";//链上投票规则-英文
    public static final String RULE_DIVIDEND_VOTE_ZL_ZH = "/VotingReward";//节点投票分红规则-中文
    public static final String RULE_DIVIDEND_VOTE_ZL_EN = "/VotingRewarden";//节点投票分红规则-英文

    public static final int ADDRESS_SIZE = 40;

    /**
     * 根据chooseType判断来源，判断返回的代投说明文字
     *
     * @param context
     * @return
     */
    public static String getVoteAgainRule(Context context) {
        int type = ChangeLanguageUtil.languageType(context);
        String sharedName = type == 1 ? VOTE_RULE_AGAIN_ZH : VOTE_RULE_AGAIN_EN;
        return SharedPreferencesUtil.getSharePreString(context, sharedName);
    }

    /**
     * 根据chooseType判断来源，返回不同的url后缀
     *
     * @param context
     * @return
     */
    public static String backUrlHou(Context context, int chooseType) {
        int type = ChangeLanguageUtil.languageType(context);
        String urlHou = "";
        switch (chooseType) {
            case 1://什么是私钥
                urlHou = type == 1 ? WHAT_PRIVATE_ZH : WHAT_PRIVATE_EN;
                break;
            case 2://什么是keystore
                urlHou = type == 1 ? WHAT_KEYSTORE_ZH : WHAT_KEYSTORE_EN;
                break;
            case 3://什么是助记词
                urlHou = type == 1 ? WHAT_MNEONICE_ZH : WHAT_MNEONICE_EN;
                break;
            case 4://服务隐私条款
                urlHou = type == 1 ? SECERT_SERVICES_ZH : SECERT_SERVICES_EN;
                break;
            case 5://常见问题
                urlHou = type == 1 ? HELP_CENTER_ZH : HELP_CENTER_EN;
                break;
            case 6://联系我们
                urlHou = type == 1 ? CONTACT_US_ZH : CONTACT_US_EN;
                break;
            case 7://什么是备份钱包
                urlHou = type == 1 ? WHAT_COPY_WALLET_ZH : WHAT_COPY_WALLET_EN;
                break;
            case 8://使用协议
                urlHou = type == 1 ? USE_SERVICES_ZH : USE_SERVICES_EN;
                break;
            case 9:
                urlHou = type == 1 ? GAS_PRICE_ZH : GAS_PRICE_EN;
                break;
            case 10://投票规则
                urlHou = type == 1 ? RULE_VOTE_ZH : RULE_VOTE_EN;
                break;
            case 11://如何使用冷钱包
                urlHou = type == 1 ? HOW_USE_COLDWALLET_ZH : HOW_USE_COLDWALLET_EN;
                break;
            case 12://链上投票规则
                urlHou = type == 1 ? RULE_VOTE_ZL_ZH : RULE_VOTE_ZL_EN;
                break;
            case 13://节点投票分红规则
                urlHou = type == 1 ? RULE_DIVIDEND_VOTE_ZL_ZH : RULE_DIVIDEND_VOTE_ZL_EN;
                break;
        }
        return urlHou;
    }
}
