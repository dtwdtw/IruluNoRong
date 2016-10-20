package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.bean.Product;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.comment.activity.MyReviewsActivity;
import com.wf.irulu.module.order.activity.SelectToReivewActivity;

import java.util.ArrayList;

/**
 * Created by Sellen on 15/11/23.
 */
public class SelectToReviewAdapter extends BaseAdapter {

    ViewHolder holder;
    Context context;
    ArrayList<OrderDetailProduct> products;

    public SelectToReviewAdapter(Context context, ArrayList<OrderDetailProduct> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products == null ? 0 : products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_to_review, null);
            holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.write_review = (Button) convertView.findViewById(R.id.write_review);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        IruluApplication.getInstance().getPicasso()
                .load(StringUtils.getThumbnailImageUrlFormat(products.get(position).getImage(), UIUtils.getSixthWidth()))
                .placeholder(R.mipmap.notify_image_xiaotu)
                .error(R.mipmap.notify_image_xiaotu)
                .into(holder.thumb);
        holder.name.setText(products.get(position).getName());
        holder.write_review.setOnClickListener(new MyClickListener(position));

        return convertView;
    }

    final class MyClickListener implements View.OnClickListener {

        int position;

        public MyClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            OrderDetailProduct p = products.get(position);
            Product product = new Product();
            product.setId(String.valueOf(p.getProductId()));
            product.setImage(p.getImage());
            product.setName(p.getName());
            product.setPrice(p.getPrice());
            product.setQuantity(p.getQuantity());
            product.setSku(p.getSku());
            MyReviewsActivity.startMyReviewsActivity((SelectToReivewActivity)context, product,"order");
        }
    }

    final class ViewHolder {
        ImageView thumb;
        TextView name;
        Button write_review;
    }
}
