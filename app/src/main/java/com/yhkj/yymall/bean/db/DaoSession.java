package com.yhkj.yymall.bean.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.yhkj.yymall.bean.BaseConfig;
import com.yhkj.yymall.bean.RecnetSearchBean;
import com.yhkj.yymall.bean.UserConfig;

import com.yhkj.yymall.bean.db.BaseConfigDao;
import com.yhkj.yymall.bean.db.RecnetSearchBeanDao;
import com.yhkj.yymall.bean.db.UserConfigDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig baseConfigDaoConfig;
    private final DaoConfig recnetSearchBeanDaoConfig;
    private final DaoConfig userConfigDaoConfig;

    private final BaseConfigDao baseConfigDao;
    private final RecnetSearchBeanDao recnetSearchBeanDao;
    private final UserConfigDao userConfigDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        baseConfigDaoConfig = daoConfigMap.get(BaseConfigDao.class).clone();
        baseConfigDaoConfig.initIdentityScope(type);

        recnetSearchBeanDaoConfig = daoConfigMap.get(RecnetSearchBeanDao.class).clone();
        recnetSearchBeanDaoConfig.initIdentityScope(type);

        userConfigDaoConfig = daoConfigMap.get(UserConfigDao.class).clone();
        userConfigDaoConfig.initIdentityScope(type);

        baseConfigDao = new BaseConfigDao(baseConfigDaoConfig, this);
        recnetSearchBeanDao = new RecnetSearchBeanDao(recnetSearchBeanDaoConfig, this);
        userConfigDao = new UserConfigDao(userConfigDaoConfig, this);

        registerDao(BaseConfig.class, baseConfigDao);
        registerDao(RecnetSearchBean.class, recnetSearchBeanDao);
        registerDao(UserConfig.class, userConfigDao);
    }
    
    public void clear() {
        baseConfigDaoConfig.clearIdentityScope();
        recnetSearchBeanDaoConfig.clearIdentityScope();
        userConfigDaoConfig.clearIdentityScope();
    }

    public BaseConfigDao getBaseConfigDao() {
        return baseConfigDao;
    }

    public RecnetSearchBeanDao getRecnetSearchBeanDao() {
        return recnetSearchBeanDao;
    }

    public UserConfigDao getUserConfigDao() {
        return userConfigDao;
    }

}
