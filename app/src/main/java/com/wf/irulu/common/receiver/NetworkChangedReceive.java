package com.wf.irulu.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.NetworkUtils;
import com.wf.irulu.logic.IruluController;

/**
 * 
 * @描述: 网络转换广播接收器
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.receiver
 * @类名:NetworkChangedReceive
 * @作者: 左西杰
 * @创建时间:2015-8-31 下午12:40:19
 *
 */
public class NetworkChangedReceive extends BroadcastReceiver {
	
	private final String TAG = getClass().getCanonicalName();
	public static final String NETWORK_CHANGED_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private static BroadcastReceiver mReceive = null;
	public static int isnet = 0;
	
	public NetworkChangedReceive() {
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ILog.i(TAG, "注册网络连接-------");
		try {
			IruluController controller = IruluController.getInstance();
			
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (null == connManager) {
				return;
			}
			
			// 判断是否正在使用WIFI网络
			State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			
			if (null == state) {
				return;
			}
			isnet = State.CONNECTED == state ? 0 : -1;
			// WIFI 连接不成功的情况下开启GPRS连接网络
			if (State.CONNECTED != state) {
				state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
				
				if (null == state) {
					return;
				}
				
				// 判断是否正在使用GPRS网络
				isnet = State.CONNECTED == state ? 0 : -1;
			}
			if (isnet == -1) {
				//无网络
				ILog.i(TAG, "------无网络连接------");
//				controller.getImManager().closeConn();
				controller.postNoInternetConnCallback(true);
				//TODO
				/*Handler handler = controller.getHandlerMap().get(MixActivity.class.getCanonicalName());
				if (null != handler) {
					handler.sendEmptyMessage(MixActivity.WHAT_NO_NETWROK);
				}*/
			} else {
				//有网络
				String type = "WIFI";
				int networkType = NetworkUtils.getInstance().getNetworkType();
				switch (networkType) {
				case NetworkUtils.NETWORK_TYPE_WIFI:
					type = "WIFI";
					break;
				case NetworkUtils.NETWORK_TYPE_4G:
					type = "4G";
					break;
				case NetworkUtils.NETWORK_TYPE_3G:
					type = "3G";
					break;
				case NetworkUtils.NETWORK_TYPE_2G:
					type = "2G";
					break;
				default:
					break;
				}
				ILog.i(TAG, "已连接网络，网络类型:" + type);
				ConstantsUtils.NETWORK_TYPE = type;
				controller.postNoInternetConnCallback(false);
				//TODO
			/*	LoginUser user = controller.getCacheManager().getLoginUser();
				if (null != user && !user.isLogin && controller.isToMain) {
					controller.getServiceManager().getAasService().login(HttpConstant.LOGIN_TYPE_MOBILE, user.mobile, user.password, null, null, null);
				} else {
					if (!IMManager.getInstance().isLogined()) {
						IMManager.getInstance().onResumeLogin();
					}
				}
				
				Handler handler = controller.getHandlerMap().get(MixActivity.class.getCanonicalName());
				if (null != handler) {
					handler.sendEmptyMessage(MixActivity.WHAT_HAS_NETWROK);
				}*/
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/***
	 * 注册网络监听广播
	 */
	public static void beginListenNetworkChange() {
		if(null == mReceive) {
			try {
				IntentFilter filter = new IntentFilter(NetworkChangedReceive.NETWORK_CHANGED_ACTION);
				mReceive = new NetworkChangedReceive();
				IruluApplication.getInstance().registerReceiver(mReceive, filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/***
	 * 注销网络监听广播
	 */
	public static void finishListenNetworkChange () {
		if(null != mReceive) {
			try {
				IruluApplication.getInstance().unregisterReceiver(mReceive);
				mReceive = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

