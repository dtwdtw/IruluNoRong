package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @描述: 适应嵌套ListView的效果
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.view
 * @类名:NoScrollListView
 * @作者: 左杰
 * @创建时间:2015/10/30 15:38
 */
public class NoScrollListView extends ListView{

    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写该方法，达到适应嵌套ListView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
