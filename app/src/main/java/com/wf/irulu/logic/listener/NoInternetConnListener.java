package com.wf.irulu.logic.listener;

/**
 * @描述: 无网络连接回调监听器
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.listener
 * @类名:NoInternetListener
 * @作者: 左西杰
 * @创建时间:2015-8-31 下午2:55:53
 * 
 */
public interface NoInternetConnListener {

	/**
	 * 无网络连接监听方法
	 * @param b true 为监听到无网络连接状态
	 */
	void noInternetConn(boolean b);
}
