package com.wf.irulu.logic.manager;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.wf.irulu.common.utils.LogUtils;

import java.util.Stack;

/**
 * @描述: 页面管理类
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.logic
 * @类名:PageManager
 * @作者: 左西杰
 * @创建时间:2015-5-27 上午10:54:19
 * 
 */

public class PageManager {

	private Stack<Object> pageStack;

	public PageManager() {
		pageStack = new Stack<Object>();
	}

	/**
	 * 移除页面对象
	 * 
	 * @param obj
	 *            页面对象
	 */
	public void removePage(Object obj) {
		if (obj != null) {
			if (obj instanceof Activity) {
				if(!((Activity) obj).isFinishing()) {
					((Activity) obj).finish();
				}
			} else if (obj instanceof FragmentActivity) {
				if(!((FragmentActivity) obj).isFinishing()) {
					((FragmentActivity) obj).finish();
				}

			}
			pageStack.remove(obj);
		}
	}

	/**
	 * 添加新页面
	 * 
	 * @param obj
	 *            页面对象
	 */
	public void addPage(Object obj) {
		if (!pageStack.contains(obj)) {
			pageStack.add(obj);
			LogUtils.d("PageManager.addPage 添加新页面:" + obj.getClass().getName());
		} else {
			LogUtils.d("PageManager.addPage 页面已存在");
		}
	}

	/**
	 * 清理页面
	 */
	public void clearPage() {
		int size = pageStack.size();
		for (int i = 0; i < size; i++) {
			Object obj = pageStack.get(i);
			if (obj instanceof Activity) {
				((Activity) obj).finish();
			} else if (obj instanceof FragmentActivity) {
				((FragmentActivity) obj).finish();
			}
		}
		pageStack.clear();
	}

}
