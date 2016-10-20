package com.wf.irulu.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wf.irulu.IruluApplication;

/**
 * 
 * @描述: 网络检测
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:NetworkUtils
 * @作者: 左西杰
 * @创建时间:2015-8-31 下午12:42:30
 *
 */
public class NetworkUtils {

	public static final int NETWORK_TYPE_UNKNOWN = -1;

	public static final int NETWORK_TYPE_WIFI = 0;

	public static final int NETWORK_TYPE_3G = 1;

	public static final int NETWORK_TYPE_2G = 2;

	public static final int NETWORK_TYPE_4G = 3;
	
	/**中国移动**/
	public static final int CHINA_MOBILE_CMCC =0;
	/**中国联通***/
	public static final int CHINA_UNICOM =1;
	/**中国电信**/
	public static final int CHINA_TELECOM =2;
	/**运营商未知*/
	public static final int UNKONW = 3;
	private ConnectivityManager mConnManager;

	private TelephonyManager mPhonyManager;

	private static NetworkUtils mInstance;
	private Context mContext;

	private NetworkUtils() {
		mContext = IruluApplication.getInstance();
		mConnManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mPhonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static NetworkUtils getInstance() {
		if (mInstance == null) {
			synchronized (NetworkUtils.class) {
				mInstance = new NetworkUtils();
			}
		}
		return mInstance;
	}

	/**
	 * @return
	 */
	public int getNetworkType() {
		if (isWifiAvailable()) {
			return NETWORK_TYPE_WIFI;
		}
		if (isMobileNetAvailable()) {
			if (isConnection4G()) {
				return NETWORK_TYPE_4G;
			}

			if (isConnection3G()) {
				return NETWORK_TYPE_3G;
			}

			if (isConnection2G()) {
				return NETWORK_TYPE_2G;
			}
		}
		return NETWORK_TYPE_UNKNOWN;
	}

	/***
	 * @return
	 */
	public boolean isConnection4G() {
		boolean result = false;
		switch (mPhonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_LTE: // 4G
			result = true;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * @return
	 */
	public boolean isConnection3G() {
		boolean result = false;
		switch (mPhonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
		case TelephonyManager.NETWORK_TYPE_EVDO_0: // 3G
		case TelephonyManager.NETWORK_TYPE_EVDO_A: // 3G
		case TelephonyManager.NETWORK_TYPE_HSDPA: // 3G
		case TelephonyManager.NETWORK_TYPE_HSUPA: // 3G
		case TelephonyManager.NETWORK_TYPE_HSPA: // 3G
			// case TelephonyManager.NETWORK_TYPE_EVDO_B: // 3G
			result = true;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConnection2G() {
		boolean result = false;
		switch (mPhonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
		case TelephonyManager.NETWORK_TYPE_GPRS: // 2.5G
		case TelephonyManager.NETWORK_TYPE_EDGE: // 2.75G
		case TelephonyManager.NETWORK_TYPE_CDMA: // 2G
		case TelephonyManager.NETWORK_TYPE_1xRTT: // 2G
		case TelephonyManager.NETWORK_TYPE_IDEN: // 2G
			result = true;
			break;
		default:
			break;
		}
		return result;
	}

	/***
	 * 根据IMSI
	 */
	public int getTelephoneDetailByIMSI() {
		/**
		 * 获取SIM卡的IMSI码 SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile
		 * Subscriber Identification Number）是区别移动用户的标志，
		 * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
		 * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
		 * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
		 * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
		 */
		String imsi = mPhonyManager.getSubscriberId();
		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
				// 中国移动
				return CHINA_MOBILE_CMCC;
			} else if (imsi.startsWith("46001")) {
				// 中国联通
				return CHINA_UNICOM;
			} else if (imsi.startsWith("46003")) {
				// 中国电信
				return CHINA_TELECOM;
			}
		}
		return UNKONW;
	}
	
	/***
	 * 根据运营商
	 */
	public int getTelephoneDetailByOperator(){
		String operator = mPhonyManager.getSimOperator();
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46002")) {
				// 中国移动
				return CHINA_MOBILE_CMCC;
			} else if (operator.equals("46001")) {
				// 中国联通
				return CHINA_UNICOM;
			} else if (operator.equals("46003")) {
				// 中国电信
				return CHINA_TELECOM;
			}
		}
		return UNKONW;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNetworkAvailable() {
		NetworkInfo info = mConnManager.getActiveNetworkInfo();
		return isLinkable(info);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMobileNetAvailable() {
		NetworkInfo info = mConnManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return isLinkable(info);
	}

	/**
	 * <br>
	 * 根据Wifi信息获取本地Mac</br>
	 * 
	 * @return
	 */
	public String getLocalMacAddressFromWifiInfo() {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String wifiMac = info.getMacAddress();
		if (TextUtils.isEmpty(wifiMac)) {
			return "00:00:00:00:00:00";
		} else {
			return wifiMac;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isWifiAvailable() {
		NetworkInfo info = mConnManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return isLinkable(info);
	}

	/**
	 * @param info
	 * @return
	 */
	private boolean isLinkable(NetworkInfo info) {
		if ((info != null) && info.isConnected() && info.isAvailable()) {
			return true;
		}
		return false;
	}

//	/**
//	 * @param httpParams
//	 */
//	public void fillProxy(final HttpParams httpParams) {
//		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		final NetworkInfo networkInfo = connectivityManager
//				.getActiveNetworkInfo();
//		if (networkInfo == null || networkInfo.getExtraInfo() == null) {
//			return;
//		}
//
//		String info = networkInfo.getExtraInfo().toLowerCase();
//		if (info != null) {
//			if (info.startsWith("cmwap") || info.startsWith("uniwap")
//					|| info.startsWith("3gwap")) {
//				HttpHost proxy = new HttpHost("10.0.0.172", 80);
//				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
//				return;
//			} else if (info.startsWith("ctwap")) {
//				HttpHost proxy = new HttpHost("10.0.0.200", 80);
//				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
//				return;
//			} else if (info.startsWith("cmnet") || info.startsWith("uninet")
//					|| info.startsWith("ctnet") || info.startsWith("3gnet")) {
//				return;
//			}
//		}
//		String defaultProxyHost = android.net.Proxy.getDefaultHost();
//		int defaultProxyPort = android.net.Proxy.getDefaultPort();
//
//		if (defaultProxyHost != null && defaultProxyHost.length() > 0) {
//			if ("10.0.0.172".equals(defaultProxyHost.trim())) {
//				HttpHost proxy = new HttpHost("10.0.0.172", defaultProxyPort);
//				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
//			} else if ("10.0.0.200".equals(defaultProxyHost.trim())) {
//				HttpHost proxy = new HttpHost("10.0.0.200", 80);
//				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
//			} else {
//			}
//		} else {
//		}
//	}

//	public static String buildParamListInHttpRequest(List<NameValuePair> params) {
//		return URLEncodedUtils.format(params, "utf-8");
//	}
}
