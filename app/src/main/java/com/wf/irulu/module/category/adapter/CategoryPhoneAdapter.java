package com.wf.irulu.module.category.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.CategoryProductListBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.category.activity.CategoryPhoneActivity;
import com.wf.irulu.module.category.holder.CategoryPhoneHolder;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategoryPhoneAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CategoryProductListBean> list;

    public CategoryPhoneAdapter(Context context, ArrayList<CategoryProductListBean> list) {
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
        CategoryPhoneHolder holder;
        if (view == null) {
            holder = new CategoryPhoneHolder();
            view = View.inflate(mContext, R.layout.item_category_phone_lv, null);
            holder.iv = (ImageView) view.findViewById(R.id.item_category_phone_iv);
            holder.name_tv = (TextView) view.findViewById(R.id.item_category_phone_name_tv);
            holder.rb = (RatingBar) view.findViewById(R.id.item_category_phone_rtb);
            holder.comments_tv = (TextView) view.findViewById(R.id.item_category_phone_comments_tv);
            holder.price_tv = (TextView) view.findViewById(R.id.item_category_phone_price_tv);
            holder.ll = (LinearLayout) view.findViewById(R.id.item_category_phone_ll);
            view.setTag(holder);
        }else{
         holder = (CategoryPhoneHolder) view.getTag();
        }
      final  CategoryProductListBean pb = list.get(i);
        IruluApplication.getInstance().getPicasso().load(pb.getImage()+"?imageView2/3/w/" +(ConstantsUtils.DISPLAYW- UIUtils.dip2px(40))/4 + "/h/" + UIUtils.dip2px(100)).error(R.mipmap.bg_picutre_show).into(holder.iv);
        holder.name_tv.setText(pb.getName());
        holder.comments_tv.setText(String.valueOf(pb.getCommentNum()));
        holder.rb.setRating(pb.getStar());
        holder.price_tv.setText(StringUtils.getPriceByFormat(pb.getPrice()));
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.startProductDetailActivity((CategoryPhoneActivity) mContext, String.valueOf(pb.getId()));
            }
        });
        return view;
    }
}
