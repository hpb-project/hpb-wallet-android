package com.zhaoxi.Open_source_Android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;
import com.zhaoxi.Open_source_Android.common.bean.ImgUseBean;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 业务方法
 * Created by 51973 on 2018/5/23.
 */

public class DBManager {
    private static SQLiteDatabase db;

    public DBManager(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * 插入钱包数据
     *
     * @param walletBean
     * @return 0:成功 2：失败
     */
    public static int insertWallet(WalletBean walletBean) {
        //step1:开始事物
        db.beginTransaction();
        try {
            //step2:插入数据操作
            ContentValues values = new ContentValues();
            values.put("address", walletBean.getAddress().startsWith("0x") ? walletBean.getAddress() : "0x" + walletBean.getAddress());//钱包地址
            values.put("keyStore", walletBean.getKeyStore());//keystore
            values.put("walletName", walletBean.getWalletBName());//钱包名称
            values.put("desZhujici", walletBean.getMnemonic());//助记词
            values.put("psdprompt", walletBean.getPrompt());//密码提示信息
            values.put("walletType", walletBean.getWalletType());//钱包类型 0为普通钱包  1为映射钱包
            values.put("isClodWallet", walletBean.getIsClodWallet());//是否为冷钱包 0为热钱包  1为冷钱包
            values.put("imgpath", walletBean.getImgPath());//头像地址
            //执行插入操作
            db.insert(DBHelper.DATABASE_TABLE_NAME, null, values);
            db.setTransactionSuccessful();//设置事务成功完成
            return 0;
        } catch (Exception e) {
            return 2;
        } finally {
            db.endTransaction();//结束事务
        }
    }

    /**
     * 查询所有的钱包
     *
     * @return
     */
    public static List<WalletBean> queryAllWallet(Context context) {
        List<WalletBean> data = new ArrayList<>();
        String sql = "select * from " + DBHelper.DATABASE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            WalletBean walletBean = new WalletBean();
            String name = cursor.getString(cursor.getColumnIndex("walletName"));
            walletBean.setWalletBName(context.getResources().getString(R.string.wallet_db_hanle_04).equals(name) ? name + cursor.getInt(cursor.getColumnIndex("id")) : name);
            walletBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            walletBean.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
            walletBean.setMnemonic(cursor.getString(cursor.getColumnIndex("desZhujici")));
            walletBean.setIsClodWallet(cursor.getInt(cursor.getColumnIndex("isClodWallet")));
            walletBean.setImgPath(cursor.getString(cursor.getColumnIndex("imgpath")));
            data.add(walletBean);
        }
        cursor.close();
        return data;
    }

    /**
     * 查询所有的钱包地址
     *
     * @return
     */
    public static List<String> queryAllAddress() {
        List<String> data = new ArrayList<>();
        String sql = "select * from " + DBHelper.DATABASE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndex("address"));
            data.add(address);
        }
        cursor.close();
        return data;
    }

    /**
     * 查询所有映射的钱包
     *
     * @return
     */
    public static List<WalletBean> queryAllMapWallet(Context context) {
        List<WalletBean> data = new ArrayList<>();
        String sql = "select * from " + DBHelper.DATABASE_TABLE_NAME + " where walletType = '1'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            WalletBean walletBean = new WalletBean();
            String name = cursor.getString(cursor.getColumnIndex("walletName"));
            walletBean.setWalletBName(context.getResources().getString(R.string.wallet_db_hanle_04).equals(name) ? name + cursor.getInt(cursor.getColumnIndex("id")) : name);
            walletBean.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            walletBean.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
            walletBean.setMnemonic(cursor.getString(cursor.getColumnIndex("desZhujici")));
            walletBean.setImgPath(cursor.getString(cursor.getColumnIndex("imgpath")));
            data.add(walletBean);
        }
        cursor.close();
        return data;
    }

    /**
     * 取出最新的钱包
     *
     * @return
     */
    public static WalletBean queryNewWallet() {
        WalletBean walletBean = null;
        String sql = "SELECT * FROM " + DBHelper.DATABASE_TABLE_NAME + " where id = ( select max(id) id from " + DBHelper.DATABASE_TABLE_NAME + " );";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            walletBean = new WalletBean();
            walletBean.setWalletBName(c.getString(c.getColumnIndex("walletName")));
            walletBean.setAddress(c.getString(c.getColumnIndex("address")));
            walletBean.setMnemonic(c.getString(c.getColumnIndex("desZhujici")));
            walletBean.setIsClodWallet(c.getInt(c.getColumnIndex("isClodWallet")));
        }
        return walletBean;
    }

    /**
     * 判断数据是否存在
     * 判断是否为空的方法是 Cursor.getCount()这么一个简单的函数，如果是0，表示Cursor为空；如果非0，则表示Cursor不为空
     *
     * @param address
     * @return
     */
    public static boolean queryIsWallet(String address) {
        String sql = "select * from " + DBHelper.DATABASE_TABLE_NAME + " where address=?";
        Cursor cursor = db.rawQuery(sql, new String[]{address});
        if (cursor.getCount() >= 1) {//表示数据库已存在该数据
            return true;
        } else return false;
    }

    /**
     * 判断数据是否存在
     *
     * @param address
     * @return
     */
    public static boolean queryIsMapWallet(String address) {
        String sql = "select * from " + DBHelper.DATABASE_TABLE_NAME + " where address=? And walletType=?";
        Cursor cursor = db.rawQuery(sql, new String[]{address, "1"});
        if (cursor.getCount() >= 1) {//表示数据库已存在该数据
            return true;
        } else return false;
    }

    /**
     * 根据address查询某一行数据(肯定存在的)
     *
     * @param address
     * @return
     */
    public static WalletBean queryWallet(Context context, String address) {
        WalletBean walletBean = new WalletBean();
        String sql = "select * from " + DBHelper.DATABASE_TABLE_NAME + " where address=?";
        Cursor c = db.rawQuery(sql, new String[]{address});
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("walletName"));
            walletBean.setWalletBName(context.getResources().getString(R.string.wallet_db_hanle_04).equals(name) ? name + c.getInt(c.getColumnIndex("id")) : name);
            walletBean.setAddress(c.getString(c.getColumnIndex("address")));
            walletBean.setKeyStore(c.getString(c.getColumnIndex("keyStore")));
            walletBean.setMnemonic(c.getString(c.getColumnIndex("desZhujici")));
            walletBean.setImgId(c.getInt(c.getColumnIndex("imgId")));
            walletBean.setIsClodWallet(c.getInt(c.getColumnIndex("isClodWallet")));
            walletBean.setImgPath(c.getString(c.getColumnIndex("imgpath")));
        }
        return walletBean;
    }

    /**
     * 修改数据
     *
     * @param address
     * @param clumName 哪一个字段
     * @param content
     * @return
     */
    public static int updataWallet(String address, String clumName, String content) {
        try {
            String sql = "update " + DBHelper.DATABASE_TABLE_NAME + " set " + clumName + " = ? where address = ?";
            db.execSQL(sql, new String[]{content, address});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 修改数据
     *
     * @param address
     * @param clumName 哪一个字段
     * @param content
     * @return
     */
    public static int updataWallet(String address, String clumName, int content) {
        try {
            String sql = "update " + DBHelper.DATABASE_TABLE_NAME + " set " + clumName + " = ? where address = ?";
            db.execSQL(sql, new Object[]{content, address});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 删除钱包
     *
     * @param address
     * @return 0:成功 2：失败
     */
    public static int deleteWallet(String address, int imagId) {
        try {
            String sql = "delete from " + DBHelper.DATABASE_TABLE_NAME + " where address = ?";
            db.execSQL(sql, new String[]{address});
            updataUseStatus(imagId, "no");
            return 0;
        } catch (Exception e) {
            return 2;
        }

    }

    /**
     * 查询数据库中的总条数.
     *  
     *
     * @return 
     */
    public static int allCaseNum() {
        String sql = "select count(*)from " + DBHelper.DATABASE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = (int) cursor.getLong(0);
        cursor.close();
        return count;
    }

    /***************************************缓存数据*****************************************/

    /**
     * 插入需要缓存的数据
     *
     * @param key
     * @param value
     * @return 0-成功  2-失败
     */
    public static int insertHistory(String key, String value) {
        if (queryIsHistory(key)) {//已存在 修改
            updataHistory(key, value);
        } else {//不存在 插入
            //step1:开始事物
            db.beginTransaction();
            try {
                //step2:插入数据操作
                ContentValues values = new ContentValues();
                values.put("historyKey", key);
                values.put("historyValue", value);
                //执行插入操作
                db.insert(DBHelper.DATABASE_HISTORY_CACHE, null, values);
                db.setTransactionSuccessful();//设置事务成功完成
                return 0;
            } catch (Exception e) {
                return 2;
            } finally {
                db.endTransaction();//结束事务
            }
        }
        return 2;
    }

    /**
     * 根据key值 查询缓存的数据
     *
     * @param key
     * @return
     */
    public static String queryHistory(String key) {
        String result = "";
        String sql = "select * from " + DBHelper.DATABASE_HISTORY_CACHE + " where historyKey = ?";
        Cursor c = db.rawQuery(sql, new String[]{key});
        while (c.moveToNext()) {
            result = c.getString(c.getColumnIndex("historyValue"));
        }
        return result;
    }

    /**
     * 修改某条缓存数据
     *
     * @param key
     * @param value
     * @return
     */
    public static int updataHistory(String key, String value) {
        try {
            String sql = "update " + DBHelper.DATABASE_HISTORY_CACHE + " set historyValue" + " = ? where historyKey = ?";
            db.execSQL(sql, new String[]{value, key});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 删除某一条缓存
     *
     * @param key
     * @return 0:成功 2：失败
     */
    public static int deleteHistory(String key) {
        try {
            String sql = "delete from " + DBHelper.DATABASE_HISTORY_CACHE + " where historyKey = ?";
            db.execSQL(sql, new String[]{key});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 查询某条缓存数据是是否已创建
     *
     * @param key
     * @return
     */
    public static boolean queryIsHistory(String key) {
        String sql = "select * from " + DBHelper.DATABASE_HISTORY_CACHE + " where historyKey=?";
        Cursor cursor = db.rawQuery(sql, new String[]{key});
        if (cursor.getCount() >= 1) {//表示数据库已存在该数据
            return true;
        } else return false;
    }

    /***************************************图片使用情况*****************************************/
    /**
     * 查询数据库中的总条数.
     *  
     *
     * @return 
     */
    public static int queryAllImgNum() {
        String sql = "select count(*)from " + DBHelper.DATABASE_IMA_MANAGER;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = (int) cursor.getLong(0);
        cursor.close();
        return count;
    }

    public static int queryRandomNum() {
        int num = 0;
        //step1:先将数据库中状态为no的数据获取出来
        List<ImgUseBean> data = new ArrayList<>();
        String sql = "select * from " + DBHelper.DATABASE_IMA_MANAGER + " where isUse=?";
        Cursor cursor = db.rawQuery(sql, new String[]{"no"});
        while (cursor.moveToNext()) {
            ImgUseBean imgUseBean = new ImgUseBean();
            imgUseBean.setUseStatus(cursor.getString(cursor.getColumnIndex("isUse")));
            imgUseBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.add(imgUseBean);
        }
        cursor.close();
        //step2:获取数组大小
        int size = data.size();
        //step3:根据数组长度获取一个随机数
        Random random = new Random();
        int index = random.nextInt(size);
        //step4:根据随机数获取相对应的id
        num = data.get(index).getId();
        //step5:将相对应id的图片使用状态值 修改为yes
        int updataResult = updataUseStatus(num, "yes");
        //step6:修改数据成功 返回num 不成功再一次修改
        if (updataResult != 0) {
            updataUseStatus(num, "yes");
        }
        return num;
    }

    /**
     * 插入需要数据
     *
     * @return 0-成功  2-失败
     */
    public static int insertImageData() {
        //step1:开始事物
        db.beginTransaction();
        try {
            for (int i = 0; i < 10; i++) {
                //step2:插入数据操作
                ContentValues values = new ContentValues();
                values.put("id", i);
                values.put("isUse", "no");//是否使用 默认为no
                //执行插入操作
                db.insert(DBHelper.DATABASE_IMA_MANAGER, null, values);
            }
            db.setTransactionSuccessful();//设置事务成功完成
            return 0;
        } catch (Exception e) {
            return 2;
        } finally {
            db.endTransaction();//结束事务
        }
    }

    /**
     * 修改某条使用状态值数据
     *
     * @param key
     * @param value
     * @return
     */
    public static int updataUseStatus(int key, String value) {
        try {
            String sql = "update " + DBHelper.DATABASE_IMA_MANAGER + " set isUse" + " = ? where id = ?";
            db.execSQL(sql, new Object[]{value, key});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /***************************************绑定地址*****************************************/
    /**
     * 插入需要数据
     *
     * @return 0-成功  2-失败
     */
    public static int insertBandAddress(BandingAddressBean bandingAddressBean) {
        //先检测 数据库里面是否以后该数据
        if(queryIsBandAddress(bandingAddressBean.getMark(),bandingAddressBean.getAddressContact())){
            return 3;
        }
        //step1:开始事物
        db.beginTransaction();
        try {
            //step2:插入数据操作
            ContentValues values = new ContentValues();
            values.put("mark", bandingAddressBean.getMark());
            values.put("addressContact", bandingAddressBean.getAddressContact());
            //执行插入操作
            db.insert(DBHelper.DATABASE_DANB_ADDRESS, null, values);
            db.setTransactionSuccessful();//设置事务成功完成
            return 0;
        } catch (Exception e) {
            return 2;
        } finally {
            db.endTransaction();//结束事务
        }
    }

    /**
     * 判断数据是否存在
     *
     * @param name
     * @param address
     * @return
     */
    public static boolean queryIsBandAddress(String name ,String address) {
        String sql = "select * from " + DBHelper.DATABASE_DANB_ADDRESS + " where mark=? And addressContact=?";
        Cursor cursor = db.rawQuery(sql, new String[]{name, address});
        if (cursor.getCount() >= 1) {//表示数据库已存在该数据
            return true;
        } else return false;
    }

    /**
     * 根据id查询某一行数据(肯定存在的)
     *
     * @param id
     * @return
     */
    public static BandingAddressBean queryBandAddress(int id) {
        BandingAddressBean bandingAddressBean = new BandingAddressBean();
        String sql = "select * from " + DBHelper.DATABASE_DANB_ADDRESS + " where id=?";
        Cursor c = db.rawQuery(sql, new String[]{""+id});
        while (c.moveToNext()) {
            bandingAddressBean.setBId(c.getInt(c.getColumnIndex("id")));
            bandingAddressBean.setMark(c.getString(c.getColumnIndex("mark")));
            bandingAddressBean.setAddressContact(c.getString(c.getColumnIndex("addressContact")));
        }
        return bandingAddressBean;
    }

    /**
     * 更新所有数据
     * @param bandAddressData
     * @return
     */
    public static int insertAllBandAddress(List<BandingAddressBean> bandAddressData) {
        //step1:开始事物
        db.beginTransaction();
        try {
            //step2:插入数据操作
            BandingAddressBean bandingAddressBean;
            for (int i=0;i<bandAddressData.size();i++){
                bandingAddressBean = bandAddressData.get(i);
                if(!queryIsBandAddress(bandingAddressBean.getMark(),bandingAddressBean.getAddressContact())){
                    ContentValues values = new ContentValues();
                    values.put("mark", bandingAddressBean.getMark());
                    values.put("addressContact", bandingAddressBean.getAddressContact());
                    //执行插入操作
                    db.insert(DBHelper.DATABASE_DANB_ADDRESS, null, values);
                }
            }
            db.setTransactionSuccessful();//设置事务成功完成
            return 0;
        } catch (Exception e) {
            return 2;
        } finally {
            db.endTransaction();//结束事务
        }
    }

    /**
     * 查询所有的数据
     * @return
     */
    public static List<BandingAddressBean> queryAllBandAddress(){
        List<BandingAddressBean> data = new ArrayList<>();
        String sql = "select * from " + DBHelper.DATABASE_DANB_ADDRESS;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BandingAddressBean addressBean = new BandingAddressBean();
            addressBean.setAddressContact(cursor.getString(cursor.getColumnIndex("addressContact")));
            addressBean.setBId(cursor.getInt(cursor.getColumnIndex("id")));
            addressBean.setMark(cursor.getString(cursor.getColumnIndex("mark")));
            data.add(addressBean);
        }
        cursor.close();
        return data;
    }


    /**
     * 修改绑定地址数据
     * @param id
     * @param clumName
     * @param content
     * @return
     */
    public static int updateBandAddress(int id,String clumName, String content) {
        try {
            String sql = "update " + DBHelper.DATABASE_DANB_ADDRESS + " set " + clumName + " = ? where addressContact = ?";
            db.execSQL(sql, new Object[]{content, id});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 更新整条数据
     * @param id
     * @param name
     * @param address
     * @return
     */
    public static int updateColBandAddress(int id,String name, String address) {
        //先检测 数据库里面是否以后该数据
        if(queryIsBandAddress(name,address)){
            return 3;
        }
        try {
            String sql = "update " + DBHelper.DATABASE_DANB_ADDRESS + " set mark = ? , addressContact = ?  where id = ?";
            db.execSQL(sql, new Object[]{name,address,id});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 删除绑定数据
     * @param id
     * @return
     */
    public static int deleteBandAddress(int id){
        try {
            String sql = "delete from " + DBHelper.DATABASE_DANB_ADDRESS + " where id = ?";
            db.execSQL(sql, new Object[]{id});
            return 0;
        } catch (Exception e) {
            return 2;
        }
    }

    /***************************************授权绑定*****************************************/
    /**
     * 插入需要数据
     *
     * @return 0-成功  2-失败
     */
    public static int insertDappsAuth(String address,String authId) {
        //step1:开始事物
        db.beginTransaction();
        try {
            //step2:插入数据操作
            ContentValues values = new ContentValues();
            values.put("AuthAddress", address);
            values.put("authId", authId);
            //执行插入操作
            db.insert(DBHelper.DATABASE_DAPPS_AUTH, null, values);
            db.setTransactionSuccessful();//设置事务成功完成
            return 0;
        } catch (Exception e) {
            return 2;
        } finally {
            db.endTransaction();//结束事务
        }
    }

    /**
     * 查询某一项是否存在
     *
     * @return
     */
    public static boolean queryDappsAuth(String address,String authId) {
        String sql = "select * from " + DBHelper.DATABASE_DAPPS_AUTH + " where AuthAddress = ? And authId = ?";
        Cursor c = db.rawQuery(sql, new String[]{address,authId});
        if (c.getCount() >= 1) {//表示数据库已存在该数据
            return true;
        } else return false;
    }
}
