package com.zhaoxi.Open_source_Android.db;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.crypto.Bip39WalletUtil;
import com.zhaoxi.Open_source_Android.web3.crypto.CipherException;
import com.zhaoxi.Open_source_Android.web3.crypto.Credentials;
import com.zhaoxi.Open_source_Android.web3.crypto.ECKeyPair;
import com.zhaoxi.Open_source_Android.web3.crypto.Wallet;
import com.zhaoxi.Open_source_Android.web3.crypto.WalletFile;
import com.zhaoxi.Open_source_Android.web3.lambdaworks.MyWallet;
import com.zhaoxi.Open_source_Android.web3.protocol.ObjectMapperFactory;
import com.zhaoxi.Open_source_Android.web3.utils.AESUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * 将数据放在数据库中 从数据库获取数据
 * Created by 51973 on 2018/5/22.
 */

public class CreateDbWallet {
    private static DBManager mDbMananger;

    public CreateDbWallet(Context context) {
        mDbMananger = new DBManager(context);
    }

    /**
     * 查询全部的数据
     *
     * @return
     */
    public static List<WalletBean> queryAllWallet(Context context) {
        List<WalletBean> data = mDbMananger.queryAllWallet(context);
        return data;
    }

    /**
     * 查询所有的钱包地址
     *
     * @return
     */
    public static List<String> queryAllAddress() {
        List<String> data = mDbMananger.queryAllAddress();
        return data;
    }

    /**
     * 查询全部的映射数据
     *
     * @return
     */
    public static List<WalletBean> queryAllMapWallet(Context context) {
        List<WalletBean> data = mDbMananger.queryAllMapWallet(context);
        return data;
    }

    /**
     * 查询最新的钱包
     *
     * @return
     */
    public static WalletBean queryNewWallet() {
        WalletBean walletBean = mDbMananger.queryNewWallet();
        return walletBean;
    }

    /**
     * 根据address查询钱包
     *
     * @param address
     * @return
     */
    public static WalletBean queryWallet(Context context,String address) {
        WalletBean walletBean = mDbMananger.queryWallet(context,address);
        return walletBean;
    }

    /**
     * 根据address判断是否为冷钱包
     *
     * @return
     */
    public static boolean isColdWallet(Context context,String address) {
        WalletBean walletBean = mDbMananger.queryWallet(context,address);
        if(walletBean.getIsClodWallet() == 1){
            return true;
        }else return false;
    }

    /**
     * 生成带助记词的账号（钱包）
     *
     * @param context
     * @param walletName
     * @param password
     * @return 0:成功 1：数据库已存在 2：失败
     */
    public static int exportBip39Wallet(Context context, String walletName, String password, String prompt) {
        //step1:生成带助记词的钱包
        WalletBean walletBean = null;
        Bip39WalletUtil walletUtils = new Bip39WalletUtil(context);
        try {
            walletBean = walletUtils.generateBip39Wallet(walletName, password, prompt);
            //step2:将数据插入数据库
            return mDbMananger.insertWallet(walletBean);
        } catch (CipherException | IOException e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * keyStore-导出钱包
     *
     * @param address
     * @return
     */
    public static String exportWalletKeyStore(Context context, String address, String password) throws IOException {
        String keyStore = null;
        //step1:根据address查询出数据
        WalletBean walletBean = mDbMananger.queryWallet(context,address);
        keyStore = walletBean.getKeyStore();
        //step2:根据keystore+password 是否能生成私钥
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            WalletFile walletFile = objectMapper.readValue(keyStore, WalletFile.class);
            ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
        } catch (CipherException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                keyStore = context.getResources().getString(R.string.wallet_db_hanle_01);
            } else if (e.getMessage().contains("Wallet cipher is not supported")) {
                keyStore = context.getResources().getString(R.string.wallet_db_hanle_02);
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            keyStore = context.getResources().getString(R.string.wallet_db_hanle_03);
        }

        return keyStore;
    }

    /**
     * 私钥-导出钱包
     *
     * @param address
     * @param password
     * @return
     */
    public static String exportWalletPrivateKey(Context context, String address, String password) {
        return chackPsdIsRight(context, address, password);
    }

    /**
     * 验证keystore+密码是否生成私钥
     *
     * @param address
     * @param password
     * @return
     */
    private static String chackPsdIsRight(Context context, String address, String password) {
        String privateKey = null;
        //step1:根据adddress 查找出keyStore
        WalletBean walletBean = mDbMananger.queryWallet(context,address);
        if (walletBean == null) {
            return context.getResources().getString(R.string.wallet_db_hanle_08);
        }
        String keyStore = walletBean.getKeyStore();
        //step2:根据keyStore+password 生成私钥
        //step1:检查是否能生成私钥
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            WalletFile walletFile = objectMapper.readValue(keyStore, WalletFile.class);
            ECKeyPair ecKeyPair = null;
            ecKeyPair = Wallet.decrypt(password, walletFile);
            privateKey = ecKeyPair.getPrivateKey().toString(16);
        } catch (CipherException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                privateKey = context.getResources().getString(R.string.wallet_db_hanle_01);
            } else if (e.getMessage().contains("Wallet cipher is not supported")) {
                privateKey = context.getResources().getString(R.string.wallet_db_hanle_02);
            }
            e.printStackTrace();
        } catch (IOException e) {
            privateKey = context.getResources().getString(R.string.wallet_db_hanle_03);
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 助记词-导出钱包
     *
     * @param context
     * @param address
     * @param password
     * @return
     */
    public static String exportWalletMnemonic(Context context, String address, String password) {
        String mnemonic = null;
        //step1:根据adddress 查找出keyStore
        WalletBean walletBean = mDbMananger.queryWallet(context,address);
        //step2:取出mnemonic加密数据,根据输入的密码解密
        mnemonic = AESUtil.decrypt(password, walletBean.getMnemonic());
        //step3:判断解密之后的数据 是否是正确的助记词
        Bip39WalletUtil walletUtil = new Bip39WalletUtil(context);
        if (walletUtil.isMnemonic(mnemonic)) {
            return mnemonic;
        } else return context.getResources().getString(R.string.wallet_db_hanle_01);
    }

    /**
     * keyStore-导入钱包
     *
     * @param walletName
     * @param keystore
     * @param password
     * @return 0:成功 1：数据库已存在 2：失败 3:密码错误或其他错误
     */
    public static int importWalletKeyStore(Context context, String walletName, String keystore,
                                           String password, String type,boolean useFullScrypt) {
        String privateKey = null;
        //step1:检查是否能生成私钥
        privateKey = importCommDb(context, keystore, password,useFullScrypt);
        if (privateKey.startsWith("Failed") || privateKey.contains("失败")) {
            return 3;
        }
        //step2:为防止address被修改私钥在生成keyStore
        Credentials credentials = Credentials.create(privateKey);
        return insertCommDb(context, walletName, password, "", "", credentials, type);
    }

    /**
     * 私钥-导入钱包-生成keyStore
     *
     * @param privateKey
     * @param password
     * @return 0:成功 1：数据库已存在 2：失败 3:私钥不规范
     */
    public static int importWalletPrivateKey(Context context, String walletName, String privateKey,
                                             String password, String prompt, String type) {
        if (!Bip39WalletUtil.isValidPrivateKey(privateKey)) {
            return 3;
        }
        Credentials credentials = Credentials.create(privateKey);
        return insertCommDb(context, walletName, password, "", prompt, credentials, type);
    }

    /**
     * 助记词-导入钱包-生成keyStore
     *
     * @param password
     * @param mnemonic
     * @return 0:成功 1：数据库已存在 2：失败 3:助记词错误
     */
    public static int importWalletMnemonic(Context context, String walletName, String password, String mnemonic, String prompt) {
        //step1:判断助记词是否合理
        Bip39WalletUtil walletUtils = new Bip39WalletUtil(context);
        if (!walletUtils.isMnemonic(mnemonic)) {
            return 3;
        } else {
            Credentials credentials = Bip39WalletUtil.loadBip39Credentials("", mnemonic);
            return insertCommDb(context, walletName, password, "", prompt, credentials, "0");
        }
    }

    /**
     * 导入冷钱包
     *
     * @param walletName
     * @param address
     * @return 0:成功 1：数据库已存在 2：失败
     */
    public static int importWalletCold(String walletName,String address){
        if(StrUtil.isEmpty(walletName)||StrUtil.isEmpty(address)){
            return 2;
        }
        boolean isCun = mDbMananger.queryIsWallet(address);
        if(isCun){
            return 1;
        }
        WalletBean walletBean = new WalletBean();
        walletBean.setAddress(address);
        walletBean.setKeyStore("");
        walletBean.setMnemonic("");
        //钱包名称
        walletBean.setWalletBName(walletName);
        //钱包密码提示信息
        walletBean.setPrompt("");
        //钱包类型
        walletBean.setWalletType("0");
        walletBean.setIsClodWallet(1);
        //step5:将数据保存到db中
        return mDbMananger.insertWallet(walletBean);
    }

    /**
     * 插入数据的共同方法
     *
     * @param walletName
     * @param password
     * @param mnemonic
     * @param credentials
     * @return 0:成功 1：数据库已存在 2：失败 5:数据库已存在，并已修改为映射钱包
     */
    private static int insertCommDb(Context context, String walletName, String password,
                                    String mnemonic, String prompt, Credentials credentials,
                                    String type) {
        //step1:生成WalletFile
        WalletFile walletFile;
        try {
            walletFile = Wallet.createLight(password, credentials.getEcKeyPair());
            //step2:判断数据是否存在
            String address = walletFile.getAddress().startsWith("0x") ? walletFile.getAddress() : "0x" + walletFile.getAddress();
            boolean isCun = mDbMananger.queryIsWallet(address);

            if (!isCun) {
                //step3:生成keyStore json
                ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
                String jsonStr = objectMapper.writeValueAsString(walletFile);

                WalletBean walletBean = new WalletBean();
                walletBean.setAddress(walletFile.getAddress());
                walletBean.setKeyStore(jsonStr);
                //step4:对mnemonic进行加密
                walletBean.setMnemonic("".equals(mnemonic) ? "" : AESUtil.encrypt(password, mnemonic));
                //钱包名称
                walletBean.setWalletBName(walletName.equals("") ? context.getResources().getString(R.string.wallet_db_hanle_04) : walletName);
                //钱包密码提示信息
                walletBean.setPrompt(prompt);
                //钱包类型
                walletBean.setWalletType(type);
                //step5:将数据保存到db中
                return mDbMananger.insertWallet(walletBean);
            } else {
                boolean isMAP = mDbMananger.queryIsMapWallet(address);
                if (isMAP) {
                    return 5;
                }
                return 1;
            }
        } catch (CipherException e) {
            e.printStackTrace();
            return 2;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * 导入keystore验证是否正确
     *
     * @param keystore
     * @param password
     * @return
     */
    private static String importCommDb(Context context, String keystore, String password,boolean useFullScrypt) {
        String privateKey = null;
        //step1:检查是否能生成私钥
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
            ECKeyPair ecKeyPair = null;
            if(useFullScrypt){
                ecKeyPair = MyWallet.decrypt(password, walletFile);
            }else{//轻钱包
                ecKeyPair = Wallet.decrypt(password, walletFile);
            }
            privateKey = ecKeyPair.getPrivateKey().toString(16);
        } catch (CipherException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                privateKey = context.getResources().getString(R.string.wallet_db_hanle_05);
            } else if (e.getMessage().contains("Wallet cipher is not supported")) {
                privateKey = context.getResources().getString(R.string.wallet_db_hanle_06);
            }
            e.printStackTrace();
        } catch (IOException e) {
            privateKey = context.getResources().getString(R.string.wallet_db_hanle_07);
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 根据私钥获取address
     *
     * @param context
     * @param privateKey
     * @param password
     * @return
     */
    public static String getprivateKeyAddress(Context context, String privateKey, String password) {
        String address;
        Credentials credentials = Credentials.create(privateKey);
        //step1:生成WalletFile
        WalletFile walletFile;
        try {
            walletFile = Wallet.createLight(password, credentials.getEcKeyPair());
            //step2:判断数据是否存在
            address = walletFile.getAddress().startsWith("0x") ? walletFile.getAddress() : "0x" + walletFile.getAddress();
        } catch (CipherException e) {
            address = context.getResources().getString(R.string.main_map_toast_change_type_error);
        }

        return address;
    }

    /**
     * 根据Keystroe获取address
     *
     * @param context
     * @param keystore
     * @param password
     * @return
     */
    public static String getKeystroeAddress(Context context, String keystore, String password,boolean useFullScrypt) {
        String address;
        String privateKey = importCommDb(context, keystore, password,useFullScrypt);
        Credentials credentials = Credentials.create(privateKey);
        //step1:生成WalletFile
        WalletFile walletFile;
        try {
            walletFile = Wallet.createLight(password, credentials.getEcKeyPair());
            //step2:判断数据是否存在
            address = walletFile.getAddress().startsWith("0x") ? walletFile.getAddress() : "0x" + walletFile.getAddress();
        } catch (CipherException e) {
            address = context.getResources().getString(R.string.main_map_toast_change_type_error);
        }

        return address;
    }

    /**
     * 修改钱包名称
     *
     * @param address
     * @param newName
     * @return 0：成功  2失败
     */
    public static int updateWalletName(String address, String newName) {
        return mDbMananger.updataWallet(address, "walletName", newName);
    }

    /**
     * 修改助记词
     *
     * @param address
     * @return 0：成功  2失败
     */
    public static int updateWalletHeader(String address, String path) {
        return mDbMananger.updataWallet(address, "imgpath", path);
    }

    /**
     * 修改keystore
     *
     * @param address
     * @param keystore
     * @return 0：成功  2失败
     */
    public static int updateWalletKeystore(String address, String keystore) {
        return mDbMananger.updataWallet(address, "keyStore", keystore);
    }

    /**
     * 修改助记词
     *
     * @param address
     * @return 0：成功  2失败
     */
    public static int updateMnemonicstore(String address, String type) {
        return mDbMananger.updataWallet(address, "desZhujici", type);
    }

    /**
     * 修改Map walletType值
     *
     * @param address
     * @return 0：成功  2失败
     */
    public static int updateWalletType(String address, String content) {
        return mDbMananger.updataWallet(address, "walletType", content);
    }

    /**
     * 修改密码
     *
     * @param address
     * @param oldPsd
     * @param newPsd
     * @return 0：成功  3：密码输入错误 2：修改密码失败
     */
    public static int updatePassword(Context context, String address, String oldPsd, String newPsd) {
        String jsonStr = "";
        //step1:验证密码是否输入正确-->生成私钥
        String privateKey = chackPsdIsRight(context, address, oldPsd);
        if (privateKey.startsWith("Failed") || privateKey.contains("失败")) {
            //密码错误
            return 3;
        } else {
            //step2:判断是否存在助记词
            WalletBean walletBean = mDbMananger.queryWallet(context,address);
            String mnemonic = walletBean.getMnemonic();
            //step2.1:有->助记词 根据助记词生成私钥
            if (!StrUtil.isEmpty(mnemonic)) {
                //step2.1.1:私钥+密码 生成keystore
                Credentials credentials = Credentials.create(privateKey);
                WalletFile walletFile = null;
                try {
                    walletFile = Wallet.createLight(newPsd, credentials.getEcKeyPair());
                    //step4:生成keyStore json
                    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
                    jsonStr = objectMapper.writeValueAsString(walletFile);
                } catch (CipherException e) {
                    e.printStackTrace();
                    return 2;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return 2;
                }
            } else {//step2.2:无->助记词 根据私钥+密码 生成新的keystore
                Credentials credentials = Credentials.create(privateKey);
                try {
                    WalletFile walletFile = Wallet.createLight(newPsd, credentials.getEcKeyPair());
                    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
                    jsonStr = objectMapper.writeValueAsString(walletFile);
                } catch (CipherException e) {
                    e.printStackTrace();
                    return 2;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return 2;
                }
            }
            //step3:修改keystore
            return updateWalletKeystore(address, jsonStr);
        }
    }

    /**
     * 删除钱包
     *
     * @param address
     * @param password
     * @return 0:成功 1:密码错误 2：失败
     */
    public static int deleteWallet(Context context, String address,int imagId, String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, JsonProcessingException {
        //step1:根据地址查询数据keyStore
        WalletBean walletBean = mDbMananger.queryWallet(context,address);
        //step2:使用keyStore+password判断是否生成私钥
        String privateKey = importCommDb(context, walletBean.getKeyStore(), password,false);
        if (privateKey.startsWith("Failed") || privateKey.contains("失败")) {
            return 1;
        } else {
            //step3:提交数据库
            return mDbMananger.deleteWallet(address,imagId);
        }
    }

    /**
     * 删除冷钱包
     * @param address
     * @param imagId
     * @return 0:成功 2：失败
     */
    public static int deleteColdWallet(String address,int imagId){
        return mDbMananger.deleteWallet(address,imagId);
    }


    /**
     * 判断是否存在钱包
     *
     * @return
     */
    public static boolean isHasWalletNum() {
        int count = mDbMananger.allCaseNum();
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断创建钱包数量是否超过10条
     *
     * @return
     */
    public static boolean isOverWalletNum() {
        int count = mDbMananger.allCaseNum();
        if (count >= 10) {
            return true;
        }
        return false;
    }


    /***************************************图片使用情况*****************************************/
    /**
     * 查询所有数据条数
     * @return
     */
    public static int queryAllImgNum(){
        return mDbMananger.queryAllImgNum();
    }

    /**
     * 插入数据
     * @return
     */
    public static int insertImageData(){
        return mDbMananger.insertImageData();
    }

    /*************************************** 绑定地址 *****************************************/
    /**
     * 查询所有数据条数
     * @return
     */
    public static List<BandingAddressBean> queryAllBandAddress(){
        return mDbMananger.queryAllBandAddress();
    }

    /**
     * 查询某一条数据
     * @param id
     * @return
     */
    public static BandingAddressBean queryBandAddress(int id){
        return mDbMananger.queryBandAddress(id);
    }

    /**
     * 插入数据
     * @return
     */
    public static int insertBandAddress(BandingAddressBean bandingAddressBean){
        return mDbMananger.insertBandAddress(bandingAddressBean);
    }

    /**
     * 同步更新数据
     * @param bandAddressData
     * @return
     */
    public static int insertAllBandAddress(List<BandingAddressBean> bandAddressData){
        return mDbMananger.insertAllBandAddress(bandAddressData);
    }

    /**
     * 更新数据
     * @param id
     * @param clumName
     * @param content
     * @return
     */
    public static int updateBandAddress(int id,String clumName, String content){
        return mDbMananger.updateBandAddress(id,clumName,content);
    }

    /**
     * 更新整条数据
     * @param id
     * @param name
     * @param address
     * @return
     */
    public static int updateColBandAddress(int id,String name, String address) {
        return mDbMananger.updateColBandAddress(id,name,address);
    }

    /**
     * 删除绑定数据
     * @param id
     * @return
     */
    public static int deleteBandAddress(int id){
        return mDbMananger.deleteBandAddress(id);
    }

    /***************************************授权绑定*****************************************/
    /**
     * 插入数据
     * @return
     */
    public static int insertDappsAuth(String address,String authId){
        return mDbMananger.insertDappsAuth(address,authId);
    }
    /**
     * 查询某一项是否存在
     * @return
     */
    public static boolean queryDappsAuth(String address,String authId){
        return mDbMananger.queryDappsAuth(address,authId);
    }
}
