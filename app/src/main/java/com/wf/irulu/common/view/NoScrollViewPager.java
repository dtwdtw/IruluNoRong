package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * @描述: 不可以滑动的ViewPager(不然主界面可以滑动)
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.view
 * @类名:NoScrollViewPager
 * @作者: 左西杰
 * @创建时间:2015-7-2 下午3:32:50
 *
 */
public class NoScrollViewPager extends LazyViewPager{

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //不拦截
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//不消费
    }

}
