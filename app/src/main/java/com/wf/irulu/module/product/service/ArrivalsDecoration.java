package com.wf.irulu.module.product.service;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by iRULU on 2016/4/29.
 */
public class ArrivalsDecoration extends RecyclerView.ItemDecoration {
    private int horizonSpaceLeft = 0;
    private int horizonSpaceRight = 0;
    private int verticalSpace = 0;

    private int left = 0;
    private int right = 0;

    public ArrivalsDecoration(int left, int right, int top) {
        this.horizonSpaceLeft = left;
        this.horizonSpaceRight = right;
        this.verticalSpace = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int postion = parent.getChildAdapterPosition(view);
        GridLayoutManager vGridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = vGridLayoutManager.getSpanCount();
        int childCount = parent.getAdapter().getItemCount();
        if (postion == 0) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (postion == childCount - 1) {
            outRect.set(0, verticalSpace, 0, 0);
            return;
        }
        int columnIndex = (postion - 1) % spanCount;
        if (columnIndex == 0) {
            left = horizonSpaceLeft;
            right = horizonSpaceRight;

        } else {
            left = horizonSpaceRight;
            right = horizonSpaceLeft;

        }
        outRect.set(left, verticalSpace, right, 0);
    }

}
