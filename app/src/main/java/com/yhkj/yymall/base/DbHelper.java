package com.yhkj.yymall.base;

import android.content.Context;


import com.vise.xsnow.database.DBManager;
import com.yhkj.yymall.bean.BaseConfig;
import com.yhkj.yymall.bean.RecnetSearchBean;
import com.yhkj.yymall.bean.UserConfig;
import com.yhkj.yymall.bean.db.DaoMaster;
import com.yhkj.yymall.bean.db.DaoSession;

import org.greenrobot.greendao.AbstractDao;


public class DbHelper {
    private static final String DB_NAME = "woosport.db";//数据库名称
    private static DbHelper instance;
    private static DBManager<RecnetSearchBean,Long> recnetSearchBeanLongDBManager;
    private static DBManager<UserConfig,Long> userConfigLongDBManager;
    private static DBManager<BaseConfig,Long> baseConfigLongDBManager;
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DbHelper() {

    }

    public static DbHelper getInstance() {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    instance = new DbHelper();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public void init(Context context, String dbName) {
        mHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }


    public DBManager<UserConfig, Long> userConfigLongDBManager() {
        if (userConfigLongDBManager == null) {
            userConfigLongDBManager = new DBManager<UserConfig, Long>(){
                @Override
                public AbstractDao<UserConfig, Long> getAbstractDao() {
                    return mDaoSession.getUserConfigDao();
                }
            };
        }
        return userConfigLongDBManager;
    }

    public DBManager<BaseConfig, Long> baseConfigLongDBManager(){
        if (baseConfigLongDBManager == null) {
            baseConfigLongDBManager = new DBManager<BaseConfig, Long>(){
                @Override
                public AbstractDao<BaseConfig, Long> getAbstractDao() {
                    return mDaoSession.getBaseConfigDao();
                }
            };
        }
        return baseConfigLongDBManager;
    }

    public DBManager<RecnetSearchBean, Long> recnetSearchBeanLongDBManager() {
        if (recnetSearchBeanLongDBManager == null) {
            recnetSearchBeanLongDBManager = new DBManager<RecnetSearchBean, Long>(){
                @Override
                public AbstractDao<RecnetSearchBean, Long> getAbstractDao() {
                    return mDaoSession.getRecnetSearchBeanDao();
                }
            };
        }
        return recnetSearchBeanLongDBManager;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void clear() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void close() {
        clear();
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }
}
