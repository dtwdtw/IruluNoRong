package com.wf.irulu.logic.listener;

/**
 * @描述: 会话数据变化监听
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.listener
 * @类名:SessionChangeListener
 * @作者: 左西杰
 * @创建时间:2015-6-2 下午5:56:39
 * 
 */

public interface TotalUnreadCountListener {

	/**
	 * 会话数据改变回调
	 */
	void totalUnreadCount(int totalUnreadCount);
}
