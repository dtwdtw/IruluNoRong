package com.wf.irulu.common.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 
 * @描述:  请求父容器不拦截touch的viewPager
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.view
 * @类名:HorizontalViewPager
 * @作者: 左西杰
 * @创建时间:2015-7-4 下午2:49:20
 *
 */
public class HorizontalViewPager extends ViewPager {

	private int downX;
	private int downY;

	public HorizontalViewPager(Context context) {
		super(context);
	}

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();

			int diffX = downX - moveX;
			int diffY = downY - moveY;

			if(Math.abs(diffX) > Math.abs(diffY)) {
				if(getCurrentItem() == 0 && diffX < 0) {
					// 当前viewpager是第一个图片, 并且是从左向右拉动, 可以允许父类拦截事件
					getParent().requestDisallowInterceptTouchEvent(false); // false 让父控件拦截当前事件
				} else if((getCurrentItem() == (getAdapter().getCount() -1)) && diffX > 0) {
					// 如果viewpager显示的是最后一张图片, 并且是从右向左拉动, 可以允许父类拦截事件的.
					getParent().requestDisallowInterceptTouchEvent(false); // false 让父控件拦截当前事件
				} else {
					getParent().requestDisallowInterceptTouchEvent(true); // true 不让父控件拦截当前事件
				}
			} else {
				getParent().requestDisallowInterceptTouchEvent(false); // false 让父控件拦截当前事件
			}

			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}
}
