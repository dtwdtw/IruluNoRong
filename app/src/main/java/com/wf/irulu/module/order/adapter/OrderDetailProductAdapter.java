package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.order.adapter
 * @类名:OrderDetailProductAdapter
 * @作者: Yuki
 * @创建时间:2015/11/24
 */
public class OrderDetailProductAdapter extends BaseAdapter{

    private ArrayList<OrderDetailProduct> mProductInfos;
    private Context mContext;
    private LayoutInflater mInflater;
    private Picasso mPicasso;

    public OrderDetailProductAdapter(ArrayList<OrderDetailProduct> productInfos, Context context) {
        mProductInfos = productInfos;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPicasso = IruluApplication.getInstance().getPicasso();
    }

    @Override
    public int getCount() {
        return mProductInfos == null ? 0 : mProductInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null){
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_detail_product,null);
            mHolder.mOrderIvImage = (ImageView) convertView.findViewById(R.id.product_iv_image);
            mHolder.mOrderTvCount = (TextView) convertView.findViewById(R.id.product_tv_count);
            mHolder.mOrderTvPrice = (TextView) convertView.findViewById(R.id.product_tv_price);
            mHolder.mOrderTvSKU = (TextView) convertView.findViewById(R.id.product_tv_sku);
            mHolder.mOrderTvName = (TextView) convertView.findViewById(R.id.product_tv_name);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        OrderDetailProduct info = mProductInfos.get(position);
        mHolder.mOrderTvCount.setText("x " + String.valueOf(info.getQuantity()));
        mHolder.mOrderTvPrice.setText(StringUtils.getPriceByFormat(info.getPrice()));
        mHolder.mOrderTvSKU.setText(info.getSku().toString().replace("[","").replace("]", ""));
        mHolder.mOrderTvName.setText(info.getName());
        mPicasso.load(StringUtils.getThumbnailImageUrlFormat(info.getImage(), UIUtils.getSixthWidth())).placeholder(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(mHolder.mOrderIvImage);
        return convertView;
    }

    class ViewHolder {
        TextView mOrderTvName;
        TextView mOrderTvSKU;
        TextView mOrderTvPrice;
        TextView mOrderTvCount;
        ImageView mOrderIvImage;
    }
}
