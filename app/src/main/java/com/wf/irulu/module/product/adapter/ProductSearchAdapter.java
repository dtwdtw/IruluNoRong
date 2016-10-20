package com.wf.irulu.module.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductSearch;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.adapter
 * @类名:WishAdapter
 * @作者: Yuki
 * @创建时间:2015/11/13
 */
public class ProductSearchAdapter extends BaseAdapter{

    private Context mContext;
    /** wish列表*/
    private ArrayList<ProductSearch> mWishList;
    private LayoutInflater mInflater;
    private Picasso mPicasso;

    public ProductSearchAdapter(Context context, ArrayList<ProductSearch> wishList) {
        mContext = context;
        mWishList = wishList;
        mInflater = LayoutInflater.from(mContext);
        mPicasso = IruluApplication.getInstance().getPicasso();
    }

    public void refresh(ArrayList<ProductSearch> pWishList){
        this.mWishList = pWishList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
//        return 0;
        return mWishList == null ? 0 : mWishList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        ProductSearch info = mWishList.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_wish_list,null);
            mHolder = new ViewHolder();
            mHolder.mWishIvImage = (ImageView) convertView.findViewById(R.id.wish_iv_image);
            mHolder.mWishTvName = (TextView) convertView.findViewById(R.id.wish_tv_name);
            mHolder.mWishRbRate = (RatingBar) convertView.findViewById(R.id.wish_rb_rate);
            mHolder.mWishTvTotal = (TextView) convertView.findViewById(R.id.wish_tv_total);
            mHolder.mWishTvPrice = (TextView) convertView.findViewById(R.id.wish_tv_price);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mWishTvName.setText(info.getName());
        mHolder.mWishTvPrice.setText(StringUtils.getPriceByFormat(info.getPrice()));
        mHolder.mWishTvTotal.setText(String.valueOf(info.getCommentNum()));
        mHolder.mWishRbRate.setRating(info.getStar());
        mPicasso.load(StringUtils.getThumbnailImageUrlFormat(info.getImage(), UIUtils.getThirdWidth())).error(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(mHolder.mWishIvImage);
        return convertView;
    }

    private class ViewHolder {
        ImageView mWishIvImage;
        TextView mWishTvName;
        RatingBar mWishRbRate;
        TextView mWishTvTotal;
        TextView mWishTvPrice;
    }
}
