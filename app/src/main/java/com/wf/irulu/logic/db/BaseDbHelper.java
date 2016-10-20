package com.wf.irulu.logic.db;

import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.logic.IruluController;

/**
 * 
 * @描述: TODO
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.db
 * @类名:BaseDbHelper
 * @作者: 左西杰
 * @创建时间:2015-6-3 上午10:28:59
 *
 */
public class BaseDbHelper {
	
	public LoginUser getLoginUser() {
		return IruluController.getInstance().getCacheManager().getLoginUser();
	}
	
}
