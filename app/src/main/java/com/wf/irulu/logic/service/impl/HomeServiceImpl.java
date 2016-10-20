package com.wf.irulu.logic.service.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.HomeList;
import com.wf.irulu.common.bean.HomeProduct;
import com.wf.irulu.common.bean.InitAppBean;
import com.wf.irulu.common.bean.StartAdvertising;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.FileUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.TimeValConstant;
import com.wf.irulu.logic.ConfigXML;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.listener.ServiceListener.ActionTypes;
import com.wf.irulu.logic.manager.CacheManager;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.logic.service.HomeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * @描述: 主页接口定义实现
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.service.impl
 * @类名:HomeServiceImpl
 * @作者: 左杰
 * @创建时间:2015/10/24 11:26
 */
public class HomeServiceImpl extends BaseService implements HomeService {

    private final String TAG = getClass().getCanonicalName();

    private IruluController controller;
    private ServiceManager serviceManager;

    public HomeServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }

    @Override
    public void startAdvertising(final ServiceListener listener) {
        client.newCall(HttpUtil.getRequest(URL_AD, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_AD) {
            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ServiceListener.ActionTypes.START_ADVERTISING, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    StartAdvertising startAdvertising = gson.fromJson(bean.data.toString(), StartAdvertising.class);
                    handleServiceSuccess(listener, ActionTypes.START_ADVERTISING, null, startAdvertising);
                } else {
                    handleServiceFailure(listener, ActionTypes.START_ADVERTISING, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void appInit(final boolean isFirst, final ServiceListener listener) {
        Headers headers = addHeadersFromOKHttp(controller);
        String tempurl=URL_INIT;
        if(tempurl.startsWith("https")){
            tempurl=tempurl.replace("https","http");
        }
        Request rq = HttpUtil.getRequest(tempurl, headers, null);
        client.newCall(rq).enqueue(new RequestCallBack(URL_INIT) {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                 if (isFirst) {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.APP_INIT, null, ErrorCodeUtils.NO_NET_RESPONSE);
                }
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    InitAppBean initAppBean = new Gson().fromJson(bean.data.toString(), InitAppBean.class);
                    String priceSymbol = initAppBean.getPriceSymbol();//货币符号
                    int nativePaymentEnabled=initAppBean.getNativePaymentEnabled();//支付方式
                    ILog.e("hellolove",nativePaymentEnabled+"");
                    //将货币符号存在sp中
                    CacheUtils.setString(IruluApplication.getInstance(), "priceSymbol", priceSymbol);
                    CacheUtils.setInt(IruluApplication.getInstance(),"nativePaymentEnabled",nativePaymentEnabled);
                    // 设置到缓存
                    CacheUtils.setString(IruluApplication.getInstance(), "initAppBean", bean.data.toString());
                    if (isFirst) {
                        handleServiceSuccess(listener, ServiceListener.ActionTypes.APP_INIT, null, null);
                    }
                }
            }
        });
    }

    @Override
    public void getHomeData(final ServiceListener listener) {
        final Gson gson = new Gson();
        final String savePath = CacheManager.getJsonCacheMyVisitorPath();
        String jsonStr = FileUtils.readFileToString(savePath);
        if (!TextUtils.isEmpty(jsonStr)) {
            HomeList homeList = null;
            try {
                homeList = gson.fromJson(jsonStr, HomeList.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            handleServiceCallback(listener, ActionTypes.HOME, ServiceListener.TYPE_CACHE_DATA, homeList);

        }

        Headers headers = addHeadersFromOKHttp(controller);
        Request rq = HttpUtil.getRequest(URL_HOME, headers, null);
        client.newCall(rq).enqueue(new RequestCallBack(URL_HOME) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ActionTypes.HOME, null, ErrorCodeUtils.NO_NET_RESPONSE);
                e.printStackTrace();
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    HomeList homeList = null;
                    try {
                        homeList = gson.fromJson(bean.data.toString(), HomeList.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    handleServiceSuccess(listener, ActionTypes.HOME, null, homeList);
                    FileUtils.saveFileByString(bean.data.toString(), savePath);
                } else {
                    handleServiceFailure(listener, ActionTypes.HOME, bean.msg, bean.code);
                }
            }
        });
    }

    @Override
    public void getHomeDataMore(final String id, final String page, final String num, final boolean isPullDownToRefresh, final ServiceListener listener) {
        final ConfigXML config = new ConfigXML(controller);
        if (isPullDownToRefresh) {//是下拉刷新
            // 时间限制
            long time = System.currentTimeMillis();
            long homeDataMoreTime = config.read("getHomeDataMore_PullDown_" + id, 0L);
            if (Math.abs(time - homeDataMoreTime) < TimeValConstant.TIME_PULL_REFRESH) {
                handleServiceCallback(listener, ActionTypes.HOME_MORE, ServiceListener.TYPE_REFRESH_TIME_VAL, null);
                return;
            }
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("page", page);
        params.put("num", num);
//        ILog.e("hellolove",URL_PRODUCT_MORE);
        String tempurl=URL_PRODUCT_MORE;
        client.newCall(HttpUtil.getRequest(URL_PRODUCT_MORE, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_PRODUCT_MORE) {

            @Override
            public void onFailure(Request request, IOException e) {
                handleServiceFailure(listener, ActionTypes.HOME_MORE, page, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(bean.data.toString());
                        if (jsonObj.has("list")) {
                            JSONObject list = jsonObj.getJSONObject("list");
                            ILog.e("hellolove",list.toString());
                            Gson gson = new Gson();
                            HomeProduct homeProduct = null;
                            try {
                                String s = list.toString();
                                homeProduct = gson.fromJson(s, HomeProduct.class);
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            handleServiceSuccess(listener, ActionTypes.HOME_MORE, page, homeProduct);
                            if (isPullDownToRefresh)
                                config.save("getHomeDataMore_PullDown_" + id, System.currentTimeMillis());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    handleServiceFailure(listener, ActionTypes.HOME_MORE, page, bean.code);
                }
            }
        });
    }
}
