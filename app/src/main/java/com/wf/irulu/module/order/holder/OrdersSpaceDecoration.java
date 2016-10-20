package com.wf.irulu.module.order.holder;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dtw on 16/3/10.
 */
public class OrdersSpaceDecoration extends RecyclerView.ItemDecoration  {
    /** item间隔*/
    private int mSpace;

    public OrdersSpaceDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state) {
        // 设置上下间隔

        outRect.top = 0;
        outRect.bottom = mSpace;
    }
}
