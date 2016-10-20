package com.wf.irulu.module.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.view.NoScrollGridView;

import java.util.ArrayList;

/**
 * @描述: Products订单条目适配器
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.adapter
 * @类名:OrderConfirmAdapter
 * @作者: 左杰
 * @创建时间:2015/11/16 16:20
 */
public class OrderConfirmAdapter extends BaseAdapter {
    private ArrayList<OrderDetail> orders = null;
    private LayoutInflater mInflater = null;
    private String priceSymbol = "";
    private Context mContext;

    public OrderConfirmAdapter(ArrayList<OrderDetail> orders, Context mContext) {
        super();
        this.orders = orders;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.priceSymbol = "$ ";
    }

    public void setOrders(ArrayList<OrderDetail> orders){
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (orders == null) {
            return 0;
        }
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mHolder mHolder = null;
        final OrderDetail order = (OrderDetail) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.orders_splite_show, null);
            mHolder = new mHolder();
            mHolder.orders_lv_product  = (NoScrollGridView) convertView.findViewById(R.id.order_nsgv_products );
            ((ViewGroup)convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mHolder.orders_lv_product.setFocusable(false);
            convertView.setTag(mHolder);
        }else {
            mHolder = (mHolder) convertView.getTag();
        }
        //Products中的商品的Adapter
        mHolder.orders_lv_product.setAdapter(new OrderConfirmProductAdapter(order.getProductList(), mContext));
        return convertView;
    }

    static class mHolder {
        private NoScrollGridView orders_lv_product;
    }
}
