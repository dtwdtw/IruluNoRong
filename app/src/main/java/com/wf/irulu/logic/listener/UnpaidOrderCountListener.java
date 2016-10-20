package com.wf.irulu.logic.listener;

/**
 * @描述: 未付款订单的数量接收监听
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.listener
 * @类名:UnpaidOrderCountListener
 * @作者: 左杰
 * @创建时间:2015/12/2
 */
public interface UnpaidOrderCountListener {
    void UnpaidOrderCount(int count);
}
