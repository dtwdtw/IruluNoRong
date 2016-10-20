package com.wf.irulu.logic.service.impl;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.OrderInfo;
import com.wf.irulu.common.bean.ShoppingCart;
import com.wf.irulu.common.bean.WishList;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.parser.SearchParser;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.logic.service.ShoppingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XImoon on 15/10/19.
 */
public class ShoppingServiceImpl extends BaseService implements ShoppingService {

    private static final String TAG = ShoppingServiceImpl.class.getCanonicalName();

    private IruluController controller;
    private ServiceManager serviceManager;

    public ShoppingServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }

    /**
     * 添加到心愿单
     *
     * @param productId 商品id
     * @param listener
     */
    @Override
    public void addToWishList(final String productId, final ServiceListener listener) {
        Map<String, String> builder = new HashMap<String, String>();
        builder.put("productId", productId);
        client.newCall(HttpUtil.getRequest(URL_SHOPPING_WISH_ADD, addHeadersFromOKHttp(controller), builder)).enqueue(new RequestCallBack(URL_SHOPPING_WISH_ADD) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ADD_WISH, null, ErrorCodeUtils.NO_NET_RESPONSE);

            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    WishList wishList = gson.fromJson(bean.data.toString(), WishList.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ADD_WISH, productId, wishList);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ADD_WISH, bean.msg, bean.code);
                }
            }
        });
    }

    /**
     * TODO
     *
     * @param likesId
     * @param listener
     */
    @Override
    public void deleteToWishList(final String likesId, final String productId, final ServiceListener listener) {
        Map<String, String> builder = new HashMap<String, String>();
        builder.put("likesId", likesId);
        builder.put("productId", productId);
        client.newCall(HttpUtil.getRequest(URL_SHOPPING_WISH_DEL, addHeadersFromOKHttp(controller), builder)).enqueue(new RequestCallBack(URL_SHOPPING_WISH_DEL) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.DEL_WISH, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    WishList wishList = gson.fromJson(bean.data.toString(), WishList.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.DEL_WISH, productId, wishList);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.DEL_WISH, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void addToCart(final String productId, final String pskuid, final int quantity, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("productId", productId);
        builder.add("pskuid", pskuid);
        builder.add("quantity", String.valueOf(quantity));
        client.newCall(HttpUtil.postRequest(URL_SHOPPING_CART_ADD, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_SHOPPING_CART_ADD) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ADD_CART, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    JSONObject jsonObject = (JSONObject) bean.data;
                    Integer count = 0;
                    try {
                        count = jsonObject.getInt("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ADD_CART, null, count);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ADD_CART, null, bean.code);
                }
            }
        });
    }

    @Override
    public void buyNow(final String pId, final String id, final int quantity, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("id", id);
        builder.add("quantity", String.valueOf(quantity));
        client.newCall(HttpUtil.postRequest(URL_SHOPPING_BUY_BOW, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_SHOPPING_BUY_BOW) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ADD_CART, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.PRODUCT_BUYNOW, null, null);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.PRODUCT_BUYNOW, null, bean.code);
                }
            }
        });
    }

    @Override
    public void getCartInfo(final ServiceListener listener) {
        Headers headers = addHeadersFromOKHttp(controller);
        Request rq = HttpUtil.getRequest(URL_GET_CART_INFO, headers, null);
        client.newCall(rq).enqueue(new RequestCallBack(URL_GET_CART_INFO) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.GET_CART_INFO, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    ShoppingCart shoppingCart = gson.fromJson(bean.data.toString(), ShoppingCart.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.GET_CART_INFO, bean.msg, shoppingCart);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.GET_CART_INFO, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void updataProductNum(final String id, final String quantity, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("id", id);
        builder.add("quantity", String.valueOf(quantity));
        client.newCall(HttpUtil.postRequest(URL_UPDATE_PRODUCT, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_UPDATE_PRODUCT) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.UPDATA_PRODUCT_NUM, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    ShoppingCart shoppingCart = gson.fromJson(bean.data.toString(), ShoppingCart.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.UPDATA_PRODUCT_NUM, bean.msg, shoppingCart);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.UPDATA_PRODUCT_NUM, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void removeProduct(final String id, final String sku_id, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("id", id);
        builder.add("sku_id", sku_id);
        client.newCall(HttpUtil.postRequest(URL_REMOVE_PRODUCT, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_REMOVE_PRODUCT) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.CART_REMOVE_PRODUCT, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    ShoppingCart shoppingCart = gson.fromJson(bean.data.toString(), ShoppingCart.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.CART_REMOVE_PRODUCT, bean.msg, shoppingCart);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.CART_REMOVE_PRODUCT, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void getOrders(final int num, final int page, final int type, final ServiceListener listener) {
        handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_LIST, ServiceListener.TYPE_LOADINGING, true);
        Map<String, String> builder = new HashMap<String, String>();
        builder.put("num", String.valueOf(num));
        builder.put("page", String.valueOf(page));
        builder.put("type", String.valueOf(type));
        client.newCall(HttpUtil.getRequest(URL_ORDER_LIST, addHeadersFromOKHttp(controller), builder)).enqueue(new RequestCallBack(URL_ORDER_LIST) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_LIST, null, ErrorCodeUtils.NO_NET_RESPONSE);
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_LIST, ServiceListener.TYPE_LOADINGING, false);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    OrderInfo info = new Gson().fromJson(bean.data.toString(), OrderInfo.class);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ORDER_LIST, page, info);
                    if (type == 0 || type == 2) {
                        CacheUtils.setLong(IruluApplication.getInstance(), "unpaidOrderNum", info.getNotpay().getCount());
                        controller.postUnpaidOrderCountCallback(info.getNotpay().getCount());
                    }
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_LIST, page, bean.code);
                }
                handleServiceCallback(listener, ServiceListener.ActionTypes.ORDER_LIST, ServiceListener.TYPE_LOADINGING, false);
            }
        });
    }

    @Override
    public void getWishIds(final ServiceListener listener) {
        client.newCall(HttpUtil.getRequest(URL_WISH_IDS, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_WISH_IDS) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_LIST, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    ArrayList<String> mIDs = SearchParser.parserHotSearch(bean.data);
                    handleServiceSuccess(listener, ServiceListener.ActionTypes.ORDER_LIST, null, mIDs);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.ORDER_LIST, null, bean.code);
                }
            }
        });
    }
}
