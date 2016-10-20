package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.wf.irulu.module.order.activity.OrderDetailActivity;
import com.wf.irulu.module.order.activity.OrdersActivity;

import java.util.ArrayList;

/**
 * 
 * @描述: 订单中商品适配器
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.shopping.adapter
 * @类名:ProductOrderAdapter
 * @作者: Yuki
 * @创建时间:2015-7-28 下午6:36:19
 *
 */
public class ProductOrderAdapter extends BaseAdapter {
	
	private ArrayList<OrderDetailProduct> products = null;
	private LayoutInflater mInflater = null;
	private Context mContext = null;
	private String priceSymbol = "$";
	private String orderId = null;
	private Picasso mPicasso = null;
	private int width = 0;

	ViewHolder holder;

	public ProductOrderAdapter(ArrayList<OrderDetailProduct> products, Context mContext,String orderId) {
		super();
		this.products = products;
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		this.mPicasso = IruluApplication.getInstance().getPicasso();
//		this.priceSymbol = IruluController.getInstance().getCacheManager().getPriceSymbol();
		this.orderId = orderId;
		this.width = UIUtils.dip2px(60);
	}

	public void setProductsOrderID(ArrayList<OrderDetailProduct> products, String orderId) {
		this.products = products;
		this.orderId = orderId;
		notifyDataSetChanged();
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
    public boolean isEnabled(int position) {     
       return false;     
    }  

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderDetailProduct info = (OrderDetailProduct) getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_order_product, null);
			holder.order_iv_img = (ImageView) convertView.findViewById(R.id.order_iv_img);
			holder.order_tv_name = (TextView) convertView.findViewById(R.id.order_tv_name);
			holder.order_tv_price = (TextView) convertView.findViewById(R.id.order_tv_price);
			holder.order_tv_total = (TextView) convertView.findViewById(R.id.order_tv_total);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.order_tv_total.setText("x" + info.getQuantity());
		holder.order_tv_name.setText(info.getName());
		holder.order_tv_price.setText(priceSymbol + info.getPrice());
//		picasso.load(info.getImage() + "?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75").placeholder(R.drawable.bg_picutre_show).error(R.drawable.bg_picutre_show).resizeDimen(R.dimen.image_height, R.dimen.image_height).into(mHolder.order_iv_img);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrderDetailActivity.startOrderDetailActivity((OrdersActivity)mContext,orderId);
			}
		});
		mPicasso.load(StringUtils.getThumbnailImageUrlFormat(info.getImage(), UIUtils.getSixthWidth())).placeholder(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(holder.order_iv_img);
		return convertView;
	}

	static class ViewHolder{
		private ImageView order_iv_img;
		private TextView order_tv_name;
		private TextView order_tv_price;
		private TextView order_tv_total;
	}
}
