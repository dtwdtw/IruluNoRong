package com.wf.irulu.module.product.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/** 为了方便共用以前的adapter 才使用这个类一般情况建议使用scrollView
 * Created by iRULU on 2016/4/27.
 */
public class WrapHeightListView extends ListView {
    public WrapHeightListView(Context context) {
        super(context);
    }

    public WrapHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapHeightListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrapHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            if (height == 0) {
                heightMeasureSpec= MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
