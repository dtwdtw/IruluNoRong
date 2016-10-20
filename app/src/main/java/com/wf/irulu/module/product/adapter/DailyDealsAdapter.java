package com.wf.irulu.module.product.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.DailyDealsInit;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.module.home.onclick.AddWishlistOnClick;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/4/14 上午10:03
 */
public class DailyDealsAdapter extends RecyclerView.Adapter<DailyDealsAdapter.HoldView> {

    public static final String TAG="DailyDealsAdapter";
    private Context mContext;
    private IruluController controller;
    private List<DailyDealsInit.ProductListBean> list;
    private int width = 0;
    private int biaoqianResId;


    public DailyDealsAdapter(Context mContext, List<DailyDealsInit.ProductListBean> list, int type, IruluController controller) {
        this.mContext = mContext;

        if (list!=null) {
            this.list = list;
        }

        this.controller = controller;
        this.width = ConstantsUtils.DISPLAYW * 30 / 75;

        switch (type){
            case 0:
                biaoqianResId=R.drawable.biaoqian2;
                break;
            case 1:
                biaoqianResId=R.drawable.biaoqian3;
                break;
        }

    }

    public void setDatas(List<DailyDealsInit.ProductListBean> list){
        if (this.list!=null) {
            this.list.clear();
        }
        this.list=list;
    }

    public void addDatas(List<DailyDealsInit.ProductListBean> list){
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public HoldView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HoldView(View.inflate(mContext, R.layout.item_list_daily_deals, null));
    }

    @Override
    public void onBindViewHolder(HoldView holder, int position) {

        DailyDealsInit.ProductListBean listBean = list.get(position);
        int productId = listBean.getProductId();

        String productName = listBean.getProductName();
        String imageUrl = listBean.getImage();
        //原价
        String price = listBean.getPrice();
        //折扣价格
        String discountPrice = listBean.getDiscountPrice();

        //
        int addWishList = listBean.getAddWishList();

        //折扣
        String percent = listBean.getPercent();

        //让老的价格有一条横杠
        holder.oldPreice.setText(StringUtils.getPriceByFormat(Double.parseDouble(price)));
        holder.oldPreice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (addWishList == 0) {//没有加入到WishList
            holder.wish.setImageResource(R.mipmap.wishlist_icon_normal);
        } else if (addWishList == 1) {//加入到WishList
            holder.wish.setImageResource(R.mipmap.wishlist_icon_select);
        }

        holder.title.setText(productName);
        holder.newPrice.setText(StringUtils.getPriceByFormat(Double.parseDouble(discountPrice)));
        holder.biaoqian.setBackground(mContext.getResources().getDrawable(biaoqianResId));
        if ("0".equals(percent)) {
            holder.biaoqian.setVisibility(View.INVISIBLE);
        }else {
            holder.biaoqian.setText(percent + "%" + "OFF");
        }
        if (!TextUtils.isEmpty(imageUrl))
            IruluApplication.getInstance().getPicasso().load(imageUrl + "?imageView2/0/w/" + width + "/h/" + width + "/interlace/1/q/75").placeholder(R.mipmap.notify_image_xiaotu).into(holder.ico);

        /**
         * 加入Wish List的点击事件
         */
        holder.wish.setOnClickListener(new AddWishlistOnClick(controller,mContext,holder.wish,productId+"",addWishList));

        /**
         * 跳转商品详情页面的点击事件
         */
        holder.itemView.setOnClickListener(new MyOnClick((Activity) mContext, productId));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HoldView extends RecyclerView.ViewHolder {
        private ImageView ico;
        private TextView title;
        private TextView newPrice;
        private TextView oldPreice;
        private ImageView wish;
        private TextView biaoqian;

        public HoldView(View itemView) {
            super(itemView);

            ico = (ImageView) itemView.findViewById(R.id.item_daily_deals_iv_ico);
            title = (TextView) itemView.findViewById(R.id.item_daily_deals_tv_title);
            newPrice = (TextView) itemView.findViewById(R.id.item_daily_deals_tv_price_now);
            oldPreice = (TextView) itemView.findViewById(R.id.item_daily_deals_tv_price_old);
            wish = (ImageView) itemView.findViewById(R.id.item_daily_deals_iv_wish);
            biaoqian = (TextView) itemView.findViewById(R.id.item_daily_deals_tv_biaoqian);


        }
    }

    /**
     * 自定义跳转商品详情页面的点击事件
     */
    class MyOnClick implements View.OnClickListener {
        private int mProductId;
        private Activity mActivity;

        public MyOnClick(Activity activity, int productId) {
            mActivity = activity;
            mProductId = productId;
        }

        @Override
        public void onClick(View v) {
            ProductDetailActivity.startProductDetailActivity(mActivity, String.valueOf(mProductId));
        }
    }
}
