package com.wf.irulu.common.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/4/25 下午3:29
 * 用于设置RecyclerView item的间距
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration{

    private int left;
    private int top;
    private int right;
    private int bottom;

    public CustomItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(left,top,right,bottom);
    }
}
