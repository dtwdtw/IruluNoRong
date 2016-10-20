package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @描述: 可覆盖头部滑动布局
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.view
 * @类名:CoveredTitleScrollView
 * @作者: Yuki
 * @创建时间:2015/10/28
 */
public class CoveredTitleScrollView extends ScrollView{

    private onScrolledListener mListener = new onScrolledListener(){

        @Override
        public void scrolled(int t, int scrollY) {

        }
    };

    public void setOnScrolledListener(onScrolledListener pListener){
        this.mListener = pListener;
    }

    public CoveredTitleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoveredTitleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CoveredTitleScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mListener.scrolled(t,getScrollY());
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface onScrolledListener{
        /**
         *
         * @param pT    新的滑动距离 (Deprecated)
         * @param pScrollY  ScrollView 滑动的纵坐标
         */
        void scrolled(int pT,int pScrollY);
    }
}
