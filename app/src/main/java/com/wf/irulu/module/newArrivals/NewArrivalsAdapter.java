package com.wf.irulu.module.newArrivals;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.module.home.onclick.AddWishlistOnClick_01;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;


/**
 * Created by iRULU on 2016/4/14.
 */
public class NewArrivalsAdapter extends RecyclerView.Adapter<NewArrivalsAdapter.NewArrivalsViewHolder> {

    private Context mContext;
    private int mWidth;
    private ArrayList<ProductInfo> mProductInfos;
    private NewArrivalsViewHolder.ItemClickListener mItemClickListener;

    public NewArrivalsAdapter(Context context, ArrayList<ProductInfo> productList) {
        mContext = context;
        this.mProductInfos = productList;
        this.mWidth = ConstantsUtils.DISPLAYW * 30 / 75;
        initItemClickListener();
    }




    private void initItemClickListener() {

        mItemClickListener = new NewArrivalsViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int type, int position) {
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                ProductInfo vProductInfo = mProductInfos.get(position);
                switch (type) {
                    case NewArrivalsViewHolder.ITEM:
                        ProductDetailActivity.startProductDetailActivity(mContext, vProductInfo.getProductId());
                        break;
                    case NewArrivalsViewHolder.WISH:
                        new AddWishlistOnClick_01(mContext, IruluController.getInstance()).OnClick(Integer.parseInt(vProductInfo.getAddWishList()), vProductInfo.getProductId());

                        break;
                }

            }
        };
    }

    @Override
    public NewArrivalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vInflater = LayoutInflater.from(mContext);
        View view = vInflater.inflate(R.layout.new_arrivals_item, parent, false);
        return new NewArrivalsViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(NewArrivalsViewHolder holder, int position) {


        ProductInfo vProductInfo = mProductInfos.get(position);


        int wish = Integer.valueOf(vProductInfo.getAddWishList());
        holder.mWishIv.setImageResource(wish == 0 ? R.mipmap.wishlist_icon_normal : R.mipmap.wishlist_icon_select);

        holder.mDiscountTV.setText(vProductInfo.getPercent() + "%\nOFF");
        holder.mPriceTv.setText(StringUtils.getPriceByFormat(Float.valueOf(vProductInfo.getDiscountPrice())));

        if (vProductInfo.getDiscountPrice().equals( vProductInfo.getPrice())) {
            holder.mOriginalPriceTv.setVisibility(View.GONE);
            holder.mDiscountTV.setVisibility(View.GONE);
        } else {
            holder.mOriginalPriceTv.setVisibility(View.VISIBLE);
            holder.mDiscountTV.setVisibility(View.VISIBLE);
            holder.mOriginalPriceTv.setText(StringUtils.getPriceByFormat(Float.valueOf(vProductInfo.getPrice())));
        }

        if (!TextUtils.isEmpty(vProductInfo.getImage())) {
            IruluApplication.getInstance().getPicasso().load(vProductInfo.getImage() + "?imageView2/0/w/" + mWidth + "/h/" + mWidth + "/interlace/1/q/75").placeholder(R.mipmap.notify_image_xiaotu).error(R.mipmap.notify_image_xiaotu).into(holder.mGoodsIv);
        } else {
            holder.mGoodsIv.setImageResource(R.mipmap.notify_image_xiaotu);
        }


    }


    @Override
    public int getItemCount() {
        return mProductInfos == null ? 0 : mProductInfos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class NewArrivalsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static final int WISH = 1;
        public static final int ITEM = 2;
        TextView mDiscountTV;

        ImageView mGoodsIv;
        ImageView mWishIv;
        TextView mPriceTv;
        TextView mOriginalPriceTv;
        ItemClickListener mItemClickListener;


        public NewArrivalsViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            LinearLayout vItemView = (LinearLayout) itemView.findViewById(R.id.item_view);
            mDiscountTV = (TextView) itemView.findViewById(R.id.rtv_discount);
            mGoodsIv = (ImageView) itemView.findViewById(R.id.iv_product);
            mWishIv = (ImageView) itemView.findViewById(R.id.iv_wish);
            mPriceTv = (TextView) itemView.findViewById(R.id.tv_current_price);
            mOriginalPriceTv = (TextView) itemView.findViewById(R.id.tv_original_price);

            mOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            mWishIv.setTag(WISH);
            vItemView.setTag(ITEM);

            this.mItemClickListener = itemClickListener;
            vItemView.setOnClickListener(this);
            mWishIv.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag(), getAdapterPosition() - 1);

        }


        interface ItemClickListener {

            void onItemClick(View view, int type, int position);

        }
    }
}
