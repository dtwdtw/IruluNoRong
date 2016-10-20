package com.wf.irulu.module.homenew1_3_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.module.home.onclick.AddWishlistOnClick;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * @描述: 主页中SpecialOffers的RecyclerAdapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.home.adapter
 * @类名:HomeRecyclerAdapter
 * @作者: 左杰
 * @创建时间:2016/1/12 19:31
 */
public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.HomeRecyclerHolder>{

    private IruluController controller;
    protected LayoutInflater mLayoutInflater;
    List<ProductInfo> productListBea;
    private Context mContext;
    private int width = 0;

    public HomeRecycleViewAdapter(Context context, List<ProductInfo> productListBea, IruluController controller){
        this.controller = controller;
        this.mContext = context;
        this.productListBea = productListBea;
        mLayoutInflater = LayoutInflater.from(mContext);
//        this.width = (ConstantsUtils.DISPLAYW - UIUtils.dip2px(20)) / 2;
        this.width = ConstantsUtils.DISPLAYW*30/75;
    }

    @Override
    public int getItemCount() {
        if (productListBea != null) {
            return productListBea.size();
        }
        return 0;
    }

    @Override
    public HomeRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_grid_special_offers, parent, false);
        HomeRecyclerHolder mHolder = new HomeRecyclerHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(HomeRecyclerHolder holder, int position) {
        ProductInfo productInfo = productListBea.get(position);
        String id = productInfo.getId();
        String productId = productInfo.getProductId();//产品id
        String image = productInfo.getImage();//产品图片
        String price = productInfo.getPrice();//原价
        String discountPrice = productInfo.getDiscountPrice();//折扣价
        String percent = productInfo.getPercent();//折扣比
        String addWishList = productInfo.getAddWishList();//是否加入到Wish List  0：否、1：是
        int addWishListInt = Integer.parseInt(addWishList);

        ILog.e("hellolove productId",productId);
        ILog.e("hellolove discountPrice",discountPrice);
        ILog.e("hellolove percent",percent);


        String formatPrice = StringUtils.getPriceByFormat(Float.parseFloat(price));
        String formatDiscountPrice = StringUtils.getPriceByFormat(Float.parseFloat(discountPrice));

        //让老的价格有一条横杠
        holder.product_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (addWishListInt == 0) {//没有加入到WishList
            holder.wishlist_icon_iv.setImageResource(R.mipmap.wishlist_icon_normal);
        } else if (addWishListInt == 1) {//加入到WishList
            holder.wishlist_icon_iv.setImageResource(R.mipmap.wishlist_icon_select);
        }
        holder.product_old_price.setText(formatPrice);
        holder.product_new_price.setText(formatDiscountPrice);
        holder.biaoqian_tv.setText(percent);
        if (!TextUtils.isEmpty(image))
            IruluApplication.getInstance().getPicasso().load(image + "?imageView2/0/w/" + width + "/h/" + width + "/interlace/1/q/75").placeholder(R.mipmap.notify_image_xiaotu).into(holder.image);
        /*if(position % 2 == 1){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            int px = UIUtils.dip2px(5);
            ILog.i("zxj","px==="+px);
            layoutParams.rightMargin = px;
            holder.mView.setLayoutParams(layoutParams);
        }*/
        /**
         * 加入Wish List的点击事件
         */
        holder.wishlist_icon_iv.setOnClickListener(new AddWishlistOnClick(mContext, holder.wishlist_icon_iv, productInfo, controller));

        /**
         * 跳转商品详情页面的点击事件
         */
        holder.mView.setOnClickListener(new MyOnClick((Activity) mContext, productId));
    }

    /**
     * RecyclerView的ViewHolder类
     */
    public class HomeRecyclerHolder extends RecyclerView.ViewHolder {
        private TextView biaoqian_tv,product_old_price,product_new_price;
        private ImageView image,wishlist_icon_iv;
        private FrameLayout biaoqian_layout;
        private View mView;

        public HomeRecyclerHolder(View itemView) {
            super(itemView);
//            mTvItem = (TextView) itemView.findViewById(R.id.tv_item);
            image = (ImageView) itemView.findViewById(R.id.product_img);
            biaoqian_layout = (FrameLayout) itemView.findViewById(R.id.biaoqian_layout);
            biaoqian_tv = (TextView) itemView.findViewById(R.id.biaoqian_tv);
            wishlist_icon_iv = (ImageView) itemView.findViewById(R.id.wishlist_icon_iv);
            product_old_price = (TextView) itemView.findViewById(R.id.product_old_price);
            product_new_price = (TextView) itemView.findViewById(R.id.product_new_price);
            mView = itemView;
        }
    }

    public void setNotifyDataSetChanged(List<ProductInfo> productList) {
        productListBea = productList;
        notifyDataSetChanged();
    }

    /**
     * 自定义跳转商品详情页面的点击事件
     */
    class MyOnClick implements View.OnClickListener {
        private String mProductId;
        private Activity mActivity;

        public MyOnClick(Activity activity, String productId) {
            mActivity = activity;
            mProductId = productId;
        }

        @Override
        public void onClick(View v) {
            ProductDetailActivity.startProductDetailActivity(mActivity, mProductId);
        }
    }
}
