package com.wf.irulu.logic.service;

import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.SettingManager;

/**
 * @描述: 产品API
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:ProductService
 * @作者: Yuki
 * @创建时间:2015-7-15 上午10:31:21
 */
public interface ProductService {

    //  产品列表
    public static final String URL_PRODUCT_LISTS = SettingManager.URL_PRODUCT_SYSTEM + "/product/lists";

    // 产品详情
    public static final String URL_PRODUCT_DETAIL = SettingManager.URL_PRODUCT_SYSTEM + "/product/detail";

    // 搜索关键字猜想
    public static final String URL_PRODUCT_SEARCH_SUGGEST = SettingManager.URL_PRODUCT_SYSTEM + "/search/suggest";

    // 热门关键字
    public static final String URL_PRODUCT_HOT = SettingManager.URL_PRODUCT_SYSTEM + "/search/hot";

    // 	产品搜索
    public static final String URL_PRODUCT_SEARCH = SettingManager.URL_PRODUCT_SYSTEM + "/search";

    // 	产品评论列表
    public static final String URL_PRODUCT_COMMENTS = SettingManager.URL_PRODUCT_SYSTEM + "/comment/lists";

    // 	产品评论标签
    public static final String URL_PRODUCT_COMMENT_TAG = SettingManager.URL_PRODUCT_SYSTEM + "/comment/tag";

    // 	产品评论
    public static final String URL_PRODUCT_PUBLISH = SettingManager.URL_PRODUCT_SYSTEM + "/comment/publish";

    // 首页推荐更多
    public static final String URL_PRODUCT_RECOMMENDATION = SettingManager.URL_PRODUCT_SYSTEM + "/welcome/rec";

    // 获取wish心愿单
    public static final String URL_WISH_LIST = SettingManager.URL_CART_SYSTEM + "/likes//getList";

    // 获取热门标签
    public static final String URL_SEARCH_HOT = SettingManager.URL_PRODUCT_SYSTEM + "/search/hot";

    // 获取搜索条目
    public static final String URL_SEARCH_SUGGEST = SettingManager.URL_PRODUCT_SYSTEM + "/search/suggest";

    // 获取搜索条目
    public static final String URL_SEARCH_RESULT = SettingManager.URL_PRODUCT_SYSTEM + "/search";

    //产品分页首页
    public static final String URL_PRODUCT_CATEGORY_HOME = SettingManager.URL_PRODUCT_SYSTEM + "/product/cateIndex";

    //获取产品分类的列表
    public static final String URL_PRODUCT_CATEGORY_LIST = SettingManager.URL_PRODUCT_SYSTEM + "/product/lists";
    //获取新品
    public static final String URL_PRODUCT_NEW_ARRIVALS = SettingManager.URL_PRODUCT_SYSTEM + "/product/more";

    //获取dail deals
    public static final String URL_PRODUCT_DAILY_DEALS = SettingManager.URL_PRODUCT_SYSTEM + "/product/daily";

    //获取dail deals初始化
    public static final String URL_PRODUCT_DAILY_DEALS_INIT = SettingManager.URL_PRODUCT_SYSTEM + "/product/dailyinit";


    /**
     * 获取商品详情
     *
     * @param id       产品id
     * @param listener
     */
    void getProductDetail(String id, ServiceListener listener);

    /**
     * 获取评论详情
     *
     * @param id       商品id
     * @param listener
     */
    void getCommentDetail(String id, int p, int num, ServiceListener listener);

    /**
     * 获取wish列表
     *
     * @param listener
     */
    void getWishList(ServiceListener listener);

    /**
     * 获取热门搜索标签
     *
     * @param listener
     */
    void getHotSearch(ServiceListener listener);

    /**
     * 获取产品分类首页
     *
     * @param listener
     */
    void getCategoryHome(ServiceListener listener);

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
    void getCategoryList(int cate, int p, int num, String sort, int sort_type, int flags, ServiceListener listener);

    /**
     * 获取猜想词列表
     *
     * @param keyword  用户输入关键词
     * @param listener
     */
    void getSuggestSearch(String keyword, ServiceListener listener);


    /**
     * 获取搜索结果列表
     *
     * @param keyword  搜索关键字
     * @param word     排序方式指示器
     * @param sort     排序
     * @param p        当前页
     * @param num      煤页条目数
     * @param listener
     */
    void getSearchResult(String keyword, String word, int sort, int p, int num, ServiceListener listener, boolean isPullDownToRefresh);

    /**
     * 发布产品评论
     *
     * @param id
     * @param content
     * @param image
     * @param star
     * @param listener
     */
    void publishProductComment(String id, String content, String image, int star, ServiceListener listener);


    void getNewArrivals(String page, String id, ServiceListener serviceListener);

    /**
     * 获取
     *
     * @param page
     * @param num
     * @param type
     * @param listener
     */
    void getDailDeals(int page, int num, int type, ServiceListener listener);

    public void getDailDealsInit(int page, int num,
                                 final ServiceListener listener);
}
