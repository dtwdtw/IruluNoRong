package com.wf.irulu.module.category.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.R;

/**
 * Created by daniel on 2015/11/19.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    public ImageView iv;
    public TextView tv;
    public LinearLayout ll;

    public RecyclerHolder(View v) {
        super(v);
        ll = (LinearLayout) v.findViewById(R.id.item_category_sd_ll);
        iv = (ImageView) v.findViewById(R.id.item_category_sd_rlv_iv);
        tv = (TextView)v.findViewById(R.id.item_category_sd_rlv_tv);
    }

}
