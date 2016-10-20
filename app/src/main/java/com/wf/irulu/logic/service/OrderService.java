package com.wf.irulu.logic.service;

import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.SettingManager;

/**
 * @描述:  订单系统API
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.service.impl
 * @类名:OrderService
 * @作者: 左杰
 * @创建时间:2015/10/24 10:46
 */
public interface OrderService {

    public static final String URL_ORDER= SettingManager.URL_ORDER_SYSTEM + "/order";

    //	Check NOW
    public static final String URL_CHECKOUT = URL_ORDER + "/checkout";

    //	生成订单
    public static final String URL_CREATEORDER = URL_ORDER + "/createorder";

    // 	获取指定订单明细
    public static final String URL_DETAIL = SettingManager.URL_ORDER_SYSTEM + "/order/detail";

    // 	获取用户订单数据（APP）
    public static final String URL_ORDERLIST = URL_ORDER + "/orderlist";

    //  获取用户订单数据（APP：包括老系统数据）
    public static final String URL_ORDERLISTWITHOLD = URL_ORDER + "/orderListwithold";

    //  更新订单状态
    public static final String URL_UPDATEORDERSTATUS = URL_ORDER + "/updateorderstatus";

    // 	更新订单支付信息
    public static final String URL_UPDATEORDERPAYINFO = URL_ORDER + "/updateorderpayinfo";

    // 更新物流信息
    public static final String URL_UPDATELOGISTICS = URL_ORDER + "/updatelogistics";

    //	更新送货地址
    public static final String URL_UPDATEADDRESS = URL_ORDER + "/updateaddress";

    // 	根据订单物流公司id获取物流公司信息
    public static final String URL_GETLOGISTICS = SettingManager.URL_ORDER_SYSTEM + "/logistics/getlogisticstracking";

    //根据订单编号获得送货地址
    public static final String URL_GETADDRESSBYOID = URL_ORDER + "/getaddressbyoid";

    // 3.7.14	获得退款原因列表
    public static final String URL_GETREFUNDREASON = URL_ORDER + "/getrefundreason";

    // 3.7.15	获得心愿单列表
    public static final String URL_GETLIST = SettingManager.URL_CART_SYSTEM + "/likes/getList";

    // 3.7.16	移除一个心愿单
    public static final String URL_DELLIKES = URL_ORDER + "/likes/delLikes";

    // 3.7.17	添加一个心愿单
    public static final String URL_ADDLIKES = URL_ORDER + "/likes/addLikes";

    // 3.7.18	BUY NOW
    public static final String URL_CHECKOUT_BUYNOW = URL_ORDER + "/checkout/buynow";

    //3.7.19	BUY NOW生成订单
    public static final String URL_CREATEORDER_BUYNOW = URL_CREATEORDER + "/buynow";

    //3.7.20	取消订单
    public static final String URL_CANCELORDER = URL_ORDER + "/cancelorder";

    //3.7.21	删除订单
    public static final String URL_DELORDER = URL_ORDER + "/delorder";

    //  3.7.22	确认收货
    public static final String URL_CONFIRMORDER = URL_ORDER + "/confirmorder";

    //3.7.23	改变订单状态
    public static final String URL_STAT = URL_ORDER + "/stat";

    /****************************************售后 start****************************************/
    //3.10.1	申请售后
    public static final String URL_SERVICE_UP = SettingManager.URL_ORDER_SYSTEM + "/service/up";

    //3.10.2	申请售后原因
    public static final String URL_SERVICE_TAG = SettingManager.URL_ORDER_SYSTEM + "/service/tag";

    //3.10.3	售后对话
    public static final String URL_SERVICE_SERV = SettingManager.URL_ORDER_SYSTEM + "/service/serv";

    //3.10.4	售后详情
    public static final String URL_SERVICE_DETAIL = SettingManager.URL_ORDER_SYSTEM + "/service/detail";
    /****************************************售后 end****************************************/

    /**
     * checkout
     * @param coupon 优惠券码
     * @param addressId 地址Id
     * @param mobile 是否使用促销 0：否、1：是
     * @param listener
     */
    void checkout(String coupon,String addressId,String mobile,ServiceListener listener);


    /**
     * 获取售后详情
     * @param servId
     * @param listener
     */
    void getServiceDetails(String servId,ServiceListener listener);

    /**
     * BUY NOW生成订单
     * @param productId 商品id
     * @param pskuid	SKU ID
     * @param quantity	数量
     * @param coupon	优惠券码
     * @param mobile	是否使用促销 0：否、1：是
     * @param addressId	地址Id
     * @param listener
     */
    void createOrder(String productId,String pskuid,String quantity,String coupon,String mobile,String addressId,ServiceListener listener);

    /**
     * buy now
     * @param productId 商品id
     * @param pskuid	SKU ID
     * @param quantity	购买数量
     * @param coupon	优惠券码
     * @param addressId	地址Id
     * @param mobile	是否使用促销 0：否、1：是
     * @param listener
     */
    void buyNow(String productId,String pskuid,String quantity,final String coupon,String addressId,String mobile, ServiceListener listener);

    /**
     * 获取订单详情
     * @param orderid 订单id
     * @param listener
     */
    void getOrderDetail(String orderid,ServiceListener listener);

    /**
     * 取消一个订单
     * @param orderid 订单号
     * @param listener
     */
    void cancelOrder(String orderid,ServiceListener listener);

    /**
     * 删除一个订单
     * @param orderid 订单号
     * @param listener
     */
    void deleteOrder(String orderid,ServiceListener listener);

    /**
     * 确认收货
     * @param orderid 订单号
     * @param listener
     */
    void confirmOrder(String orderid,ServiceListener listener);

    /**
     * 获取物流信息
     */
    void getLogisticsInfor(String orderid,ServiceListener listener);
}
