package com.wf.irulu.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.ArrayList;

/**
 * @描述: 主页中NewRelease的RecyclerAdapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.home.adapter
 * @类名:HomeNewReleaseRecyclerAdapter
 * @作者: 左杰
 * @创建时间:2016/1/15 9:51
 */
public class HomeNewReleaseRecyclerAdapter extends RecyclerView.Adapter<HomeNewReleaseRecyclerAdapter.HomeRecyclerHolder>{
    private ArrayList<ProductInfo> mProductList;
    private Context mContext;
    public HomeNewReleaseRecyclerAdapter(Context context, ArrayList<ProductInfo> productList){
        this.mContext = context;
        mProductList = productList;
    }

    public void setNotifyDataSetChanged(ArrayList<ProductInfo> productList){
        mProductList = productList;
        notifyDataSetChanged();
    }

    @Override
    public HomeRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_new_release, null);
        HomeRecyclerHolder holder = new HomeRecyclerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeRecyclerHolder holder, int position) {
        ProductInfo productInfo = mProductList.get(position);
        String image = productInfo.getImage();
        String productId = productInfo.getProductId();//产品id

        int height = ConstantsUtils.DISPLAYW * 2 / 5;

        //设置控件的宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstantsUtils.DISPLAYW,height);
        holder.new_release_iv.setLayoutParams(layoutParams);
        String str =  "?imageView2/0/w/" + ConstantsUtils.DISPLAYW + "/h/" + height + "/interlace/1/q/75";
        IruluApplication.getInstance().getPicasso().load(image +str).placeholder(R.mipmap.notify_image_xiaotu).into(holder.new_release_iv);

        /**
         * 跳转商品详情页面的点击事件
         */
        holder.mView.setOnClickListener(new MyOnClick((Activity) mContext, productId));
    }

    @Override
    public int getItemCount() {
        if (mProductList != null) {
            return mProductList.size();
        }
        return 0;
    }

    public class HomeRecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView new_release_iv;
        private View mView;

        public HomeRecyclerHolder(View itemView) {
            super(itemView);
            new_release_iv = (ImageView)itemView.findViewById(R.id.new_release_iv);
            mView = itemView;
        }
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
