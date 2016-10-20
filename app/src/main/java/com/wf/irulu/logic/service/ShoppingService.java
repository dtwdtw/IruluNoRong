package com.wf.irulu.logic.service;

import com.wf.irulu.common.bean.WishInfo;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.SettingManager;

/**
 *
 * @描述: 购物车系统API
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:ShoppingService
 * @作者: Yuki
 * @创建时间:2015-7-15 上午11:09:16
 *
 */
public interface ShoppingService {

    // 	从购物车移除商品
    public static final String URL_REMOVE_PRODUCT = SettingManager.URL_CART_SYSTEM + "/cart/removeProduct";

    // 	获取购物车商品列表
    public static final String URL_GET_CART_INFO = SettingManager.URL_CART_SYSTEM + "/cart/getcartinfo";



    // 修改商品数量
    public static final String URL_UPDATE_PRODUCT = SettingManager.URL_CART_SYSTEM + "/cart/updateproduct";

    //  清空购物车
    public static final String URL_EMPTYPRODUCT = SettingManager.URL_CART_SYSTEM + "/cart/emptyproduct";

    // 	根据保存状态获取购物车商品列表
    public static final String URL_GETPRODUCTLIST = SettingManager.URL_CART_SYSTEM + "/cart/getproductlist";

    // 	保存到以后购买
    public static final String URL_SAVED_TOLATER = SettingManager.URL_CART_SYSTEM + "/cart/savedtolater";

    // 添加到心愿单
    public static final String URL_SHOPPING_WISH_ADD = SettingManager.URL_CART_SYSTEM + "/likes/addLikes";

    // 添加到心愿单
    public static final String URL_SHOPPING_WISH_DEL = SettingManager.URL_CART_SYSTEM + "/likes/delLikes";

    // 添加到购物车
    public static final String URL_SHOPPING_CART_ADD = SettingManager.URL_CART_SYSTEM + "/cart/addproduct";

    // 立即购买
    public static final String URL_SHOPPING_BUY_BOW = SettingManager.URL_ORDER_SYSTEM + "/order/checkout/buynow";

    // 订单列表
    public static final String URL_ORDER_LIST = SettingManager.URL_ORDER_SYSTEM + "/order/orderlist";

    // 获取wish列表id
    public static final String URL_WISH_IDS = SettingManager.URL_CART_SYSTEM + "/likes/getLikesPids";


    /**
     * 添加到心愿单
     * @param productId 商品id
     * @param listener
     */
    void addToWishList(String productId,ServiceListener listener);

    /**
     * 移除一个心愿单
     * @param likesId 心愿单唯一id
     * @param productId 产品id（二选一）
     * @param listener
     */
    void deleteToWishList(String likesId,String productId,ServiceListener listener);

    /**
     * 添加到购物车
     * @param productId 商品id
     * @param pskuid 商品SKU ID
     * @param quantity 商品购买数量
     * @param listener
     */
    void addToCart(String productId,String pskuid,int quantity,ServiceListener listener);

    /**
     * 立即购买
     * @param quantity 数量
     * @param pskuid sku ID
     * @param productId 商品id
     */
    void buyNow(String productId,String pskuid,int quantity,ServiceListener listener);

    /**
     * 获取购物车商品列表
     * @param listener
     */
    void getCartInfo(ServiceListener listener);

    /**
     * 修改商品数量
     * @param id 购物车商品id
     * @param quantity 购买数量
     * @param listener
     */
    void updataProductNum(String id,String quantity,ServiceListener listener);

    /**
     * 从购物车中移除商品
     * @param id 购物车商品id，删除多个时以逗号组合id
     * @param sku_id 购物车商品的sku_id。可选，删除多个时以逗号组合id
     * @param listener
     * @return
     */
    void removeProduct(String id,String sku_id,ServiceListener listener);


    /**
     * 获取所有订单数据
     *
     * @param num	每页显示条数
     * @param listener
     */
    void getOrders(int num ,int page,int type,ServiceListener listener);

    /**
     *
     * @param listener
     */
    void getWishIds(ServiceListener listener);
}
