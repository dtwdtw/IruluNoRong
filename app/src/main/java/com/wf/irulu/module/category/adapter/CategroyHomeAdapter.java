package com.wf.irulu.module.category.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductCategoryInformationBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.category.holder.CategoryHomeHolder;
import com.wf.irulu.module.category.onclick.CategoryHomeOnClick;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/17.
 */
public class CategroyHomeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductCategoryInformationBean> list;

    public CategroyHomeAdapter(Context context, ArrayList<ProductCategoryInformationBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (list != null) {
            return list.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CategoryHomeHolder holder;
        if (view == null) {
            holder = new CategoryHomeHolder();
            view = View.inflate(mContext, R.layout.item_category_home_lv, null);
            holder.iv = (ImageView) view.findViewById(R.id.item_category_home_iv);
            view.setTag(holder);
        } else {
            holder = (CategoryHomeHolder) view.getTag();
        }
        ProductCategoryInformationBean pb = list.get(i);
        holder.iv.setMinimumHeight((ConstantsUtils.DISPLAYW - UIUtils.dip2px(20))/3);

        IruluApplication.getInstance().getPicasso().load(pb.getIcon() + "?imageView2/0/w/" + (ConstantsUtils.DISPLAYW - UIUtils.dip2px(20)) + "/h/" + (ConstantsUtils.DISPLAYW - UIUtils.dip2px(20)) / 3 + "/interlace/1/q/75").placeholder(R.mipmap.notify_image_xiaotu).resize(ConstantsUtils.DISPLAYW - UIUtils.dip2px(20), (ConstantsUtils.DISPLAYW - UIUtils.dip2px(20)) / 3).centerCrop().into(holder.iv);
        holder.iv.setOnClickListener(new CategoryHomeOnClick(mContext,pb.getName(),pb.getId()));
        return view;
    }
}
