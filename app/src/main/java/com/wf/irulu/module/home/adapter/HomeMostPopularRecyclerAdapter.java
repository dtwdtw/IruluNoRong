package com.wf.irulu.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.module.home.onclick.AddWishlistOnClick;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;

/**
 * @描述: 主页中MostPopular的RecyclerAdapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.home.adapter
 * @类名:HomeRecyclerAdapter
 * @作者: 左杰
 * @创建时间:2016/1/12 19:31
 */
public class HomeMostPopularRecyclerAdapter extends RecyclerView.Adapter<HomeMostPopularRecyclerAdapter.HomeRecyclerHolder>{

    private IruluController controller;
    protected LayoutInflater mLayoutInflater;
    private ArrayList<ProductInfo> mProductList;
    private Context mContext;
    private int width = 0;

    public HomeMostPopularRecyclerAdapter(Context context, ArrayList<ProductInfo> productList, IruluController controller){
        this.controller = controller;
        this.mContext = context;
        mProductList = productList;
        mLayoutInflater = LayoutInflater.from(mContext);
//        this.width = (ConstantsUtils.DISPLAYW - UIUtils.dip2px(20)) / 2;
        this.width = ConstantsUtils.DISPLAYW*30/75;
    }

    @Override
    public int getItemCount() {
        if (mProductList != null) {
            return mProductList.size();
        }
        return 0;
    }

    @Override
    public HomeRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_most_popular_gridview, parent, false);
        HomeRecyclerHolder mHolder = new HomeRecyclerHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(HomeRecyclerHolder holder, int position) {
        ProductInfo productInfo = mProductList.get(position);
        String id = productInfo.getId();
        String productId = productInfo.getProductId();//产品id
        String image = productInfo.getImage();//产品图片
        String discountPrice = productInfo.getDiscountPrice();//折扣价
        String star = productInfo.getStar();//星级 0～5
        String commentNum = productInfo.getCommentNum();//评论数
        String addWishList = productInfo.getAddWishList();//是否加入到Wish List  0：否、1：是
        int addWishListInt = Integer.parseInt(addWishList);
        String discountPriceStr = StringUtils.getPriceByFormat(Float.parseFloat(discountPrice));

        if (addWishListInt == 0) {//没有加入到WishList
            holder.wishlist_icon_iv.setImageResource(R.mipmap.wishlist_icon_normal);
        } else if (addWishListInt == 1) {//加入到WishList
            holder.wishlist_icon_iv.setImageResource(R.mipmap.wishlist_icon_select);
        }
        if (!TextUtils.isEmpty(image))
            IruluApplication.getInstance().getPicasso().load(image + "?imageView2/0/w/" + width + "/h/" + width + "/interlace/1/q/75").placeholder(R.mipmap.notify_image_xiaotu).into(holder.image);
        /*if(position % 2 == 1){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            int px = UIUtils.dip2px(5);
            ILog.i("zxj","px==="+px);
            layoutParams.rightMargin = px;
            holder.mView.setLayoutParams(layoutParams);
        }*/
//        holder.product_star.setRating(Float.parseFloat(star));
//        holder.product_comment_num.setText(commentNum);

        holder.product_new_price.setText(discountPriceStr);

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
        private TextView product_new_price;
        private ImageView image,wishlist_icon_iv;
//        private RatingBar ;
        private View mView;

        public HomeRecyclerHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.product_img);
            wishlist_icon_iv = (ImageView) itemView.findViewById(R.id.wishlist_icon_iv);
            product_new_price = (TextView) itemView.findViewById(R.id.product_new_price);
//            product_star = (RatingBar) itemView.findViewById(R.id.product_star);
//            product_comment_num = (TextView) itemView.findViewById(R.id.product_comment_num);
            mView = itemView;
        }
    }

    public void setNotifyDataSetChanged(ArrayList<ProductInfo> productList) {
        mProductList = productList;
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
