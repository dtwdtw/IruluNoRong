package com.wf.irulu.logic.service;

import com.wf.irulu.common.bean.CreditCardPay;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.SettingManager;

/**
 * @描述: 支付相关接口服务定义
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.service.impl
 * @类名:PaymentService
 * @作者: 左杰
 * @创建时间:2015/11/17 19:28
 */
public interface PaymentService {

    public static final String URL_PAY_APPPROCESS = SettingManager.URL_PAY_SYSTEM + "/appprocess";

    /**从Paypal获取支付token*/
    public static final String URL_ACCESS_TOKEN = URL_PAY_APPPROCESS + "/accesstoken";

    /**行用卡支付*/
    public static final String URL_CREDIT_CARD_PAY = URL_PAY_APPPROCESS + "/creditcardpay";

    /**从Paypal获取支付后的相关验证信息（App支付后调用）*/
    public static final String URL_PAY_ORDER = URL_PAY_APPPROCESS + "/verifypayment";

    /**
     * 信用卡支付
     * @param creditCardPay 信用卡支付的实体Bean
     * @param listener
     */
    void creditCardPay(CreditCardPay creditCardPay, ServiceListener listener);

    /**
     * 从Paypal获取支付后的相关验证信息（App支付后调用）
     * @param paynowid 分单与订单对应id（与订单编号2选 1）
     * @param orderId 订单编号（与分单与订单对应id 2选 1）
     * @param payid 支付id
     * @param token 上面Api 获得的token
     * @param listener
     */
    void verifyPayment(String paynowid,String orderId,String payid,String token,ServiceListener listener);

    /**
     * 获取支付token
     * @param listener
     */
    void getPayToken(ServiceListener listener);
}
