package com.wf.irulu.logic.dao.impl;

import com.wf.irulu.logic.dao.RecentSearchDao;
import com.wf.irulu.logic.manager.DaoManager;
import com.wf.irulu.logic.manager.DbManager;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.dao.impl
 * @类名:RecentSearchDaoImpl
 * @作者: Yuki
 * @创建时间:2015/11/16
 */
public class RecentSearchDaoImpl implements RecentSearchDao{

    private DbManager dbManager;
    private DaoManager daoManager;

    public RecentSearchDaoImpl(DaoManager daoManager){
        this.daoManager = daoManager;
        this.dbManager = daoManager.getDbManager();
    }

    @Override
    public ArrayList<String> searchAllKey() {
        return null;
    }

    @Override
    public boolean isContainedKey(String key) {
        return false;
    }

    @Override
    public boolean insertKey(String key) {
        return false;
    }

    @Override
    public boolean updateKey(String key) {
        return false;
    }

    @Override
    public boolean deleteKey(String key) {
        return false;
    }
}
