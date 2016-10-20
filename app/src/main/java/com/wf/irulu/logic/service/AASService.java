package com.wf.irulu.logic.service;

import android.content.Context;

import com.facebook.Profile;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.SettingManager;

/**
 * @描述: AAS相关接口服务定义
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:AASService
 * @作者: 左西杰
 * @创建时间:2015-5-29 下午2:37:42
 */

public interface AASService {

    // 登录URL
    public static final String URL_LOGIN = SettingManager.URL_USER_SYSTEM + "/login";

    // 用户注册URL
    public static final String URL_REGISTER = SettingManager.URL_USER_SYSTEM + "/register";

    //忘记密码
    public static final String URL_FORGOT_PASSWORD = SettingManager.URL_USER_SYSTEM + "/user/requestgetpwd";

    // 修改用户信息 (昵称 生日)URL
    public static final String URL_UPDATEUSERINFO = SettingManager.URL_USER_SYSTEM + "/user/updateuserinfo";

    //  获取发货地址
    public static final String URL_GETADDRESS = SettingManager.URL_USER_SYSTEM + "/address/getaddress";

    //  设置默认发货地址
    public static final String URL_SET_DEFAULT_ADDRESS = SettingManager.URL_USER_SYSTEM + "/address/setdefault";

    //  删除发货地址
    public static final String URL_DELETE_ADDRESS = SettingManager.URL_USER_SYSTEM + "/address/delete";

    // 添加发货地址URL
    public static final String URL_ADD_ADDRESS = SettingManager.URL_USER_SYSTEM + "/address/add";

    // 编辑发货地址URL
    public static final String URL_UPDATE_ADDRESS = SettingManager.URL_USER_SYSTEM + "/address/update";

    //	获取发货国家信息
    public static final String URL_GET_ALL_COUNTRY = SettingManager.URL_USER_SYSTEM + "/country/getall";

    public static final String URL_GET_ALL_STATE=SettingManager.URL_USER_SYSTEM+"/state/getall";

    // 修改用户登录密码URL
    public static final String URL_CHANGEPWD = SettingManager.URL_USER_SYSTEM + "/user/changepwd";


    // feedback 你的建议 的URL
    public static final String URL_FEEDBACK = SettingManager.URL_API_SYSTEM + "/feedback";

    // 获取个人资料URL
    public static final String URL_CHECK_VERSION = SettingManager.URL_API_SYSTEM + "/check/update";


    // 用户登出URL
    public static final String URL_LOGOUT = SettingManager.URL_USER_SYSTEM + "/logout";

    //  获取上传图片的token

    public static final String URL_GET_PICTURE_TOKEN = SettingManager.URL_UPLOAD_SYSTEM + "/upload/token";


    // 获取个人资料URL
    public static final String URL_GETUSERINFO = SettingManager.URL_USER_SYSTEM + "/user/getuserinfo";

    // 删除个人资料URL
    public static final String URL_DELUSERINFO = SettingManager.URL_USER_SYSTEM + "/user/deluserinfo";

    // 批量查询用户信息URL
    public static final String URL_GETUSERLIST = SettingManager.URL_USER_SYSTEM + "/user/getuserlist";


    // 找回密码URL
    public static final String URL_RESETPWD = SettingManager.URL_USER_SYSTEM + "/user/resetpwd";

    //  获取默认发货地址信息
    public static final String URL_GETDEFAULT = SettingManager.URL_USER_SYSTEM + "/address/getdefault";

    //  根据地址ID获取发货地址信息
    public static final String URL_GETONEADDRESS = SettingManager.URL_USER_SYSTEM + "/address/getoneaddress";

    //  根据用户ID或设备ID获取融云token
    public static final String URL_GETRONGTOKEN = SettingManager.URL_USER_SYSTEM + "/user/getrongtoken";

    //  判断用户是否注册
    public static final String URL_ISREGISTER = SettingManager.URL_USER_SYSTEM + "/user/isregister";

    //  重新获取rongcloudtoken
    public static final String URL_RESETRONGTOKEN = SettingManager.URL_USER_SYSTEM + "/user/resetrongtoken";


    //3.8.3	获得客户支持原因列表
    public static final String URL_SUPPORT_REASON = SettingManager.URL_USER_SYSTEM + "/support/reason";

    //3.8.4	添加客户支持信息
    public static final String URL_SUPPORT_ADD = SettingManager.URL_USER_SYSTEM + "/support/add";

    //我的优惠券
    public static final String URL_COUPON = SettingManager.URL_USER_SYSTEM + "/coupon";

    /**
     * 获取上传图片的token
     *
     * @param type
     * @param key
     * @param listener
     */
    void getUpLoadImageToken(String uid, String url, String type, String key, ServiceListener listener);

    /**
     * 七牛上传图片
     * @param lisener
     * @param key
     * @param token
     * @param pic
     * @param context
     */
    void qiNiuUpload(ServiceListener lisener, String key, String token,String pic,Context context);
    /**
     * 登录
     */
    void login(String email, String password, String openid, int type, ServiceListener listener);
    void login(String email,String gender,String link, String locale,String name,String weblink, String openid, int type, ServiceListener listener);
    void login(String email,String link, String locale,String name, String screenname,String description,String website,String openid, int type, ServiceListener listener);

    /**
     * 注册
     *
     * @param email
     * @param password
     * @param froms
     * @param listener
     */
    void register(String firstname,String lastname,String email, String password, String froms, ServiceListener listener);

    /**
     * 忘记密码
     *
     * @param email
     * @param listener
     */
    void forgetPassword(String email, ServiceListener listener);

    /**
     * 更改昵称
     *
     * @param nickname
     * @param listener
     */
    void updateNickName(String nickname, ServiceListener listener);

    /**
     * 更改生日
     *
     * @param birthday
     * @param listener
     */
    void updateBirthday(String birthday, ServiceListener listener);

    /**
     * 获取所有的发货地址
     *
     * @param listener
     */
    void getAllAddress(ServiceListener listener);

    /**
     * 设置默认的发货地址
     *
     * @param id       地址的唯一标示
     * @param listener
     */
    void setDefaultAddress(String id,int position, ServiceListener listener);

    /**
     * 删掉发货地址
     *
     * @param id
     * @param listener
     */
    void deleteAddress(String id,int position, ServiceListener listener);

    /**
     * 添加发货地址
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
    void addAddress(String fristname, String lastname, String country, String state, String city, String postcode, String phonenumber, String email, String streetaddress1, String streetaddress2, String isdefault, ServiceListener listener);

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
    void editAddress(String id, String fristname, String lastname, String country, String state, String city, String postcode, String phonenumber, String email, String streetaddress1, String streetaddress2, String isdefault,ShippingAddrBean sb, ServiceListener listener);

    /**
     * 获取所有国家的信息
     *
     * @param listener
     */
    void getCountryInformation(ServiceListener listener);

    /**
     * 获取所有国家地区的信息
     *
     * @param listener
     */
    void getStateInformation(ServiceListener listener);

    /**
     * 更改用户密码
     *
     * @param oldpwd   旧密码 加密过的
     * @param newpwd   新密码 加密过的
     * @param listener
     */
    void changePassword(String oldpwd, String newpwd, ServiceListener listener);

    /**
     * @param content
     * @param listener
     */
    void sendFeedback(String content, ServiceListener listener);

    /**
     * 获取版本更新的信息
     *
     * @param listener
     */
    void getVersionUpdateInformation(ServiceListener listener);

    /**
     * 登出
     * @param listener
     */
    void logout(ServiceListener listener);

    /**
     * 获得客户支持原因列表
     * @param listener
     */
    void getSupportReason(ServiceListener listener);

    /**
     * 添加客户支持信息
     * @param rid 原因id
     * @param reasontext 反馈内容
     * @param email 邮箱
     * @param listener
     */
    void addSupportInfo(String rid,String reasontext,String email,ServiceListener listener);

    /**
     * 3重新获取rongcloudtoken
     * @param listener
     */
    void resetRongToken(ServiceListener listener);

    /**
     * 我的优惠券
     * @param listener
     */
    void myCoupon(ServiceListener listener);
}
