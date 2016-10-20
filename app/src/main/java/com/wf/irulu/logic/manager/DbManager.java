package com.wf.irulu.logic.manager;

import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.db.RecentSearchDbHelper;

/**
 * @描述: 数据库表管理类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.db
 * @类名:DbManager
 * @作者: 左西杰
 * @创建时间:2015-5-28 上午10:51:06
 * 
 */

public class DbManager {

	private RecentSearchDbHelper mRecentSearchDbHelper;

	public DbManager(IruluController controller) {
		mRecentSearchDbHelper = new RecentSearchDbHelper(controller);
	}

	public RecentSearchDbHelper getRecentSearchDbHelper() {
		return mRecentSearchDbHelper;
	}
}
