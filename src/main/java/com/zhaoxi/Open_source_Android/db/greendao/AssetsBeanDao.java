package com.zhaoxi.Open_source_Android.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ASSETS_BEAN".
*/
public class AssetsBeanDao extends AbstractDao<AssetsBean, Long> {

    public static final String TABLENAME = "ASSETS_BEAN";

    /**
     * Properties of entity AssetsBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Ids = new Property(0, Long.class, "ids", true, "_id");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property TokenSymbol = new Property(2, String.class, "tokenSymbol", false, "TOKEN_SYMBOL");
        public final static Property TokenSymbolImageUrl = new Property(3, String.class, "tokenSymbolImageUrl", false, "TOKEN_SYMBOL_IMAGE_URL");
        public final static Property TokenName = new Property(4, String.class, "tokenName", false, "TOKEN_NAME");
        public final static Property ContractCreater = new Property(5, String.class, "contractCreater", false, "CONTRACT_CREATER");
        public final static Property ContractAddress = new Property(6, String.class, "contractAddress", false, "CONTRACT_ADDRESS");
        public final static Property TokenTotalSupply = new Property(7, String.class, "tokenTotalSupply", false, "TOKEN_TOTAL_SUPPLY");
        public final static Property ContractType = new Property(8, String.class, "contractType", false, "CONTRACT_TYPE");
        public final static Property TransfersNum = new Property(9, String.class, "transfersNum", false, "TRANSFERS_NUM");
        public final static Property CnyPrice = new Property(10, String.class, "cnyPrice", false, "CNY_PRICE");
        public final static Property UsdPrice = new Property(11, String.class, "usdPrice", false, "USD_PRICE");
        public final static Property CnyTotalValue = new Property(12, String.class, "cnyTotalValue", false, "CNY_TOTAL_VALUE");
        public final static Property UsdTotalValue = new Property(13, String.class, "usdTotalValue", false, "USD_TOTAL_VALUE");
        public final static Property IsSelected = new Property(14, boolean.class, "isSelected", false, "IS_SELECTED");
        public final static Property BalanceOfToken = new Property(15, String.class, "balanceOfToken", false, "BALANCE_OF_TOKEN");
        public final static Property Decimals = new Property(16, int.class, "decimals", false, "DECIMALS");
    }


    public AssetsBeanDao(DaoConfig config) {
        super(config);
    }
    
    public AssetsBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ASSETS_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ids
                "\"ID\" TEXT," + // 1: id
                "\"TOKEN_SYMBOL\" TEXT," + // 2: tokenSymbol
                "\"TOKEN_SYMBOL_IMAGE_URL\" TEXT," + // 3: tokenSymbolImageUrl
                "\"TOKEN_NAME\" TEXT," + // 4: tokenName
                "\"CONTRACT_CREATER\" TEXT," + // 5: contractCreater
                "\"CONTRACT_ADDRESS\" TEXT," + // 6: contractAddress
                "\"TOKEN_TOTAL_SUPPLY\" TEXT," + // 7: tokenTotalSupply
                "\"CONTRACT_TYPE\" TEXT," + // 8: contractType
                "\"TRANSFERS_NUM\" TEXT," + // 9: transfersNum
                "\"CNY_PRICE\" TEXT," + // 10: cnyPrice
                "\"USD_PRICE\" TEXT," + // 11: usdPrice
                "\"CNY_TOTAL_VALUE\" TEXT," + // 12: cnyTotalValue
                "\"USD_TOTAL_VALUE\" TEXT," + // 13: usdTotalValue
                "\"IS_SELECTED\" INTEGER NOT NULL ," + // 14: isSelected
                "\"BALANCE_OF_TOKEN\" TEXT," + // 15: balanceOfToken
                "\"DECIMALS\" INTEGER NOT NULL );"); // 16: decimals
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ASSETS_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AssetsBean entity) {
        stmt.clearBindings();
 
        Long ids = entity.getIds();
        if (ids != null) {
            stmt.bindLong(1, ids);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String tokenSymbol = entity.getTokenSymbol();
        if (tokenSymbol != null) {
            stmt.bindString(3, tokenSymbol);
        }
 
        String tokenSymbolImageUrl = entity.getTokenSymbolImageUrl();
        if (tokenSymbolImageUrl != null) {
            stmt.bindString(4, tokenSymbolImageUrl);
        }
 
        String tokenName = entity.getTokenName();
        if (tokenName != null) {
            stmt.bindString(5, tokenName);
        }
 
        String contractCreater = entity.getContractCreater();
        if (contractCreater != null) {
            stmt.bindString(6, contractCreater);
        }
 
        String contractAddress = entity.getContractAddress();
        if (contractAddress != null) {
            stmt.bindString(7, contractAddress);
        }
 
        String tokenTotalSupply = entity.getTokenTotalSupply();
        if (tokenTotalSupply != null) {
            stmt.bindString(8, tokenTotalSupply);
        }
 
        String contractType = entity.getContractType();
        if (contractType != null) {
            stmt.bindString(9, contractType);
        }
 
        String transfersNum = entity.getTransfersNum();
        if (transfersNum != null) {
            stmt.bindString(10, transfersNum);
        }
 
        String cnyPrice = entity.getCnyPrice();
        if (cnyPrice != null) {
            stmt.bindString(11, cnyPrice);
        }
 
        String usdPrice = entity.getUsdPrice();
        if (usdPrice != null) {
            stmt.bindString(12, usdPrice);
        }
 
        String cnyTotalValue = entity.getCnyTotalValue();
        if (cnyTotalValue != null) {
            stmt.bindString(13, cnyTotalValue);
        }
 
        String usdTotalValue = entity.getUsdTotalValue();
        if (usdTotalValue != null) {
            stmt.bindString(14, usdTotalValue);
        }
        stmt.bindLong(15, entity.getIsSelected() ? 1L: 0L);
 
        String balanceOfToken = entity.getBalanceOfToken();
        if (balanceOfToken != null) {
            stmt.bindString(16, balanceOfToken);
        }
        stmt.bindLong(17, entity.getDecimals());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AssetsBean entity) {
        stmt.clearBindings();
 
        Long ids = entity.getIds();
        if (ids != null) {
            stmt.bindLong(1, ids);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String tokenSymbol = entity.getTokenSymbol();
        if (tokenSymbol != null) {
            stmt.bindString(3, tokenSymbol);
        }
 
        String tokenSymbolImageUrl = entity.getTokenSymbolImageUrl();
        if (tokenSymbolImageUrl != null) {
            stmt.bindString(4, tokenSymbolImageUrl);
        }
 
        String tokenName = entity.getTokenName();
        if (tokenName != null) {
            stmt.bindString(5, tokenName);
        }
 
        String contractCreater = entity.getContractCreater();
        if (contractCreater != null) {
            stmt.bindString(6, contractCreater);
        }
 
        String contractAddress = entity.getContractAddress();
        if (contractAddress != null) {
            stmt.bindString(7, contractAddress);
        }
 
        String tokenTotalSupply = entity.getTokenTotalSupply();
        if (tokenTotalSupply != null) {
            stmt.bindString(8, tokenTotalSupply);
        }
 
        String contractType = entity.getContractType();
        if (contractType != null) {
            stmt.bindString(9, contractType);
        }
 
        String transfersNum = entity.getTransfersNum();
        if (transfersNum != null) {
            stmt.bindString(10, transfersNum);
        }
 
        String cnyPrice = entity.getCnyPrice();
        if (cnyPrice != null) {
            stmt.bindString(11, cnyPrice);
        }
 
        String usdPrice = entity.getUsdPrice();
        if (usdPrice != null) {
            stmt.bindString(12, usdPrice);
        }
 
        String cnyTotalValue = entity.getCnyTotalValue();
        if (cnyTotalValue != null) {
            stmt.bindString(13, cnyTotalValue);
        }
 
        String usdTotalValue = entity.getUsdTotalValue();
        if (usdTotalValue != null) {
            stmt.bindString(14, usdTotalValue);
        }
        stmt.bindLong(15, entity.getIsSelected() ? 1L: 0L);
 
        String balanceOfToken = entity.getBalanceOfToken();
        if (balanceOfToken != null) {
            stmt.bindString(16, balanceOfToken);
        }
        stmt.bindLong(17, entity.getDecimals());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AssetsBean readEntity(Cursor cursor, int offset) {
        AssetsBean entity = new AssetsBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ids
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // tokenSymbol
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // tokenSymbolImageUrl
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // tokenName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // contractCreater
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // contractAddress
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // tokenTotalSupply
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // contractType
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // transfersNum
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // cnyPrice
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // usdPrice
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // cnyTotalValue
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // usdTotalValue
            cursor.getShort(offset + 14) != 0, // isSelected
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // balanceOfToken
            cursor.getInt(offset + 16) // decimals
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AssetsBean entity, int offset) {
        entity.setIds(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTokenSymbol(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTokenSymbolImageUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTokenName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContractCreater(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContractAddress(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTokenTotalSupply(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setContractType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTransfersNum(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCnyPrice(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setUsdPrice(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCnyTotalValue(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setUsdTotalValue(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setIsSelected(cursor.getShort(offset + 14) != 0);
        entity.setBalanceOfToken(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setDecimals(cursor.getInt(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AssetsBean entity, long rowId) {
        entity.setIds(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AssetsBean entity) {
        if(entity != null) {
            return entity.getIds();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AssetsBean entity) {
        return entity.getIds() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
