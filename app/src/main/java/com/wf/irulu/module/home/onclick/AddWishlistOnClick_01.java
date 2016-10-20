package com.wf.irulu.module.home.onclick;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wf.irulu.common.bean.WishList;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.aas.activity.LoginActivity;

import java.util.ArrayList;

/**
 * Created by iRULU on 2016/4/14.
 */
public class AddWishlistOnClick_01 implements ServiceListener{
    private IruluController controller;
    private Context mContext;

    public AddWishlistOnClick_01(Context context, IruluController controller) {
        this.mContext = context;
        this.controller = controller;
    }


    public void OnClick( int addWishList, String productId) {
        if (addWishList == 0) {//添加wishList
            if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                return;
            }
            PageLoadDialog.showDialogForLoading(mContext, true, false);
            controller.getServiceManager().getShoppingService().addToWishList(productId, this);
        } else {//取消添加WishList
            if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                return;
            }
            PageLoadDialog.showDialogForLoading(mContext, true, false);
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
                String productId = bandObj.toString();
                //更新首页的添加WishList图标的状态
                controller.postIsAddWishListCallback(productId, "1");//"0"代表删除，"1"代表添加
                PageLoadDialog.hideDialogForLoading();
                break;
            case DEL_WISH:
                wishList = (WishList) returnObj;
                list = wishList.getList();
                size = list.size();
                CacheUtils.setLong(mContext, "wishListNum", size);
                controller.postWishListCountCallback(size);
                UIUtils.getToastShort(mContext, "Item removed from wish list");
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

        if (returnObj != null) {
            Toast.makeText(mContext, returnObj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
