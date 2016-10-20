package com.wf.irulu.logic.service.impl;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.wf.irulu.common.bean.CheckoutBean;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.CreateOrder;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.RefundDetailsDataBean;
import com.wf.irulu.common.bean.TrackingInforBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.logic.service.OrderService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 订单相关Service接口的实现类
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.service.impl
 * @类名:OrderServiceImpl
 * @作者: 左杰
 * @创建时间:2015/10/24 11:14
 */
public class OrderServiceImpl extends BaseService implements OrderService {

    private static final String TAG = AASServiceImpl.class.getCanonicalName();

    private IruluController controller;
    private ServiceManager serviceManager;

    public OrderServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }

    @Override
    public void checkout(final String coupon, final String addressId, final String mobile,final ServiceListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("coupon", coupon);
        map.put("addressId", addressId);
        map.put("mobile", mobile);
        client.newCall(HttpUtil.getRequest(URL_CHECKOUT, addHeadersFromOKHttp(controller), map)).enqueue(new RequestCallBack(URL_CHECKOUT) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.PRODUCT_CHECKOUT, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                int code = bean.code;
                if (code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    CheckoutBean checkoutBean = gson.fromJson(bean.data.toString(), CheckoutBean.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.PRODUCT_CHECKOUT, null, checkoutBean);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.PRODUCT_CHECKOUT, bean.msg, bean.code);
                }
            }
        });
    }

    /**
     * 售后详情
     * @param servId
     * @param listener
     */
    @Override
    public void getServiceDetails(final String servId, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("servId", servId);
        client.newCall(HttpUtil.postRequest(URL_SERVICE_DETAIL, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_SERVICE_DETAIL) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.SERVICE_DETAILS, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    RefundDetailsDataBean rddb = gson.fromJson(bean.data.toString(), RefundDetailsDataBean.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.SERVICE_DETAILS, null, rddb);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.SERVICE_DETAILS, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void createOrder(final String productId, final String pskuid, final String quantity, final String coupon,final String mobile, final String addressId, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        String url = URL_CREATEORDER;
        if (!TextUtils.isEmpty(productId)) {
            builder.add("productId", productId);
            url = URL_CREATEORDER_BUYNOW;
        }
        if (!TextUtils.isEmpty(pskuid)) {
            builder.add("pskuid", pskuid);
        }
        if (!TextUtils.isEmpty(quantity)) {
            builder.add("quantity", quantity);
        }
        builder.add("addressId", addressId);
        if (!TextUtils.isEmpty(coupon)) {
            builder.add("coupon", coupon);
        }
        builder.add("mobile", mobile);
        client.newCall(HttpUtil.postRequest(url, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(url) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.CREATE_ORDER, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    CreateOrder createOrder = gson.fromJson(bean.data.toString(), CreateOrder.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.CREATE_ORDER, null, createOrder);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.CREATE_ORDER, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void buyNow(final String productId, final String pskuid, final String quantity, final String coupon, final String addressId, final String mobile ,final ServiceListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        String url = URL_CHECKOUT_BUYNOW;
        if (!TextUtils.isEmpty(productId)) {
            map.put("productId", productId);
        } else {
            url = URL_CHECKOUT;
        }
        if (!TextUtils.isEmpty(pskuid)) {
            map.put("pskuid", pskuid);
        }
        if (!TextUtils.isEmpty(quantity)) {
            map.put("quantity", quantity);
        }
        if (!TextUtils.isEmpty(coupon)) {
            map.put("coupon", coupon);
        }
        if(!TextUtils.isEmpty(addressId)){
            map.put("addressId", addressId);
        }
        map.put("mobile", mobile);
        client.newCall(HttpUtil.getRequest(url, addHeadersFromOKHttp(controller), map)).enqueue(new RequestCallBack(url) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.PRODUCT_BUYNOW, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    CheckoutBean checkoutBean = gson.fromJson(bean.data.toString(), CheckoutBean.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.PRODUCT_BUYNOW, null, checkoutBean);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.PRODUCT_BUYNOW, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void getOrderDetail(final String orderid,final ServiceListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderid", orderid);
        client.newCall(HttpUtil.getRequest(URL_DETAIL, addHeadersFromOKHttp(controller), map)).enqueue(new RequestCallBack(URL_DETAIL) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_DETAIL, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    OrderDetail orderDetail = gson.fromJson(bean.data.toString(), OrderDetail.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ORDER_DETAIL, null, orderDetail);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_DETAIL, bean.msg, bean.code);
                }
            }
        });
        handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, false);
    }

    @Override
    public void cancelOrder(final String orderid,final ServiceListener listener) {
        handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_CANCEL, ServiceListener.TYPE_LOADINGING, true);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderid", orderid);
        client.newCall(HttpUtil.postRequest(URL_CANCELORDER, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_CANCELORDER) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_CANCEL, null, ErrorCodeUtils.NO_NET_RESPONSE);
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_CANCEL, ServiceListener.TYPE_LOADINGING, false);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ORDER_CANCEL, null, null);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_CANCEL, bean.msg, bean.code);
                }

                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_CANCEL, ServiceListener.TYPE_LOADINGING, false);
            }
        });
        handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_CANCEL, ServiceListener.TYPE_LOADINGING, true);
    }

    @Override
    public void deleteOrder(final String orderid,final ServiceListener listener) {
        handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, true);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderid", orderid);
        client.newCall(HttpUtil.postRequest(URL_DELORDER, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_DELORDER) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_DELETE, null, ErrorCodeUtils.NO_NET_RESPONSE);
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, false);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ORDER_DELETE, null, null);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_DELETE, bean.msg, bean.code);
                }
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, false);
            }
        });
    }

    @Override
    public void confirmOrder(final String orderid, final ServiceListener listener) {
        handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, true);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderid", orderid);
        client.newCall(HttpUtil.postRequest(URL_CONFIRMORDER, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_CONFIRMORDER) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_CONFIRM, null, ErrorCodeUtils.NO_NET_RESPONSE);
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, false);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ORDER_CONFIRM, null, null);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_CONFIRM, bean.msg, bean.code);
                }
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_DETAIL, ServiceListener.TYPE_LOADINGING, false);
            }
        });
    }

    @Override
    public void getLogisticsInfor(final String orderid, final ServiceListener listener) {
        HashMap<String, String> params = new HashMap<String, String>() {
            {
                put( "orderid", orderid);
            }
        };
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_GETLOGISTICS, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_GETLOGISTICS) {
            @Override
            public void onFailure(Request request, IOException e) {
                // 联网失败，断网，超时等获取的信息
                handleServiceFailure(listener, ServiceListener.ActionTypes.LOGISTICS_TRACKING, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean){
                    if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                        Gson gson = new Gson();
                        TrackingInforBean tib = gson.fromJson(bean.data.toString(), TrackingInforBean.class);
                        handleServiceSuccess(listener, ServiceListener.ActionTypes.LOGISTICS_TRACKING, null, tib);
                    } else {
                        handleServiceFailure(listener, ServiceListener.ActionTypes.LOGISTICS_TRACKING, bean.msg, bean.code);
                    }
            }
        });
    }
}
