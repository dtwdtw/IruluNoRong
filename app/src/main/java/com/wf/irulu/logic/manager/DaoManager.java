package com.wf.irulu.logic.manager;

import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.dao.RecentSearchDao;
import com.wf.irulu.logic.dao.impl.RecentSearchDaoImpl;

/**
 * @描述:  数据库操作管理类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.dao
 * @类名:DaoManager
 * @作者: 左西杰
 * @创建时间:2015-5-28 上午11:04:11
 * 
 */

public class DaoManager {

	private DbManager dbManager;
	private RecentSearchDao mRecentSearchDao;
	
	/**
	 * 构造方法
	 * @param controller
	 */
	public DaoManager(IruluController controller) {
		this.dbManager = new DbManager(controller);
		this.mRecentSearchDao = new RecentSearchDaoImpl(this);
	}

	/***************set、get方法*********************/

	public DbManager getDbManager() {
		return dbManager;
	}

	public RecentSearchDao getRecentSearchDao() {
		return mRecentSearchDao;
	}
}
