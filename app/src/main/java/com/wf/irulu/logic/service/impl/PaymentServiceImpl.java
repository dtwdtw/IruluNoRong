package com.wf.irulu.logic.service.impl;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.CreditCardPay;
import com.wf.irulu.common.bean.PaypalSuccess;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.logic.service.PaymentService;

import org.json.JSONObject;

import java.io.IOException;

/**
 * @描述: 支付接口定义实现
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.service.impl
 * @类名:PaymentServiceImpl
 * @作者: 左杰
 * @创建时间:2015/11/17 19:29
 */
public class PaymentServiceImpl extends BaseService implements PaymentService{

    private final String TAG = getClass().getCanonicalName();

    private IruluController controller;
    private ServiceManager serviceManager;

    public PaymentServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }

    @Override
    public void creditCardPay(final CreditCardPay creditCardPay, final ServiceListener listener) {
        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "orderid",creditCardPay.getOrderid());
        addOKHttpRequestBody(builder,  "cardid", creditCardPay.getCardid());
        addOKHttpRequestBody(builder, "month", creditCardPay.getMonth());
        addOKHttpRequestBody(builder, "year", creditCardPay.getYear());
        addOKHttpRequestBody(builder, "cvv", creditCardPay.getCvv());
        addOKHttpRequestBody(builder, "first", creditCardPay.getFirst());
        addOKHttpRequestBody(builder,"last", creditCardPay.getLast());
        addOKHttpRequestBody(builder,"city", creditCardPay.getCity());
        addOKHttpRequestBody(builder, "line1", creditCardPay.getLine1());
        addOKHttpRequestBody(builder, "line2", creditCardPay.getLine2());
        addOKHttpRequestBody(builder,"state", creditCardPay.getState());
        addOKHttpRequestBody(builder,  "postCode", creditCardPay.getPostCode());
        addOKHttpRequestBody(builder, "country", creditCardPay.getCountry());
        addOKHttpRequestBody(builder, "phone", creditCardPay.getPhone());
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
            client.newCall(HttpUtil.postRequest(URL_CREDIT_CARD_PAY, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_CREDIT_CARD_PAY) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.PAY_SUCCESS, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                // 联网成功
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    PaypalSuccess paypalSuccess = gson.fromJson(bean.data.toString(), PaypalSuccess.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.PAY_SUCCESS, bean.msg, paypalSuccess);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.PAY_SUCCESS, bean.msg, bean.code);
                }
            }
        });

    }

    @Override
    public void verifyPayment(final String paynowid, final String orderId, final String payid, final String token, final ServiceListener listener) {
        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder,"paynowid", paynowid);
        addOKHttpRequestBody(builder, "oid", orderId);
        addOKHttpRequestBody(builder, "payid", payid);
        addOKHttpRequestBody(builder, "token", token);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_PAY_ORDER, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_PAY_ORDER) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.PAY_SUCCESS, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                // 联网成功
                if (bean.code ==  ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    PaypalSuccess paypalSuccess =gson.fromJson(bean.data.toString(), PaypalSuccess.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.PAY_SUCCESS, bean.msg, paypalSuccess);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.PAY_SUCCESS, bean.msg, bean.code);
                }
            }
        });

    }

    @Override
    public void getPayToken(final ServiceListener listener) {
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_ACCESS_TOKEN, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_ACCESS_TOKEN) {
            @Override
            public void onFailure(Request request, IOException e) {
                // 联网失败，断网，超时等获取的信息
                handleServiceFailure(listener, ServiceListener.ActionTypes.PAYMENT_TOKEN, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                ILog.e("hellolove","gettokenback");
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    JSONObject jsonObject = (JSONObject) bean.data;
                    try {
                        ILog.e("hellolove","tokenerror");
                        String token = jsonObject.getString("token");
                        ILog.e("hellolove",token);
                        handleServiceSuccess(listener, ServiceListener.ActionTypes.PAYMENT_TOKEN, null, token);
                    } catch (Exception e) {
                        handleServiceFailure(listener, ServiceListener.ActionTypes.PAYMENT_TOKEN, bean.msg, bean.code);
                        e.printStackTrace();
                    }
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.PAYMENT_TOKEN, bean.msg, bean.code);
                }
            }
        });

    }
}
