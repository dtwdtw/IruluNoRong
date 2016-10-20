
package com.wf.irulu.logic.manager;

/**
 * @描述: 客户端配置
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.service
 * @类名:SettingManager
 * @作者: 左西杰
 * @创建时间:2015-5-29 下午2:38:47
 * 
 */

public class SettingManager {

	// true 内网 false 外网
	public static final boolean DEBUG = false;

	// Terms of service
	public static final String URL_TERMS_OF_SERVICE = "https://document.irulu.com/agreement/teamsservice.html";
	// privacy policy
	public static final String URL_PRIVACY_POLICY = "https://document.irulu.com/agreement/privacypolicy.html";

	// API
	public static final String URL_API_SYSTEM = DEBUG ? "http://api.irulusales.top" : "https://api.irulu.com";
	// 用户系统 [user]
	public static final String URL_USER_SYSTEM =  DEBUG ? "http://account.irulusales.top" : "https://api-account.irulu.com";
	// 商家系统 [merchant]
	public static final String URL_SELLER_SYSTEM =  DEBUG ? "http://merchant.irulusales.top" : "https://api-merchant.irulu.com";
	// 商品系统 [product]
	public static final String URL_PRODUCT_SYSTEM =  DEBUG ? "http://item.irulusales.top" : "https://api-item.irulu.com";
	// 上传系统 [upload]
	public static final String URL_UPLOAD_SYSTEM =  DEBUG ? "http://upload.irulusales.top" : "https://api-upload.irulu.com";
	// 支付系统 [payment]
	public static final String URL_PAY_SYSTEM = DEBUG ? "http://payment.irulusales.top" : "https://api-payment.irulu.com";
	// 购物车系统 [cart]
	public static final String URL_CART_SYSTEM = DEBUG ? "http://cart.irulusales.top" : "https://api-cart.irulu.com";
	// 订单系统 [order]
	public static final String URL_ORDER_SYSTEM = DEBUG ? "http://order.irulusales.top" : "https://api-order.irulu.com";
	//网页支付［payment］
	public static final String URL_WEBPAY_SYSTEM=DEBUG?"http://m.irulusales.top/pay/payment/":"https://m.irulu.com/pay/payment/";
}
