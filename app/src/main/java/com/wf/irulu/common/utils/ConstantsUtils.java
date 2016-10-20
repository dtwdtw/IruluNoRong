package com.wf.irulu.common.utils;

import android.util.SparseArray;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.logic.manager.SettingManager;

import java.util.HashMap;

/**
 * @描述: 常量工具类
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:ConstantsUtils
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午4:09:49
 * 
 */

public class ConstantsUtils {

	/** 项目名 */
	public static final String APP_NAME = "irulu";
	/** 回车换行 */
	public static final String ENTER = "\r\n";
	/**push消息Notification的id*/
	public static int notificationId = 0;
	/** 网络类型，默认为WIFI*/
	public static String NETWORK_TYPE = "WIFI";
	/** * 屏幕的宽 */
	public static int DISPLAYW = IruluApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
	/** * 屏幕的高 */
	public static final int DISPLAYH = IruluApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
	/** * 屏幕的密度 */
	public static final float DENSITY = IruluApplication.getInstance().getResources().getDisplayMetrics().density;

	public static PayPalConfiguration config = new PayPalConfiguration().environment(ConstantsUtils.CONFIG_ENVIRONMENT).clientId(ConstantsUtils.CONFIG_CLIENT_ID).acceptCreditCards(false);
	// PayPal支付的环境
	public static final String CONFIG_CLIENT_ID = SettingManager.DEBUG ? "AVY7uKI_FT0R2CmNVvOBrlazis7qxVLDxRlmpkuceytKz7VpsYY6YEXl_JxVfCbqy_W-CCr4XOB1anSV" : "AVFF_xBmXr0MMIX0Q_ee3HrI6gO-ty20O20dsC0JCj2hk9Ef3MT1Put_x3bj";
	public static final String CONFIG_ENVIRONMENT = SettingManager.DEBUG ? PayPalConfiguration.ENVIRONMENT_SANDBOX : PayPalConfiguration.ENVIRONMENT_PRODUCTION;
	/** web与APP交互的协议*/
	public static String URL_PROTOCOL = "openapp.wfmobilemall";
	/** 支付请求码 */
	public static final int REQUEST_CODE_PAYMENT = 1;
	/** 添加地址请求码 */
	public static final int REQUEST_CODE_ADDRESS = 2;
	/** 默认地址结果码 */
	public static final int RESULT_CODE_ADDRESS = 3;
	/** 选择图片结果码 */
	public static final int RESULT_CODE_PHOTOS = 4;
	/**
	 * 选着国家后的结果码
	 */
	public static final int SELECT_COUNTRY_RESULT_CODE = 101;
	public static final int SELECR_STATE_RESULT_CODE=102;

	/** DIALOG id*/
	public static final int DIALOG_PROGRESS = 0x8;

	/** 分享到Facebook*/
	public static final int SHARE_FACEBOOK = 2015;
	/** 分享到Twitter*/
	public static final int SHARE_TWITTER = 2016;

	
	/** 订单状态标签 */
	public static final SparseArray<String> ORDER_STATUS = new SparseArray<String>() {
		{
			// 1 Unpaid 未付款
			put(1, "Unpaid");
			// 2 Pending 待审核
			put(2, "Pending");
			// 3 Paid 已付款
			put(3, "Unshipped");
			// 4 Shipped 已发货
			put(4, "Shipped");
			// 5 Frozen 交易冻结
			put(5, "Trading freezing");
			// 6 Closed 交易关闭
			put(6, "Trading closed");
			// 7 Completed 完成
			put(7, "Trading succeed");
		}
	};
	
//	/** 订单售后标签*/
//	public static final SparseArray<String> SERVER_STATUS = new SparseArray<String>(){
//		{
//			put(1, IruluApplication.getInstance().getString(R.string.refund_details));
//			put(2, IruluApplication.getInstance().getString(R.string.return_details));
//		}
//	};

	public static final HashMap<String,String> mVersionAnalystics = new HashMap<String,String>(){
		{
			put("version",HttpConstantUtils.VERSION);
		}
	};


}
