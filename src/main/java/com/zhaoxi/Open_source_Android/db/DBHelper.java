package com.zhaoxi.Open_source_Android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 51973 on 2018/5/23.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static volatile DBHelper mInstance = null;

    public static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBHelper.class) {
                if (mInstance == null) {
                    mInstance = new DBHelper(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private static final String DATABASE_NAME = "Open_source_Android.db";
    private static final int DATABASE_VERSION = 5;
    public static final String DATABASE_TABLE_NAME = "wallet";
    public static final String DATABASE_HISTORY_CACHE = "historycache";
    public static final String DATABASE_IMA_MANAGER = "imgmanager";
    public static final String DATABASE_DANB_ADDRESS = "bandingaddress";
    public static final String DATABASE_DAPPS_AUTH = "dappsauth";

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS wallet (id integer primary key autoincrement, address varchar(100)," +
                " keyStore text, walletName varchar(20), desZhujici text,psdprompt text,imgId integer," +
                "walletType varchar(20), isClodWallet integer default 0, imgpath text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS historycache (id integer primary key autoincrement, historyKey varchar(100), historyValue text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS imgmanager (id integer primary key autoincrement, isUse varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS bandingaddress (id integer primary key autoincrement, mark varchar(20), addressContact text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dappsauth (id integer primary key autoincrement, AuthAddress varchar(100), authId text)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS imgmanager (id integer primary key autoincrement, isUse varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS bandingaddress (id integer primary key autoincrement, mark varchar(20), addressContact text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dappsauth (id integer primary key autoincrement, AuthAddress varchar(100), authId text)");
        switch (newVersion){
            case 4:
                db.execSQL("ALTER TABLE wallet ADD COLUMN isClodWallet integer default 0");
                break;
            case 5:
                if(oldVersion < 4){
                    db.execSQL("ALTER TABLE wallet ADD COLUMN isClodWallet integer default 0");
                }
                db.execSQL("ALTER TABLE wallet ADD COLUMN imgpath text");
                break;
        }
    }
}
