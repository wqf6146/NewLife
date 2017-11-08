package com.yhkj.yymall.bean.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.yhkj.yymall.bean.UserConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_CONFIG".
*/
public class UserConfigDao extends AbstractDao<UserConfig, Long> {

    public static final String TABLENAME = "USER_CONFIG";

    /**
     * Properties of entity UserConfig.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Token = new Property(1, String.class, "token", false, "TOKEN");
        public final static Property State = new Property(2, Boolean.class, "state", false, "STATE");
        public final static Property Phone = new Property(3, String.class, "phone", false, "PHONE");
    }


    public UserConfigDao(DaoConfig config) {
        super(config);
    }
    
    public UserConfigDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_CONFIG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TOKEN\" TEXT," + // 1: token
                "\"STATE\" INTEGER," + // 2: state
                "\"PHONE\" TEXT);"); // 3: phone
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_CONFIG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserConfig entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(2, token);
        }
 
        Boolean state = entity.getState();
        if (state != null) {
            stmt.bindLong(3, state ? 1L: 0L);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserConfig entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(2, token);
        }
 
        Boolean state = entity.getState();
        if (state != null) {
            stmt.bindLong(3, state ? 1L: 0L);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserConfig readEntity(Cursor cursor, int offset) {
        UserConfig entity = new UserConfig( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // token
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // state
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // phone
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserConfig entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setToken(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setState(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserConfig entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserConfig entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserConfig entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
