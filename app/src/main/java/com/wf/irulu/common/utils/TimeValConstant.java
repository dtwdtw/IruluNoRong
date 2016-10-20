package com.wf.irulu.common.utils;

/**
 * @描述: 时间有效性配置
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.utils
 * @类名:TimeValConstant
 * @作者: 左杰
 * @创建时间:2015/12/31 20:04
 */
public class TimeValConstant {

    /**
     * 上报LBS频率
     */
    public static final int TIME_SENDLBS = 2 * 60 * 1000;

    /**
     * 定位频率
     */
    public static final int TIME_LOCATION = 2 * 60 * 1000;

    /**
     * 下拉刷新频率
     */
    public static final int TIME_PULL_REFRESH = 60 * 1000;

    /**
     * 获取用户详情频率
     */
    public static final int TIME_USER_INFO = 60 * 1000;

    /**
     * 获取用户相册频率
     */
    public static final int TIME_USER_PHOTO = 60 * 1000;

    /**
     * 动态下拉刷新频率
     */
    public static final int TIME_DYNAMIC_REFRESH = 60 * 1000;
}
