package com.wf.irulu.module.home.onclick;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.bean.WishList;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.aas.activity.LoginActivity;

import java.util.ArrayList;

/**
 * @描述: 自定义加入Wish List的点击事件
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.content.onclick
 * @类名:AddWishlistOnClick
 * @作者: 左杰
 * @创建时间:2015/11/27 15:13
 */
public class AddWishlistOnClick implements View.OnClickListener, ServiceListener {
    private IruluController controller;
    private Context mContext;
    private ImageView holderIv;
    private ProductInfo mProductInfo;
    private String productId;
    /**
     * 判断是否加入WishList，默认未加入
     */
    private boolean isAddWishlist = true;
    /**是否是第一次点击*/
    private boolean isFirst = true;
    private int addWishList=-1;

    public AddWishlistOnClick(IruluController controller, Context mContext, ImageView holderIv, String productId,int addWishList) {
        this.controller = controller;
        this.mContext = mContext;
        this.holderIv = holderIv;
        this.productId = productId;
        this.addWishList=addWishList;

    }

    public AddWishlistOnClick(Context context, ImageView holderIv, ProductInfo productInfo, IruluController controller) {
        this.mContext = context;
        this.holderIv = holderIv;
        this.mProductInfo = productInfo;
        this.controller = controller;
        this.productId=productInfo.getProductId();
        this.addWishList=Integer.valueOf(productInfo.getAddWishList());
    }




    @Override
    public void onClick(View v) {
        //如果mProductInfo不为空
        if ((mProductInfo!=null) && (TextUtils.isEmpty(productId)) && (addWishList==-1)) {
            String addWishListStr = mProductInfo.getAddWishList();
            addWishList = Integer.parseInt(addWishListStr);
            String productId = mProductInfo.getProductId();
        }

        if(addWishList == 0 & isFirst){
            isAddWishlist = true;
            isFirst = false;
        }else if(addWishList == 1 & isFirst){
            isAddWishlist = false;
            isFirst = false;
        }

        if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("shoppingCartActivityTag", "ShoppingCartActivity");
            mContext.startActivity(intent);
            return;
        }

        if(isAddWishlist) {//添加wishList
            PageLoadDialog.showDialogForLoading(mContext, true, false);
            controller.getServiceManager().getShoppingService().addToWishList(productId, this);
        }else{//取消添加WishList
            PageLoadDialog.showDialogForLoading(mContext,true,false);
            controller.getServiceManager().getShoppingService().deleteToWishList("", productId, this);
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case ADD_WISH:
                WishList wishList = (WishList) returnObj;
                ArrayList<WishList> list = wishList.getList();
                int size = list.size();
                CacheUtils.setLong(mContext, "wishListNum", size);
                controller.postWishListCountCallback(size);

                UIUtils.getToastShort(mContext, "Added to Wish List");
                holderIv.setImageResource(R.mipmap.wishlist_icon_select);
                isAddWishlist = !isAddWishlist;

                String productId = bandObj.toString();
                //更新首页的添加WishList图标的状态
                controller.postIsAddWishListCallback(productId, "1");//"0"代表删除，"1"代表添加

                PageLoadDialog.hideDialogForLoading();
                break;
            case DEL_WISH:
                wishList = (WishList) returnObj;
                list = wishList.getList();
                size = list.size();
                CacheUtils.setLong(mContext,"wishListNum",size);

                controller.postWishListCountCallback(size);
                UIUtils.getToastShort(mContext, "Item removed from wish list");
                holderIv.setImageResource(R.mipmap.wishlist_icon_normal);
                isAddWishlist = !isAddWishlist;

                productId = bandObj.toString();
                //更新首页的添加WishList图标的状态
                controller.postIsAddWishListCallback(productId, "0");//"0"代表删除，"1"代表添加

                PageLoadDialog.hideDialogForLoading();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case ADD_WISH:
                PageLoadDialog.hideDialogForLoading();
                break;
            case DEL_WISH:
                PageLoadDialog.hideDialogForLoading();
                break;
            default:
                break;
        }

        if(returnObj != null){
            Toast.makeText(mContext,returnObj.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
