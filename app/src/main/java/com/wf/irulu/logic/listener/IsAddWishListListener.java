package com.wf.irulu.logic.listener;

/**
 * @描述: 判断是否是加入WishList的监听
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.listener
 * @类名:IsAddWishListListener
 * @作者: 左杰
 * @创建时间:2015/12/4 17:33
 */
public interface IsAddWishListListener {
    /**
     * 当前商品是否加入WishList
     * @param productId 商品ID
     * @param addWishList 1 加入，0未加入
     */
    void isAddWishList(String productId,String addWishList);
}
