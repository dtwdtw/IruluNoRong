package com.wf.irulu.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wf.irulu.IruluApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @描述: 公共帮助类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:CommonUtil
 * @作者: 左西杰
 * @创建时间:2015-5-28 上午10:19:38
 * 
 */

public class CommonUtil {

	private static SharedPreferences exition = IruluApplication.getInstance().getSharedPreferences("Exittion", 0);

	public static void closeKeyBoard(EditText editText) {
		InputMethodManager imm = (InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	public static void hideSoftInput(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); 					//强制隐藏键盘
	}
	
	public static void showSoftInput(View view){
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 判断网络状态
	 */
	public static boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) IruluApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return false;
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}

		if (networkInfo.isConnected()) {
			return true;
		}

		return false;
	}
	
	public static String getDateFormat(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time));
	}
	
	/**
	 * 获取设备唯一号
	 * 
	 * @return 设备唯一号
	 */
	public static String getDeviceId() {
		TelephonyManager manager = (TelephonyManager) IruluApplication.getInstance().getSystemService(Activity.TELEPHONY_SERVICE);
		String did = manager.getDeviceId();//我的小米手机使用TelephonyManager获取设备唯一号为null
		if(did == null){//所以只能获取手机的物理地址作为设备唯一号
			did = getLocalMacAddress();
		}
		return did;
	}
	
	/**
	 * 获取手机的物理地址
	 * @return
	 */
	public static String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) IruluApplication.getInstance().getSystemService(Activity.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress(); 
    }
	/**
	 * @return 获得当前版本的名字 表示 没有找到包 的异常
	 */
	public static String getVersionName() {
		// 包管理器的信息PackageManager 管理android系统安装所有apk信息
		PackageManager pmm =IruluApplication.getInstance().getPackageManager();
		try {
			PackageInfo packageInfo = pmm.getPackageInfo(IruluApplication.getInstance().getPackageName(), 0);
			String VersionName = packageInfo.versionName;
			return VersionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获取Android系统版本号
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion = 0;
		try {
//			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);//过时了
			sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
			sdkVersion = 0;

		}

		return sdkVersion;
	}
}
