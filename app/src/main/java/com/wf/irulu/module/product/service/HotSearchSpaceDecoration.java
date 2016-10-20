package com.wf.irulu.module.product.service;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.service
 * @类名:HotSearchSpaceDecoration
 * @作者: Yuki
 * @创建时间:2015/11/16
 */
public class HotSearchSpaceDecoration  extends RecyclerView.ItemDecoration {

    /** item间隔*/
    private int space;

    public HotSearchSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state) {
        // 设置左右间隔
        outRect.left = space;
        if (parent.getChildAdapterPosition(view) == parent.getChildCount() - 1){
            outRect.right = space;
        }else{
            outRect.right = 0;
        }
    }
}
