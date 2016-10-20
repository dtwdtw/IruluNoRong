package com.wf.irulu.module.category.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.CategoryDataBean;
import com.wf.irulu.common.bean.CategoryDataListBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.category.holder.SmartDeviceHolder;
import com.wf.irulu.module.category.onclick.SmartDeviceOnclick;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/19.
 */
public class SmartDeviceAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CategoryDataListBean> list;
    private CategoryDataBean cdb;

    public SmartDeviceAdapter(Context context, CategoryDataBean cdb) {
        this.mContext = context;
        this.cdb = cdb;
        list = cdb.getList();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (list != null) {
            return list.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SmartDeviceHolder holder;
        if (view == null) {
            holder = new SmartDeviceHolder();
            view = View.inflate(mContext, R.layout.item_category_smart_device_lv, null);
            holder.title = (TextView) view.findViewById(R.id.item_category_smart_device_title_tv);
            holder.rl = (RelativeLayout) view.findViewById(R.id.item_category_sd_rl);
            holder.rv = (RecyclerView) view.findViewById(R.id.item_category_sd_rlv);
            view.setTag(holder);
        } else {
            holder = (SmartDeviceHolder) view.getTag();
        }
        CategoryDataListBean db = list.get(i);

        holder.title.setText(db.getName());

        holder.rl.setOnClickListener(new SmartDeviceOnclick(mContext, db));

        holder.rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        holder.rv.setLayoutManager(lm);
        holder.rv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConstantsUtils.DISPLAYW/4+ UIUtils.dip2px(40)));
        holder.rv.setAdapter(new RecyclerAdapter(mContext, db.getList()));
        return view;
    }
}
