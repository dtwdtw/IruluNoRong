package com.wf.irulu.module.payment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;

import java.util.ArrayList;

/**
 * @描述: 限售(Shipment Restricted)的Adapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.payment.adapter
 * @类名:ShipmentRestrictedAdapter
 * @作者: 左杰
 * @创建时间:2015/11/19 11:34
 */
public class ShipmentRestrictedAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<OrderDetailProduct> mOrderDetailProductList;
    private int width = 0;

    public ShipmentRestrictedAdapter(Context context, ArrayList<OrderDetailProduct> orderDetailProductList) {
        this.mContext = context;
        this.mOrderDetailProductList = orderDetailProductList;
        this.width = UIUtils.px2dip(135);//135转换成dip后的值为90
    }

    @Override
    public int getCount() {
        if (null != mOrderDetailProductList) return mOrderDetailProductList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_shipment_restricted_adapter, null);
            holder = new ViewHolder();
            holder.product_image = (ImageView) convertView.findViewById(R.id.product_image);
            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.product_productSku = (TextView) convertView.findViewById(R.id.product_productSku);
            holder.product_price = (TextView) convertView.findViewById(R.id.product_price);
            holder.product_num = (TextView) convertView.findViewById(R.id.product_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderDetailProduct orderDetailProduct = mOrderDetailProductList.get(position);
        String msg = orderDetailProduct.getMsg();//限售消息提示
        String image = orderDetailProduct.getImage();
        String name = orderDetailProduct.getName();
        ArrayList<String> productSku = orderDetailProduct.getSku();
        float price = orderDetailProduct.getPrice();
        int quantity = orderDetailProduct.getQuantity();
        String str = "?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75";
        IruluApplication.getInstance().getPicasso().load(image + str).placeholder(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(holder.product_image);
        holder.product_name.setText(name);
        holder.product_productSku.setText(productSku.toString().replace("[", "").replace("]", ""));
        holder.product_price.setText(StringUtils.getPriceByFormat(price));
        holder.product_num.setText("x" + quantity);
        return convertView;
    }

    static class ViewHolder {
        ImageView product_image;
        TextView product_name;
        TextView product_productSku;
        TextView product_price;
        TextView product_num;
    }
}
