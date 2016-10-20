package com.wf.irulu.module.category.onclick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wf.irulu.common.bean.CategoryDataListBean;
import com.wf.irulu.common.bean.CategoryDataSortBean;
import com.wf.irulu.module.category.activity.CategoryPhoneActivity;
import com.wf.irulu.module.category.activity.CategorySmartDeviceActivity;
import com.wf.irulu.module.category.holder.SmartDeviceHolder;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/19.
 */
public class SmartDeviceOnclick implements View.OnClickListener {
    private Context context;
    private CategoryDataListBean db;

    public SmartDeviceOnclick(Context context, CategoryDataListBean db) {
        this.context = context;
        this.db = db;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(context, CategoryPhoneActivity.class);
        i.putExtra("id",db.getCateId());
        i.putExtra("name",db.getName());
        context.startActivity(i);
    }
}
