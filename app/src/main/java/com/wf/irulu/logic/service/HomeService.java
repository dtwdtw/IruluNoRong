package com.wf.irulu.logic.service;

import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.SettingManager;

/**
 * @描述: APP首页API
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.service
 * @类名:HomeService
 * @作者: 左杰
 * @创建时间:2015/10/24 11:19
 */
public interface HomeService {

    //3.1.1	APP初始化
    public static final String URL_INIT = SettingManager.URL_API_SYSTEM + "/app/init";

    //3.1.1	启动广告
    public static final String URL_AD = SettingManager.URL_API_SYSTEM + "/app/ad";

    //3.1.2	产品首页
//    public static final String URL_HOME = SettingManager.URL_PRODUCT_SYSTEM + "/product/home";
    public static final String URL_HOME = SettingManager.URL_PRODUCT_SYSTEM + "/product/appInit";

    //3.1.3	产品首页分页
    public static final String URL_PRODUCT_MORE = SettingManager.URL_PRODUCT_SYSTEM + "/product/more";

    //3.8.2	Android检查更新
    public static final String URL_CHECK_UPDATE = SettingManager.URL_API_SYSTEM + "/check/update";

    //3.8.1	Feedback
    public static final String URL_FEEDBACK = SettingManager.URL_API_SYSTEM + "/feedback";

    //V1.1.1版本 APP 首页
    public static final String URL_OLD_HOME = SettingManager.URL_PRODUCT_SYSTEM + "/welcome/home";

    /**
     * 启动广告
     * @param listener
     */
    void startAdvertising(ServiceListener listener);
    /**
     * APP初始化
     * @param isFirst   是否第一次加载
     * @param listener
     */
    void appInit(boolean isFirst,ServiceListener listener);
    /**
     * 获取产品首页数据
     * @param listener
     */
    void getHomeData(ServiceListener listener);

    /**
     * 获取产品首页的分页类容
     * @param id 默认值为0
     * @param page 页码，默认值为1
     * @param num 每页的条目数，默认值为20
     * @param isPullDownToRefresh 是否是下拉刷新
     * @param listener
     */
    void getHomeDataMore(String id,String page,String num,boolean isPullDownToRefresh,ServiceListener listener);

}
