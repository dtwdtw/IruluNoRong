package com.wf.irulu.logic.service.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.ContactUsTag;
import com.wf.irulu.common.bean.CountryInfor;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.bean.NewVersionInfors;
import com.wf.irulu.common.bean.QiNiuReturnBean;
import com.wf.irulu.common.bean.ShippingAddr;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.bean.StateInfo;
import com.wf.irulu.common.bean.UploadPicKeyTokenBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.CommonJsonParser;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.LogUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.component.qiniu.storage.Configuration;
import com.wf.irulu.component.qiniu.storage.UpCompletionHandler;
import com.wf.irulu.component.qiniu.storage.UploadManager;
import com.wf.irulu.component.qiniu.storage.UploadOptions;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.listener.ServiceListener.ActionTypes;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.service.AASService;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.module.user.activity.AddNewAddressActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.http.HEAD;


/**
 * Created by XImoon on 15/10/19.
 */
public class AASServiceImpl extends BaseService implements AASService {
    private final String TAG = getClass().getCanonicalName();

    private IruluController controller;
    private ServiceManager serviceManager;

    public AASServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }

    /**
     * 获取上传图片的token
     *
     * @param uid
     * @param url
     * @param type
     * @param key
     * @param listener
     */
    @Override
    public void getUpLoadImageToken(final String uid, final String url, final String type, final String key, final ServiceListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        /**
         * 获取图片的格式
         */
        final String e = url.substring(url.lastIndexOf(".") + 1);
        HashMap<String, String> params = new HashMap<String, String>() {
            {
                put("uid", uid);
                put("ext", e);
                put("type", type);
                put("key", key);
            }
        };
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_GET_PICTURE_TOKEN, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_GET_PICTURE_TOKEN, ActionTypes.UPLOAD_TOKEN) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {

                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {

                    UploadPicKeyTokenBean ub = new Gson().fromJson(bean.data.toString(), UploadPicKeyTokenBean.class);
                    handleServiceSuccess(listener, actionTypes, url, ub);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 上传图片到七牛
     *
     * @param lisener
     * @param key
     * @param token
     * @param pic
     * @param context
     */
    @Override
    public void qiNiuUpload(final ServiceListener lisener, String key, String token, final String pic, final Context context) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(token) || TextUtils.isEmpty(pic)) {
            return;
        }
        Configuration config = new Configuration.Builder().chunkSize(256 * 1024) // 分片上传时，每片的大小。默认 256k
                .putThreshhold(512 * 1024) // 启用分片上传阀值。默认 512K
                .connectTimeout(10) // 链接超时。默认 10秒
                .responseTimeout(20) // 服务器响应超时。默认 60秒
                .build();
        UploadManager uploadManager = new UploadManager(config);

        // File对象，或文件路径，或字节数组
        String storageState = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(storageState)) {
            Toast.makeText(context, "sdcard ：" + storageState, Toast.LENGTH_SHORT).show();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("x:foo", "fooval");
        final UploadOptions opt = new UploadOptions(params, null, true, null, null);
        uploadManager.put(pic, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String k, com.wf.irulu.component.qiniu.http.ResponseInfo rinfo, JSONObject response) {
                ILog.d(context.getClass().getCanonicalName(), k + rinfo);
                CommonJsonParser sjp = new CommonJsonParser();
                if (sjp == null || response == null) {
                    PageLoadDialog.hideDialogForLoading();
                    return;
                }
                CommonServiceBean csb = sjp.commonParser(response.toString());
                if (rinfo.isOK()) {
                    QiNiuReturnBean fromJson = new Gson().fromJson(csb.data.toString(), QiNiuReturnBean.class);
                    Log.e("fromjaosn", "上传成功");
                    handleServiceSuccess(lisener, ActionTypes.QINIU_UPLOAD, pic, fromJson);

                } else {
                    /**
                     * 上传七牛图片失败
                     */
                    handleServiceFailure(lisener, ActionTypes.QINIU_UPLOAD, csb.msg, csb.code);
                    return;
                }
            }

        }, opt);
    }

    /**
     * 登录
     *
     * @param email
     * @param password
     * @param listener
     */
    @Override
    public void login(final String email, final String password, final String openid, final int type, final ServiceListener listener) {

        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        if (type == 1) {
            addOKHttpRequestBody(builder, "email", email);
            addOKHttpRequestBody(builder, "password", password);
        } else {
            addOKHttpRequestBody(builder, "openid", openid);
        }
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_LOGIN, logingHeader(String.valueOf(type)), builder.build())).enqueue(new RequestCallBack(URL_LOGIN, ActionTypes.LOGIN) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    LoginUser loginUser = new Gson().fromJson(bean.data.toString(), LoginUser.class);
                    handleServiceSuccess(listener, actionTypes, type, loginUser);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void login(String email, String gender, String link, String locale, String name,String weblink, final String openid, final int type, final ServiceListener listener) {

        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "email", email);
        addOKHttpRequestBody(builder, "openid", openid);
        addOKHttpRequestBody(builder, "gender", gender);
        addOKHttpRequestBody(builder, "photo_url", link);
        addOKHttpRequestBody(builder, "address", locale);
        addOKHttpRequestBody(builder, "display_name", name);
        addOKHttpRequestBody(builder, "first_name", name);
        addOKHttpRequestBody(builder, "last_name", "");
        addOKHttpRequestBody(builder, "website_url", weblink);

        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_LOGIN, logingHeader(String.valueOf(type)), builder.build())).enqueue(new RequestCallBack(URL_LOGIN, ActionTypes.LOGIN) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    LoginUser loginUser = new Gson().fromJson(bean.data.toString(), LoginUser.class);
                    handleServiceSuccess(listener, actionTypes, type, loginUser);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }
    @Override
    public void login(String email,String link, String locale,String name, String screenname,String description,String website,String openid, final int type, final ServiceListener listener) {

        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "email", email);
        addOKHttpRequestBody(builder, "openid", openid);
        addOKHttpRequestBody(builder, "photo_url", link);
        addOKHttpRequestBody(builder, "address", locale);
        addOKHttpRequestBody(builder, "display_name", screenname);
        addOKHttpRequestBody(builder, "first_name", name.split(" ")[0]);
        addOKHttpRequestBody(builder, "last_name", name.split(" ").length>1?name.split(" ")[1]:"");
        addOKHttpRequestBody(builder,"description",description);
        addOKHttpRequestBody(builder,"website_url",website);


        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_LOGIN, logingHeader(String.valueOf(type)), builder.build())).enqueue(new RequestCallBack(URL_LOGIN, ActionTypes.LOGIN) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    LoginUser loginUser = new Gson().fromJson(bean.data.toString(), LoginUser.class);
                    handleServiceSuccess(listener, actionTypes, type, loginUser);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 注册
     *
     * @param email
     * @param password
     * @param froms
     * @param listener
     */
    @Override
    public void register(final String firstname, final String lastname, final String email, final String password, String froms, final ServiceListener listener) {
        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "email", email);
        addOKHttpRequestBody(builder, "firstname", firstname);
        addOKHttpRequestBody(builder, "lastname", lastname);
        addOKHttpRequestBody(builder, "password", password);
        RequestBody requestBody = builder.build();
        Request rq = HttpUtil.postRequest(URL_REGISTER, addHeadersFromOKHttp(controller), requestBody);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_REGISTER, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_REGISTER, ActionTypes.REGISTER) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    LoginUser loginUser = new Gson().fromJson(bean.data.toString(), LoginUser.class);
                    handleServiceSuccess(listener, actionTypes, null, loginUser);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 忘记密码
     *
     * @param email
     * @param listener
     */
    @Override
    public void forgetPassword(final String email, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "email", email);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_FORGOT_PASSWORD, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_FORGOT_PASSWORD, ActionTypes.FIND_PASSWORD) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 更改昵称
     *
     * @param nickname
     * @param listener
     */
    @Override
    public void updateNickName(final String nickname, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "nickname", nickname);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_UPDATEUSERINFO, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_UPDATEUSERINFO, ActionTypes.UPDATE_NICKNAME) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, nickname, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 更改生日
     *
     * @param birthday
     * @param listener
     */
    @Override
    public void updateBirthday(final String birthday, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "birthday", birthday);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_UPDATEUSERINFO, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_UPDATEUSERINFO, ActionTypes.UPDATE_BIRTHDAY) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, birthday, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });

    }

    /**
     * 获取所有的发货地址
     *
     * @param listener
     */
    @Override
    public void getAllAddress(final ServiceListener listener) {
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_GETADDRESS, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_GETADDRESS, ActionTypes.GET_ADDRESS) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    ShippingAddr sa = new Gson().fromJson(bean.data.toString(), ShippingAddr.class);
                    handleServiceSuccess(listener, actionTypes, null, sa);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 设置默认地址
     *
     * @param id       地址的唯一标示
     * @param listener
     */
    @Override
    public void setDefaultAddress(final String id, final int position, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "id", id);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_SET_DEFAULT_ADDRESS, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_SET_DEFAULT_ADDRESS, ActionTypes.DEFAULT_ADDRESS) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, position);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 删除地址
     *
     * @param id
     * @param listener
     */
    @Override
    public void deleteAddress(final String id, final int position, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "id", id);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_DELETE_ADDRESS, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_DELETE_ADDRESS, ActionTypes.DELETE_ADDRESS) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, position);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 添加新地址
     *
     * @param fristname
     * @param lastname
     * @param country
     * @param state
     * @param city
     * @param postcode
     * @param phonenumber
     * @param email
     * @param streetaddress1
     * @param streetaddress2
     * @param isdefault
     * @param listener
     */
    @Override
    public void addAddress(final String fristname, final String lastname, final String country, final String state, final String city, final String postcode, final String phonenumber, final String email, final String streetaddress1, final String streetaddress2, final String isdefault, final ServiceListener listener) {

        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "fristname", fristname);
        addOKHttpRequestBody(builder, "lastname", lastname);
        addOKHttpRequestBody(builder, "country", country);
        addOKHttpRequestBody(builder, "state", state);
        addOKHttpRequestBody(builder, "city", city);
        addOKHttpRequestBody(builder, "postcode", postcode);
        addOKHttpRequestBody(builder, "phonenumber", phonenumber);
        addOKHttpRequestBody(builder, "email", email);
        addOKHttpRequestBody(builder, "streetaddress1", streetaddress1);
        addOKHttpRequestBody(builder, "streetaddress2", streetaddress2);
        addOKHttpRequestBody(builder, "isdefault", isdefault);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_ADD_ADDRESS, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_ADD_ADDRESS, ActionTypes.ADD_ADDRESS) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    ShippingAddr shippingAddr = new Gson().fromJson(bean.data.toString(), ShippingAddr.class);
                    List<ShippingAddrBean> shippingAddrBeanList = shippingAddr.getList();
                    if (shippingAddrBeanList != null && shippingAddrBeanList.size() > 0) {
                        if (shippingAddrBeanList.get(0).getIsDefault() == 1) {
                            handleServiceSuccess(listener, actionTypes, shippingAddrBeanList, shippingAddrBeanList.get(1));
                        } else {
                            handleServiceSuccess(listener, actionTypes, shippingAddrBeanList, shippingAddrBeanList.get(0));
                        }
                    } else {
                        handleServiceSuccess(listener, actionTypes, shippingAddrBeanList, null);
                    }

                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 编辑地址
     *
     * @param id             地址的唯一标示
     * @param fristname
     * @param lastname
     * @param country
     * @param state
     * @param city
     * @param postcode
     * @param phonenumber
     * @param email
     * @param streetaddress1
     * @param streetaddress2
     * @param isdefault
     * @param listener
     */
    @Override
    public void editAddress(final String id, final String fristname, final String lastname, final String country, final String state, final String city, final String postcode, final String phonenumber, final String email, final String streetaddress1, final String streetaddress2, final String isdefault, final ShippingAddrBean sb, final ServiceListener listener) {

        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "id", id);
        addOKHttpRequestBody(builder, "fristname", fristname);
        addOKHttpRequestBody(builder, "lastname", lastname);
        addOKHttpRequestBody(builder, "country", country);
        addOKHttpRequestBody(builder, "state", state);
        addOKHttpRequestBody(builder, "city", city);
        addOKHttpRequestBody(builder, "postcode", postcode);
        addOKHttpRequestBody(builder, "phonenumber", phonenumber);
        addOKHttpRequestBody(builder, "email", email);
        addOKHttpRequestBody(builder, "streetaddress1", streetaddress1);
        addOKHttpRequestBody(builder, "streetaddress2", streetaddress2);
        addOKHttpRequestBody(builder, "isdefault", isdefault);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_UPDATE_ADDRESS, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_UPDATE_ADDRESS, ActionTypes.EDIT_ADDRESS) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    ShippingAddr shippingAddr = gson.fromJson(bean.data.toString(), ShippingAddr.class);
                    List<ShippingAddrBean> shippingAddrBeanList = shippingAddr.getList();
                    handleServiceSuccess(listener, actionTypes, shippingAddrBeanList, sb);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 获取国家信息
     *
     * @param listener
     */
    @Override
    public void getCountryInformation(final ServiceListener listener) {
        final String countryCache = CacheUtils.getString((Activity) listener, "CountryInformationCache");
        LogUtils.d("countryCache=" + countryCache);
        if (!TextUtils.isEmpty(countryCache)) {
            CountryInfor ci = new Gson().fromJson(countryCache, CountryInfor.class);
            handleServiceCallback(listener, ActionTypes.COUNTRY_INFORMATION, 0, ci);
            return;
        }
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_GET_ALL_COUNTRY, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_GET_ALL_COUNTRY, ActionTypes.COUNTRY_INFORMATION) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    CountryInfor ci = new Gson().fromJson(bean.data.toString(), CountryInfor.class);
                    CacheUtils.setString((Activity) listener, "CountryInformationCache", bean.data.toString());
                    handleServiceSuccess(listener, ActionTypes.COUNTRY_INFORMATION, null, ci);

                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 获取地区信息
     *
     * @param listener
     */

    public void getStateInformation(final ServiceListener listener) {
        final String stateInfo = CacheUtils.getString((AddNewAddressActivity) listener, "StateInformationCache");
        LogUtils.d("StateInformationCache==stateInfo=" + stateInfo);
        if (!TextUtils.isEmpty(stateInfo)) {
            HashMap<String, ArrayList<StateInfo.ListEntity>> stateMaps = StateInfo.parseState(stateInfo);
            handleServiceCallback(listener, ActionTypes.STATE_INFORMATION, 0, stateMaps);
            return;
        }
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_GET_ALL_STATE, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_GET_ALL_STATE) {
            @Override
            public void onFailure(Request request, IOException e) {
                // 联网失败，断网，超时等获取的信息
                handleServiceFailure(listener, ServiceListener.ActionTypes.STATE_INFORMATION, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    CacheUtils.getString((AddNewAddressActivity) listener, "StateInformationCache", bean.data.toString());
                    HashMap<String, ArrayList<StateInfo.ListEntity>> stateMaps = StateInfo.parseState(bean.data.toString());
                    handleServiceSuccess(listener, ActionTypes.STATE_INFORMATION, null, stateMaps);
                } else {
                    handleServiceFailure(listener, ServiceListener.ActionTypes.STATE_INFORMATION, bean.msg, bean.code);
                }
            }
        });
    }

    /**
     * 更改密码
     *
     * @param oldpwd   旧密码 加密过的
     * @param newpwd   新密码 加密过的
     * @param listener
     */
    @Override
    public void changePassword(final String oldpwd, final String newpwd, final ServiceListener listener) {

        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "oldpwd", oldpwd);
        addOKHttpRequestBody(builder, "newpwd", newpwd);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_CHANGEPWD, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_CHANGEPWD, ActionTypes.CHANGE_PASSWORD) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

        });
    }

    /**
     * 发送建议
     *
     * @param content
     * @param listener
     */
    @Override
    public void sendFeedback(final String content, final ServiceListener listener) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "content", content);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_FEEDBACK, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_FEEDBACK, ActionTypes.FEEDBACK) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 获取最新版本的信息
     *
     * @param listener
     */
    @Override
    public void getVersionUpdateInformation(final ServiceListener listener) {
        Headers headers = addHeadersFromOKHttp(controller);
        Request rq = HttpUtil.getRequest(URL_CHECK_VERSION, headers, null);
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(rq).enqueue(new RequestCallBack(URL_CHECK_VERSION, ActionTypes.CHECK_VERSION) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    NewVersionInfors newVersionInfors = new Gson().fromJson(bean.data.toString(), NewVersionInfors.class);
                    handleServiceSuccess(listener, actionTypes, null, newVersionInfors);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 登出
     *
     * @param listener
     */
    @Override
    public void logout(final ServiceListener listener) {


        client.newCall(HttpUtil.getRequest(URL_LOGOUT, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_LOGOUT, ActionTypes.LOGOUT) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void getSupportReason(final ServiceListener listener) {
        client.newCall(HttpUtil.getRequest(URL_SUPPORT_REASON, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_SUPPORT_REASON, ActionTypes.SUPPORT_REASON) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    ContactUsTag contactUsTag = null;
                    try {
                        contactUsTag = gson.fromJson(bean.data.toString(), ContactUsTag.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    handleServiceSuccess(listener, actionTypes, null, contactUsTag);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void addSupportInfo(final String rid, final String reasontext, final String email, final ServiceListener listener) {

        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "rid", rid);
        addOKHttpRequestBody(builder, "reasontext", reasontext);
        addOKHttpRequestBody(builder, "email", email);
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_SUPPORT_ADD, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_SUPPORT_ADD, ActionTypes.ADD_SUPPORT_INFO) {


            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, bean.code);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void resetRongToken(final ServiceListener listener) {


        client.newCall(HttpUtil.getRequest(URL_RESETRONGTOKEN, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_RESETRONGTOKEN, ActionTypes.RESET_RONG_TOKEN) {
            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    String resetrongtoken = null;
                    try {
                        JSONObject jsonObject = (JSONObject) bean.data;
                        resetrongtoken = jsonObject.getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handleServiceSuccess(listener, actionTypes, null, resetrongtoken);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void myCoupon(final ServiceListener listener) {

        client.newCall(HttpUtil.getRequest(URL_COUPON, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_COUPON, ActionTypes.RESET_RONG_TOKEN) {

            @Override
            public void onResponse(CommonServiceBean bean, ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, null);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }
}
