package com.wf.irulu.logic.listener;

import com.wf.irulu.common.bean.ProductCart;

import java.util.ArrayList;

/**
 * @描述: 清空or刷新购物车的监听
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.listener
 * @类名:ShoppingCartEmptyListener
 * @作者: 左杰
 * @创建时间:2015/11/19 9:43
 */
public interface ShoppingCartEmptyListener {

    void shoppingCartEmpty(ArrayList<ProductCart> productList);
}
