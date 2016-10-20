package com.wf.irulu.module.comment.service;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.comment.service
 * @类名:CustomerReviewsSpace
 * @作者: Yuki
 * @创建时间:2015/11/25
 */
public class CustomerReviewsSpaceDecoration extends RecyclerView.ItemDecoration {

    /** item间隔*/
    private int mSpace;

    public CustomerReviewsSpaceDecoration() {
        mSpace = 10;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state) {
        // 设置左右间隔
        outRect.left = 0;
        outRect.right = mSpace;
        // 最后一个设置无右边距
        if (parent.getChildAdapterPosition(view) == parent.getChildCount() - 1){
        }else{
            outRect.right = 0;
        }
    }
}
