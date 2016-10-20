package com.wf.irulu.common.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.logic.IruluController;

/**
 * @描述: HTTP网络层所有常量定义的工具类
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:HttpConstantUtils
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午5:38:11
 * 
 */

public class HttpConstantUtils {

	/** 操作类型*/
	public static final String X_APP_DEVICE = "X-App-Device"; // 1.Android 2.iPhone 3.iPad 8.商家后台  9.运营后台   10.微博、
	/** APP版本号*/
	public static final String X_APP_VERSION = "X-App-Version"; // 版本号 : 2.0.0
	/** API版本号*/
	public static final String X_API_VERSION = "X-Api-Version"; // API版本号(v1.0)
	/** 设备唯一号*/
	public static final String X_DID = "X-Did"; // 设备唯一号
	/** 渠道号*/
	public static final String X_UTM = "X-Utm"; // 渠道号
	/** 用户id*/
	public static final String X_UID = "X-Uid"; // 用户id
	/** 用户token*/
	public static final String X_TOKEN = "X-Token"; // 用户token
	/** 用户类型*/
	public static final String X_USER_TYPE = "X_User_Type"; // 用户类型  1：Email  2：Fackbook  3：Twitter
	/** UA信息*/
	public static final String USER_AGENT = "User-Agent";	//UA Rulu/版本号(设备类型;系统类型版本号) NetType/网络类型
	/** 系统*/
	public static final String SYSTEM = "Android";// 1:iOS 2:Android
	/** 设备类型*/
	public static final String DEVICE = "1";// 1.Android 2.iPhone 3.iPad 8.商家后台  9.运营后台   10.Web
	/** app版本号*/
	public static final String VERSION = IruluApplication.getInstance().getString(R.string.version_name);
	/** API版本值(：v4.0)*/
	public static final String API_VERSION = "v2.0";
	/** 设备唯一号*/
	public static final String DID = CommonUtil.getDeviceId();
	/**分页时每页数量*/
	public static final int PAGE_SIZE = 20;								// 分页时每页数量
	/**
	 * 获取AndroidManifest.xml里渠道名
	 * 
	 * @return channel
	 */
	public static String getMetaData() {
		String channel = "";
		try {
			// 获得包管理者
			PackageManager packageManager = IruluApplication.getInstance()
					.getPackageManager();
			// 根据包管理者获得应用信息
			ApplicationInfo appInfo = packageManager.getApplicationInfo(
					IruluApplication.getInstance().getPackageName(),
					PackageManager.GET_META_DATA);

			channel = appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return channel;
	}

	public static String getLocation(){
		String location = IruluController.getInstance().getConfigXml().read("X-LNG/LAT", "LNG/0 LAT/0");
		if (location.contains("LNG/0.0 LAT/0.0")){
			IruluApplication.getInstance().getAppInitLocation();
			location = IruluController.getInstance().getConfigXml().read("X-LNG/LAT", "LNG/0 LAT/0");
		}
		return location;
	}


	public static String getUserAgent() {
		StringBuffer buf = new StringBuffer();
		buf.append("iRulu/").append(IruluApplication.getInstance().getString(R.string.version_name));
		buf.append("(");
		buf.append(android.os.Build.MODEL + ";" + "Android " + android.os.Build.VERSION.RELEASE);
		buf.append(") ");
		buf.append(getLocation());
		buf.append(" NetType/");
		buf.append(ConstantsUtils.NETWORK_TYPE);
		return buf.toString();
	}
}
