package com.wf.irulu.logic.service;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.utils.HttpConstantUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.listener.ServiceListener.ActionTypes;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @描述: 接口调用基类
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:BaseService
 * @作者: 左西杰
 * @创建时间:2015-6-4 下午2:53:41
 */
public class BaseService {

    public final OkHttpClient client;

    public BaseService() {
        client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);

    }

    /**
     * 请求网络成功的回调，异步
     *
     * @param listener
     * @param actionType
     * @param bandObj
     * @param returnObj
     */
    public void handleServiceSuccess(final ServiceListener listener,
                                     final ActionTypes actionType, final Object bandObj, final Object returnObj) {
        if (null != listener) {
            IruluController.mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.serviceSuccess(actionType, bandObj, returnObj);
                }
            });
        }
    }

    /**
     * 请求网络成功的回调，同步
     *
     * @param listener
     * @param actionType
     * @param bandObj
     * @param returnObj
     */
    public void handleServiceSuccessSync(final ServiceListener listener, final ActionTypes actionType, final Object bandObj, final Object returnObj) {
        if (null != listener) {
            listener.serviceSuccess(actionType, bandObj, returnObj);
        }
    }

    /**
     * 请求网络失败的回调，异步
     *
     * @param listener
     * @param actionType
     * @param returnObj
     * @param errorCode
     */
    public void handleServiceFailure(final ServiceListener listener,
                                     final ActionTypes actionType, final Object returnObj, final int errorCode) {
        if (null != listener) {
            IruluController.mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.serviceFailure(actionType, returnObj, errorCode);
                }
            });
        }
    }

    /**
     * 请求网络失败的回调，同步
     *
     * @param listener
     * @param actionType
     * @param returnObj
     * @param errorCode
     */
    public void handleServiceFailureSync(final ServiceListener listener, final ActionTypes actionType, final Object returnObj, final int errorCode) {
        if (null != listener) {
            listener.serviceFailure(actionType, returnObj, errorCode);
        }
    }

    /**
     * 请求网络后缓存的回调，异步
     *
     * @param listener
     * @param actionType
     * @param type
     * @param returnObj
     */
    public void handleServiceCallback(final ServiceListener listener,
                                      final ActionTypes actionType, final int type, final Object returnObj) {
        if (null != listener) {
            IruluController.mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.serviceCallback(actionType, type, returnObj);
                }
            });
        }
    }

    /**
     * 请求网络后缓存的回调，同步
     *
     * @param listener
     * @param actionType
     * @param type
     * @param returnObj
     */
    public void handleServiceCallbackSync(final ServiceListener listener, final ActionTypes actionType, final int type, final Object returnObj) {
        if (null != listener) {
            listener.serviceCallback(actionType, type, returnObj);
        }
    }

    /**
     * 添加HTTP头传值
     *
     * @param headerMap
     * @param key
     * @param value
     */
    public void addToHeaderMap(Map<String, String> headerMap, String key, String value) {
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value) && null != headerMap) {
            headerMap.put(key, value);
        }
    }

    /**
     * 添加OKHTTP的请求参数
     *
     * @param builder
     * @param name
     * @param value
     */
    public void addOKHttpRequestBody(FormEncodingBuilder builder, String name, String value) {
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value) && null != builder) {
            builder.add(name, value);
        }
    }

    /**
     * OKHttp 联网框架获取登陆前信息头
     *
     * @return
     */
    public Headers unLoginHeader() {
        Headers.Builder builder = new Headers.Builder();
        builder.add(HttpConstantUtils.X_APP_DEVICE, HttpConstantUtils.DEVICE);
        builder.add(HttpConstantUtils.X_APP_VERSION, HttpConstantUtils.VERSION);
        builder.add(HttpConstantUtils.X_API_VERSION, HttpConstantUtils.API_VERSION);
        builder.add(HttpConstantUtils.X_DID, HttpConstantUtils.DID);
        builder.add(HttpConstantUtils.X_UTM, HttpConstantUtils.getMetaData());
        builder.add(HttpConstantUtils.USER_AGENT, HttpConstantUtils.getUserAgent());
        return builder.build();
    }

    /**
     * OKHttp 联网框架获取登陆后信息头
     *
     * @return
     */
    public Headers loginHeader(IruluController controller) {
        LoginUser loginUser = controller.getCacheManager().getLoginUser();
        Headers.Builder builder = new Headers.Builder();
        builder.add(HttpConstantUtils.X_APP_DEVICE, HttpConstantUtils.DEVICE);
        builder.add(HttpConstantUtils.X_APP_VERSION, HttpConstantUtils.VERSION);
        builder.add(HttpConstantUtils.X_API_VERSION, HttpConstantUtils.API_VERSION);
        builder.add(HttpConstantUtils.X_DID, HttpConstantUtils.DID);
        builder.add(HttpConstantUtils.X_UTM, HttpConstantUtils.getMetaData());
        builder.add(HttpConstantUtils.USER_AGENT, HttpConstantUtils.getUserAgent());
        builder.add(HttpConstantUtils.X_UID, String.valueOf(loginUser.getUid()));
        builder.add(HttpConstantUtils.X_TOKEN, String.valueOf(loginUser.getToken()));
        builder.add(HttpConstantUtils.X_USER_TYPE, String.valueOf(loginUser.getUserType()));
        ILog.e("hellolove", builder.build().toString());
        return builder.build();
    }

    /**
     * OKHttp 联网框架获取登陆时信息头
     *
     * @return
     */
    public Headers logingHeader(String type) {
        Headers.Builder builder = new Headers.Builder();
        builder.add(HttpConstantUtils.X_APP_DEVICE, HttpConstantUtils.DEVICE);
        builder.add(HttpConstantUtils.X_APP_VERSION, HttpConstantUtils.VERSION);
        builder.add(HttpConstantUtils.X_API_VERSION, HttpConstantUtils.API_VERSION);
        builder.add(HttpConstantUtils.X_DID, HttpConstantUtils.DID);
        builder.add(HttpConstantUtils.X_UTM, HttpConstantUtils.getMetaData());
        builder.add(HttpConstantUtils.USER_AGENT, HttpConstantUtils.getUserAgent());
        builder.add("X-User-Type", type);
//		builder.add(HttpConstantUtils.X_USER_TYPE, String.valueOf(loginUser.getUserType()));
        return builder.build();
    }

    /**
     * OKHttp 联网框架获取对应信息头
     *
     * @return
     */
    public Headers addHeadersFromOKHttp(IruluController controller) {
        LoginUser loginUser = controller.getCacheManager().getLoginUser();
        int uid = loginUser.getUid();
        if (uid == 0 || uid == -1) {
            return unLoginHeader();
        } else {
            return loginHeader(controller);
        }
    }

    public static Map<String, String> getWebHead(IruluController controller) {
        LoginUser loginUser = controller.getCacheManager().getLoginUser();
        int uid = loginUser.getUid();
        if (uid == 0 || uid == -1) {
            return getWebHeadunLogin();
        } else {
            return getWebHeadtLogin(controller);
        }
    }

    public static Map<String, String> getWebHeadunLogin() {
        Map<String, String> headMap = new HashMap<>();

        headMap.put(HttpConstantUtils.X_APP_DEVICE, HttpConstantUtils.DEVICE);
        headMap.put(HttpConstantUtils.X_APP_VERSION, HttpConstantUtils.VERSION);
        headMap.put(HttpConstantUtils.X_API_VERSION, HttpConstantUtils.API_VERSION);
        headMap.put(HttpConstantUtils.X_DID, HttpConstantUtils.DID);
        headMap.put(HttpConstantUtils.X_UTM, HttpConstantUtils.getMetaData());
        headMap.put(HttpConstantUtils.USER_AGENT, HttpConstantUtils.getUserAgent());

        return headMap;
    }

    public static Map<String, String> getWebHeadtLogin(IruluController controller) {
        Map<String, String> headMap = new HashMap<>();
        LoginUser loginUser = controller.getCacheManager().getLoginUser();

        headMap.put(HttpConstantUtils.X_APP_DEVICE ,HttpConstantUtils.DEVICE);
        headMap.put(HttpConstantUtils.X_APP_VERSION, HttpConstantUtils.VERSION);
        headMap.put(HttpConstantUtils.X_API_VERSION, HttpConstantUtils.API_VERSION);
        headMap.put(HttpConstantUtils.X_DID, HttpConstantUtils.DID);
        headMap.put(HttpConstantUtils.X_UTM, HttpConstantUtils.getMetaData());
        headMap.put(HttpConstantUtils.USER_AGENT, HttpConstantUtils.getUserAgent());
        headMap.put(HttpConstantUtils.X_UID, String.valueOf(loginUser.getUid()));
        headMap.put(HttpConstantUtils.X_TOKEN, String.valueOf(loginUser.getToken()));
        headMap.put(HttpConstantUtils.X_USER_TYPE, String.valueOf(loginUser.getUserType()));

        return headMap;
    }
}
