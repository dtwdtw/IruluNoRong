package com.wf.irulu.module.product.service;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wf.irulu.common.utils.ILog;

/**
 * 仅限于GridLayoutManager
 * Created by XImoon on 15/11/6.
 */
public class GridDecoration extends RecyclerView.ItemDecoration {

    /**
     * item间隔
     */
    private int mVerticalSpace = 0;
    private int mHorizontalSpace = 0;


    public GridDecoration(int verticalSpacePx, int horizontalSpacePx) {
        mVerticalSpace = verticalSpacePx;
        mHorizontalSpace = horizontalSpacePx;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 设置左右间隔
        int left = 0, right = 0, top = 0, bottom = 0;

        top = mVerticalSpace / 2;
        bottom = mVerticalSpace / 2;

        ILog.d("TB", "top=" + top);
        left = mHorizontalSpace / 2;
        right = mHorizontalSpace / 2;
        outRect.set(left, top, right, bottom);


    }


}
