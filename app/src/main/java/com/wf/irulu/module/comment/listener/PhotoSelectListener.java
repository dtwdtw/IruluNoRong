package com.wf.irulu.module.comment.listener;

/**
 * 
 * @描述: TODO
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.comment.service
 * @类名:PhotoSelectService
 * @作者: Yuki
 * @创建时间:2015-8-18 下午7:29:03
 *
 */
public interface PhotoSelectListener {

	/**
	 * 加入图片
	 */
	void selectPics();
	
	/**
	 * 全屏显示图片
	 * @param
	 */
	void fullScreenShow(int location);
	
	/**
	 * 获取选中内容 
	 * @param position 选中条目位置
	 */
	void selectDefaultComment(int position);
}
