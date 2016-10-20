package com.wf.irulu.logic.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.bean.CategoryDataBean;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.CustomerReviews;
import com.wf.irulu.common.bean.DailyDealsInit;
import com.wf.irulu.common.bean.HomeProduct;
import com.wf.irulu.common.bean.ProductCategoryBean;
import com.wf.irulu.common.bean.ProductDetail;
import com.wf.irulu.common.bean.SearchResult;
import com.wf.irulu.common.bean.WishInfo;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.HttpConstantUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.ConfigXML;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.parser.SearchParser;
import com.wf.irulu.logic.parser.WishParser;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.logic.service.ProductService;
import com.wf.irulu.module.category.activity.CategoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by XImoon on 15/10/19.
 */
public class ProductServiceImpl extends BaseService implements ProductService {

    private static final String TAG = "ProductServiceImpl";

    private IruluController controller;
    private ServiceManager serviceManager;

    //    private SharedPreferences sharedPreferences;
    private static boolean isFirst = true;

    public ProductServiceImpl(IruluController controller, ServiceManager serviceManager) {
        this.controller = controller;
        this.serviceManager = serviceManager;
    }

    @Override
    public void getProductDetail(final String id, final ServiceListener listener) {
        // 初始化并设置get请求的参数信息
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_PRODUCT_DETAIL, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_PRODUCT_DETAIL, ServiceListener.ActionTypes.PRO_DETA) {

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    ProductDetail productDetail = new Gson().fromJson(bean.data.toString(), ProductDetail.class);
                    handleServiceSuccess(listener, actionTypes, null, productDetail);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
                ILog.e("hellolove", bean.data.toString());
            }
        });
    }

    @Override
    public void getCommentDetail(final String id, final int p, final int num, final ServiceListener listener) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("p", String.valueOf(p));
        params.put("num", String.valueOf(num));
        client.newCall(HttpUtil.getRequest(URL_PRODUCT_COMMENTS, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_PRODUCT_COMMENTS, ServiceListener.ActionTypes.COMMENT_LIST) {

            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    CustomerReviews customerReviews = new Gson().fromJson(bean.data.toString(), CustomerReviews.class);
                    handleServiceSuccess(listener, actionTypes, null, customerReviews);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void getWishList(final ServiceListener listener) {
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_WISH_LIST, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_WISH_LIST, ServiceListener.ActionTypes.WISH_LIST) {
            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    ArrayList<WishInfo> wishList = WishParser.parserWishList(bean.data);
                    handleServiceSuccess(listener, actionTypes, null, wishList);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void getHotSearch(final ServiceListener listener) {
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        SharedPreferences sharedPreferences = IruluApplication.getInstance().getSharedPreferences("SavedHotInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirst || sharedPreferences.getString("Info", "") == "") {
            isFirst = false;
            client.newCall(HttpUtil.getRequest(URL_SEARCH_HOT, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_SEARCH_HOT, ServiceListener.ActionTypes.HOT_SEARCH) {

                @Override
                public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                    if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                        ArrayList<String> hotWord = SearchParser.parserHotSearch(bean.data);
                        handleServiceSuccess(listener, actionTypes, null, hotWord);
                        // TODO
                        editor.putString("Info", bean.data.toString());
                        editor.commit();
                    } else {
                        handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                    }
                }

                @Override
                public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                    handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
                }
            });
        } else {
            ArrayList<String> hotWord = SearchParser.parserHotSearch(sharedPreferences.getString("Info", ""));
            ILog.e("lovehello", sharedPreferences.getString("Info", ""));
            handleServiceSuccess(listener, ServiceListener.ActionTypes.HOT_SEARCH, null, hotWord);
        }

    }


    /**
     * 产品分类的首页
     *
     * @param listener
     */
    @Override
    public void getCategoryHome(final ServiceListener listener) {
        String categoryHomeCache = CacheUtils.getString((CategoryActivity) listener, "CategoryHomeCache");
        if (!TextUtils.isEmpty(categoryHomeCache)) {
            Gson gson = new Gson();
            ProductCategoryBean pcb = gson.fromJson(categoryHomeCache, ProductCategoryBean.class);
            handleServiceCallback(listener, ServiceListener.ActionTypes.PRODUCT_CATEGORY, 0, pcb);
            return;
        }
        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_PRODUCT_CATEGORY_HOME, addHeadersFromOKHttp(controller), null)).enqueue(new RequestCallBack(URL_PRODUCT_CATEGORY_HOME, ServiceListener.ActionTypes.PRODUCT_CATEGORY) {

            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    ProductCategoryBean pcb = gson.fromJson(bean.data.toString(), ProductCategoryBean.class);
                    CacheUtils.setString((CategoryActivity) listener, "CategoryHomeCache", bean.data.toString());
                    handleServiceSuccess(listener, actionTypes, null, pcb);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    /**
     * 获取产品分类的列表
     *
     * @param cate
     * @param p
     * @param num
     * @param sort
     * @param sort_type
     * @param listener
     */
    @Override
    public void getCategoryList(final int cate, final int p, final int num, final String sort, final int sort_type, final int flags, final ServiceListener listener) {
        HashMap<String, String> params = new HashMap<String, String>() {
            {
                put("cate", String.valueOf(cate));
                put("p", String.valueOf(p));
                put("num", String.valueOf(num));
                if (flags != 4) {
                    put("sort", sort);
                    put("sort_type", String.valueOf(sort_type));
                }
            }
        };

        // 获取get请求方式下的请求对象并设置到client的请求队列中执行
        client.newCall(HttpUtil.getRequest(URL_PRODUCT_CATEGORY_LIST, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_PRODUCT_CATEGORY_LIST, ServiceListener.ActionTypes.PRODUCT_CATEGORY_LIST) {
            @Override
            public void onFailure(Request request, IOException e) {
                // 联网失败，断网，超时等获取的信息
                ArrayList<String> msgs = new ArrayList<String>();
                msgs.add("");
                msgs.add(String.valueOf(flags));
                handleServiceFailure(listener, ServiceListener.ActionTypes.PRODUCT_CATEGORY_LIST, msgs, ErrorCodeUtils.NO_NET_RESPONSE);
            }


            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    Gson gson = new Gson();
                    CategoryDataBean cdb = gson.fromJson(bean.data.toString(), CategoryDataBean.class);
                    ArrayList<Integer> ali = new ArrayList<Integer>();
                    ali.add(flags);
                    ali.add(p);
                    handleServiceSuccess(listener, actionTypes, ali, cdb);
                } else {
                    ArrayList<String> msgs = new ArrayList<String>();
                    msgs.add(String.valueOf(p));
                    msgs.add(String.valueOf(flags));
                    msgs.add(bean.msg + "");
                    handleServiceFailure(listener, actionTypes, msgs, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                ArrayList<String> msgs = new ArrayList<String>();
                msgs.add("");
                msgs.add(String.valueOf(flags));
                handleServiceFailure(listener, actionTypes, msgs, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void getSuggestSearch(final String keyword, final ServiceListener listener) {
        Map<String, String> params = new HashMap<String, String>() {
            {
                put("keyword", String.valueOf(keyword));
            }
        };
        client.newCall(HttpUtil.getRequest(URL_SEARCH_SUGGEST, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_SEARCH_SUGGEST, ServiceListener.ActionTypes.SUGGEST_SEARCH) {

            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    ArrayList<String> suggestWords = SearchParser.parserHotSearch(bean.data);
                    handleServiceSuccess(listener, actionTypes, null, suggestWords);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }

    @Override
    public void getSearchResult(final String keyword, final String word, final int sort, final int p, final int num, final ServiceListener listener, final boolean isPullDownToRefresh) {
        final ConfigXML config = new ConfigXML(controller);
//        if(isPullDownToRefresh){//是下拉刷新
//            // 时间限制
//            long time = System.currentTimeMillis();
//            long homeDataMoreTime = config.read("getSearchresult",0L);
//            if(Math.abs(time - homeDataMoreTime) < TimeValConstant.TIME_PULL_REFRESH&& NetworkUtils.getInstance().isNetworkAvailable()){
//                handleServiceCallback(listener, ServiceListener.ActionTypes.PRODUCT_SEARCH, ServiceListener.TYPE_REFRESH_TIME_VAL, false);
//                return;
//            }
//        }
        handleServiceCallback(listener, ServiceListener.ActionTypes.PRODUCT_SEARCH, ServiceListener.TYPE_LOADINGING, true);
        final Map<String, String> params = new HashMap<String, String>() {
            {
                put("keyword", keyword);
                put("word", word);
                put("sort", String.valueOf(sort));
                put("p", String.valueOf(p));
                put("num", String.valueOf(num));
            }
        };
        client.newCall(HttpUtil.getRequest(URL_SEARCH_RESULT, addHeadersFromOKHttp(controller), params)).enqueue(new RequestCallBack(URL_SEARCH_RESULT, ServiceListener.ActionTypes.PRODUCT_SEARCH) {

            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    SearchResult result = null;
                    try {
                        result = new Gson().fromJson(bean.data.toString(), SearchResult.class);
                        handleServiceSuccess(listener, actionTypes, params, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
                handleServiceCallback(listener, actionTypes, ServiceListener.TYPE_LOADINGING, false);

                if (isPullDownToRefresh) {
                    config.save("getSearchresult", System.currentTimeMillis());
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
                handleServiceCallback(listener, actionTypes, ServiceListener.TYPE_LOADINGING, false);
            }
        });
    }

    /**
     * 提交产品评论
     *
     * @param id
     * @param content
     * @param image
     * @param star
     * @param listener
     */
    @Override
    public void publishProductComment(final String id, final String content, final String image, final int star, final ServiceListener listener) {
        // 初始化post的表单数据对象
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 添加post表单的数据
        addOKHttpRequestBody(builder, "id", id);
        addOKHttpRequestBody(builder, "content", content);
        addOKHttpRequestBody(builder, "image", image);
        addOKHttpRequestBody(builder, "star", String.valueOf(star));
        // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
        client.newCall(HttpUtil.postRequest(URL_PRODUCT_PUBLISH, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_PRODUCT_PUBLISH, ServiceListener.ActionTypes.PUBLISH_COMMENT) {
            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    handleServiceSuccess(listener, actionTypes, null, 0);
                } else {
                    handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                }
            }

            @Override
            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }
        });
    }


    @Override
    public void getNewArrivals(String page, String id, final ServiceListener serviceListener) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("page", page);
        data.put("num", HttpConstantUtils.PAGE_SIZE + "");
        data.put("id", id);
        client.newCall(HttpUtil.getRequest(URL_PRODUCT_NEW_ARRIVALS, addHeadersFromOKHttp(controller), data)).enqueue(new RequestCallBack(URL_PRODUCT_NEW_ARRIVALS, ServiceListener.ActionTypes.NEW_ARRICALS) {
            @Override
            public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {


                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(bean.data.toString());
                        if (jsonObj.has("list")) {
                            JSONObject list = jsonObj.getJSONObject("list");

                            HomeProduct homeProduct = null;
                            try {
                                Gson gson = new Gson();
                                String s = list.toString();
                                homeProduct = gson.fromJson(s, HomeProduct.class);
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }

                            handleServiceSuccess(serviceListener, actionTypes, null, homeProduct);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    handleServiceFailure(serviceListener, actionTypes, bean.msg, bean.code);
                }

            }

            public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {

                handleServiceFailure(serviceListener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
            }

        });
    }


        /**
         * 获取日狗信息
         * @param page
         * @param num
         * @param type
         * @param listener
         * TODO:获取每日特价信息--后续完成
         */
        @Override
        public void getDailDeals ( int page, int num, int type,
        final ServiceListener listener){
            // 初始化post的表单数据对象
            FormEncodingBuilder builder = new FormEncodingBuilder();
            // 添加post表单的数据
            addOKHttpRequestBody(builder, "page", String.valueOf(page));
            addOKHttpRequestBody(builder, "num", String.valueOf(num));
            addOKHttpRequestBody(builder, "type", String.valueOf(type));
            // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
            client.newCall(HttpUtil.postRequest(URL_PRODUCT_DAILY_DEALS, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_PRODUCT_DAILY_DEALS, ServiceListener.ActionTypes.DAILY_DEALS) {
                @Override
                public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                    if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {

                    }

                }

                @Override
                public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {
                    handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
                }
            });

        }

        /**
         * 初始化日购信息
         * @param page
         * @param num
         * @param listener
         */
        @Override
        public void getDailDealsInit ( int page, int num,
        final ServiceListener listener){
            // 初始化post的表单数据对象
            FormEncodingBuilder builder = new FormEncodingBuilder();
            // 添加post表单的数据
            addOKHttpRequestBody(builder, "page", String.valueOf(page));
            addOKHttpRequestBody(builder, "num", String.valueOf(num));

            // 获取post方式下的request对象，并设置到client对象的执行队列并执行请求
            client.newCall(HttpUtil.postRequest(URL_PRODUCT_DAILY_DEALS_INIT, addHeadersFromOKHttp(controller), builder.build())).enqueue(new RequestCallBack(URL_PRODUCT_DAILY_DEALS_INIT, ServiceListener.ActionTypes.DAILY_DEALS_INIT) {

                private String json;

                @Override
                public void onResponse(CommonServiceBean bean, ServiceListener.ActionTypes actionTypes) {
                    if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                        DailyDealsInit dailyDealsInit = null;
                        json = bean.data.toString();
                        try {

                            dailyDealsInit = new Gson().fromJson(json, DailyDealsInit.class);

                            handleServiceSuccess(listener, actionTypes, null, dailyDealsInit);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (dailyDealsInit == null) {
                            handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.ERROR_PARSER);
                        }
                    } else {
                        handleServiceFailure(listener, actionTypes, bean.msg, bean.code);
                    }
                }

                @Override
                public void onFailure(Request request, ServiceListener.ActionTypes actionTypes) {

                    handleServiceFailure(listener, actionTypes, null, ErrorCodeUtils.NO_NET_RESPONSE);
                }
            });
        }

    }
