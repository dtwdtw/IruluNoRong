package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by iRULU on 2016/5/2.
 */
public class MatchParentImageView extends ImageView {
    public MatchParentImageView(Context context) {
        super(context);
    }

    public MatchParentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup vViewGroup= (ViewGroup) getParent();
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(vViewGroup.getMeasuredHeight(),MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
