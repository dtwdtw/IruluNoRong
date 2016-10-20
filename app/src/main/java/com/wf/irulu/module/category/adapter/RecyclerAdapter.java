package com.wf.irulu.module.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.CategoryProductListBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.category.activity.CategoryPhoneActivity;
import com.wf.irulu.module.category.activity.CategorySmartDeviceActivity;
import com.wf.irulu.module.category.holder.RecyclerHolder;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/19.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {
    private Context context;
    private ArrayList<CategoryProductListBean> list;

    public RecyclerAdapter(Context context, ArrayList<CategoryProductListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.item_category_sd_rl, null);
        RecyclerHolder rh = new RecyclerHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        holder.iv.setMinimumHeight(ConstantsUtils.DISPLAYW/4);
        holder.iv.setMinimumWidth(ConstantsUtils.DISPLAYW/4);
        IruluApplication.getInstance().getPicasso().load(list.get(position).getImage()+"?imageView2/0/w/" + ConstantsUtils.DISPLAYW/4 + "/h/" + ConstantsUtils.DISPLAYW/4 + "/interlace/1/q/75").placeholder(R.mipmap.notify_image_xiaotu).resize(ConstantsUtils.DISPLAYW/4,ConstantsUtils.DISPLAYW/4).into(holder.iv);

        holder.tv.setText(StringUtils.getPriceByFormat(list.get(position).getPrice()));
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.startProductDetailActivity((CategorySmartDeviceActivity) context, String.valueOf(list.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
