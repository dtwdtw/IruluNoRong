package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;

import java.util.HashMap;

public class FlowLayout extends ViewGroup {

    private float mVerticalSpacing; //每个item纵向间距
    private float mHorizontalSpacing; //每个item横向间距
    private int mMinimumWidth = 0;
    private FlowLayoutAdapter mAdapter;

    public FlowLayout(Context context) {
        super(context);
        this.mMinimumWidth = (ConstantsUtils.DISPLAYW - context.getResources().getDimensionPixelSize(R.dimen.product_margin_box_01) * 5) / 4;

    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMinimumWidth = (ConstantsUtils.DISPLAYW - context.getResources().getDimensionPixelSize(R.dimen.product_margin_box_01) * 5) / 4;

    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMinimumWidth = (ConstantsUtils.DISPLAYW - context.getResources().getDimensionPixelSize(R.dimen.product_margin_box_01) * 5) / 4;
    }

    public void setHorizontalSpacing(float pixelSize) {
        mHorizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(float pixelSize) {
        mVerticalSpacing = pixelSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int selfWidth = resolveSize(0, widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;

        //通过计算每一个子控件的高度，得到自己的高度
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);
            childView.setMinimumWidth(mMinimumWidth);
            LayoutParams childLayoutParams = childView.getLayoutParams();
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight,
                            childLayoutParams.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom,
                            childLayoutParams.height));
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            if (childWidth < mMinimumWidth){
                childView.setMinimumWidth(mMinimumWidth);
            }
            lineHeight = Math.max(childHeight, lineHeight);

            if ((childLeft + childWidth + paddingRight) > selfWidth) {
                childLeft = paddingLeft + childWidth + (int)mHorizontalSpacing;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            } else {
                childLeft += childWidth + mHorizontalSpacing;
            }
        }
        int wantedHeight = childTop + lineHeight + paddingBottom;
        setMeasuredDimension(selfWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int myWidth = r - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        //根据子控件的宽高，计算子控件应该出现的位置。
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            }
            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mHorizontalSpacing;
        }
    }

    public void setAdapter(FlowLayoutAdapter adapter){
        this.mAdapter = adapter;
        mAdapter.init();
    }

    public interface FlowLayoutAdapter{
        void init();
        void refresh();
        void setChoosed(HashMap<String,String> params);
    }
}