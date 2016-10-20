package com.wf.irulu.module.product.adapter;

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
import com.wf.irulu.common.bean.MaybeLike;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.module.home.onclick.AddWishlistOnClick_01;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.adapter
 * @类名:CustomerLoveAdapter
 * @作者: Yuki
 * @创建时间:2015/10/30
 */
public class CustomerLoveAdapter extends RecyclerView.Adapter<CustomerLoveAdapter.LoveViewHolder> {

    private Context mContext;
    private int mWidth;
    private ArrayList<MaybeLike> mMaybeLikes;
    private LoveViewHolder.ItemClickListener mItemClickListener;

    public CustomerLoveAdapter(Context context, ArrayList<MaybeLike> likes) {
        mContext = context;
        this.mMaybeLikes = likes;
        this.mWidth = ConstantsUtils.DISPLAYW * 30 / 75;
        initItemClickListener();
    }


    private void initItemClickListener() {

        mItemClickListener = new LoveViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int type, int position) {
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                MaybeLike vMaybeLike = mMaybeLikes.get(position);
                switch (type) {
                    case LoveViewHolder.ITEM:
                        ProductDetailActivity.startProductDetailActivity(mContext, vMaybeLike.getId() + "");
                        break;
                    case LoveViewHolder.WISH:
                        new AddWishlistOnClick_01(mContext, IruluController.getInstance()).OnClick(vMaybeLike.getAddWishList(), vMaybeLike.getId() + "");

                        break;
                }

            }
        };
    }

    @Override
    public LoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vInflater = LayoutInflater.from(mContext);
        View view = vInflater.inflate(R.layout.new_arrivals_item, parent, false);
        return new LoveViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(LoveViewHolder holder, int position) {


        MaybeLike vMaybeLike = mMaybeLikes.get(position);

        int wish = Integer.valueOf(vMaybeLike.getAddWishList());
        holder.mWishIv.setImageResource(wish == 0 ? R.mipmap.wishlist_icon_normal : R.mipmap.wishlist_icon_select);

        holder.mDiscountTV.setText(vMaybeLike.getPercent() + "%\nOFF");
        holder.mPriceTv.setText(StringUtils.getPriceByFormat((float) vMaybeLike.getPrice()));
        if (vMaybeLike.getPrice() == vMaybeLike.getOriPrice()) {
            holder.mOriginalPriceTv.setVisibility(View.GONE);
            holder.mDiscountTV.setVisibility(View.GONE);
        } else {
            holder.mOriginalPriceTv.setVisibility(View.VISIBLE);
            holder.mDiscountTV.setVisibility(View.VISIBLE);
            holder.mOriginalPriceTv.setText(StringUtils.getPriceByFormat((float) vMaybeLike.getOriPrice()));
        }

        if (!TextUtils.isEmpty(vMaybeLike.getImage())) {
            IruluApplication.getInstance().getPicasso().load(StringUtils.getThumbnailImageUrlFormat(vMaybeLike.getImage(), mWidth)).error(R.mipmap.notify_image_xiaotu).into(holder.mGoodsIv);
        } else {
            holder.mGoodsIv.setImageResource(R.mipmap.notify_image_xiaotu);
        }


    }


    @Override
    public int getItemCount() {
        return mMaybeLikes == null ? 0 : mMaybeLikes.size();
    }

    public static class LoveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static final int WISH = 1;
        public static final int ITEM = 2;
        TextView mDiscountTV;
        ImageView mGoodsIv;
        ImageView mWishIv;
        TextView mPriceTv;
        TextView mOriginalPriceTv;
        ItemClickListener mItemClickListener;


        public LoveViewHolder(View itemView, ItemClickListener itemClickListener) {
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
            mItemClickListener.onItemClick(v, (Integer) v.getTag(), getAdapterPosition());

        }


        interface ItemClickListener {

            void onItemClick(View view, int type, int position);

        }
    }
}
