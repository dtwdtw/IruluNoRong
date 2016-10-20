package com.wf.irulu.module.payment.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: Products中的商品的Adapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.adapter
 * @类名:OrderConfirmProductAdapter
 * @作者: 左杰
 * @创建时间:2015/11/16 16:23
 */
public class OrderConfirmProductAdapter extends BaseAdapter {
    private List<OrderDetailProduct> products = null;
    private LayoutInflater mInflater = null;
    private Picasso mPicasso;
    private int width = 0;

    public OrderConfirmProductAdapter (List<OrderDetailProduct> products,Context mContext) {
        super();
        this.mPicasso = Picasso.with(mContext);
        this.products = products;
        this.mInflater = LayoutInflater.from(mContext);
//        this.width = UIUtils.dip2px(70);
        this.width = ConstantsUtils.DISPLAYW*12/75;
    }

    @Override
    public int getCount() {
        return products.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OrderDetailProduct info = (OrderDetailProduct) getItem(position);
        mHolder mHolder = null;
        if (convertView == null) {
            mHolder = new mHolder();
            convertView = mInflater.inflate(R.layout.lv_item_order_confirm, null);
            mHolder.order_iv_img = (ImageView) convertView.findViewById(R.id.order_iv_img);
            mHolder.order_tv_name = (TextView) convertView.findViewById(R.id.order_tv_name);
            mHolder.order_tv_price = (TextView) convertView.findViewById(R.id.order_tv_price);
            mHolder.order_tv_amount = (TextView) convertView.findViewById(R.id.order_tv_amount);
            mHolder.orderdetail_tv_sku = (TextView) convertView.findViewById(R.id.orderdetail_tv_sku);
            mHolder.order_tv_limit = (TextView) convertView.findViewById(R.id.order_tv_limit);
            convertView.setTag(mHolder);
        }else{
            mHolder = (mHolder) convertView.getTag();
        }
        mHolder.order_tv_amount.setText("x" + info.getQuantity());
        mHolder.order_tv_name.setText(info.getName());
        mHolder.order_tv_price.setText(StringUtils.getPriceByFormat(info.getPrice()));
        mHolder.orderdetail_tv_sku.setText(info.getSku().toString().replace("[", "").replace("]","").trim());
        if(!TextUtils.isEmpty(info.getImage())){
            mPicasso.load(info.getImage() + "?imageView2/0/w/" + width + "/h/" + width  + "/format/jpg/interlace/1").resizeDimen(R.dimen.image_height, R.dimen.image_height).into(mHolder.order_iv_img);
        }
        if (info.getIsCanNotSend() == 0){
            mHolder.order_tv_limit.setVisibility(View.GONE);
        }else{
            mHolder.order_tv_limit.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class mHolder{
        private ImageView order_iv_img;
        private TextView order_tv_name;
        private TextView order_tv_price;
        private TextView order_tv_amount;
        private TextView orderdetail_tv_sku;
        private TextView order_tv_limit;
    }

    public void refresh(ArrayList<OrderDetailProduct> productList, int servStatus, int status) {
        this.products = productList;
        notifyDataSetChanged();
    }
}
