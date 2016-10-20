package com.wf.irulu.module.shoppingcart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.shoppingcart.adapter
 * @类名:SoldOutAdapter
 * @作者: 左杰
 * @创建时间:2015/11/26 16:09
 */
public class SoldOutAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductCart> mProductList;
    private int width = 0;

    public SoldOutAdapter(Context context,ArrayList<ProductCart> productList){
        mContext = context;
        this.mProductList = productList;
        this.width = UIUtils.px2dip(135);//135转换成dip后的值为90
    }

    @Override
    public int getCount() {
        if(null != mProductList) return mProductList.size();
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
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_sold_out_adapter, null);
            holder = new ViewHolder();
            holder.sold_out_image = (ImageView) convertView.findViewById(R.id.sold_out_image);
            holder.sold_out_name = (TextView) convertView.findViewById(R.id.sold_out_name);
            holder.sold_out_productSku = (TextView) convertView.findViewById(R.id.sold_out_productSku);
            holder.sold_out_price = (TextView) convertView.findViewById(R.id.sold_out_price);
            holder.sold_out_num = (TextView) convertView.findViewById(R.id.sold_out_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ProductCart productCart = mProductList.get(position);
        String image = productCart.getImage();
        String productName = productCart.getProduct();
        holder.sold_out_name.setText(productName);
        ArrayList<String> productSku = productCart.getProductSku();
        float price = productCart.getPrice();
        int quantity = productCart.getQuantity();
        String str ="?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75";
        IruluApplication.getInstance().getPicasso().load(image +str).placeholder(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(holder.sold_out_image);
        holder.sold_out_productSku.setText(productSku.toString().replace("[","").replace("]",""));
        holder.sold_out_price.setText(StringUtils.getPriceByFormat(price));
        holder.sold_out_num.setText("x"+quantity);
        return convertView;
    }

    static class ViewHolder{
        ImageView sold_out_image;
        TextView sold_out_name;
        TextView sold_out_productSku;
        TextView sold_out_price;
        TextView sold_out_num;
    }
}
