package com.wf.irulu.logic;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.logic.listener.IsAddWishListListener;
import com.wf.irulu.logic.listener.NoInternetConnListener;
import com.wf.irulu.logic.listener.NotifyPhotoSetChangedListener;
import com.wf.irulu.logic.listener.ShoppingCartCountListener;
import com.wf.irulu.logic.listener.ShoppingCartEmptyListener;
import com.wf.irulu.logic.listener.TotalUnreadCountListener;
import com.wf.irulu.logic.listener.UnpaidOrderCountListener;
import com.wf.irulu.logic.listener.WishListCountListener;
import com.wf.irulu.logic.manager.CacheManager;
import com.wf.irulu.logic.manager.DaoManager;
import com.wf.irulu.logic.manager.PageManager;
import com.wf.irulu.logic.manager.ServiceManager;
import com.wf.irulu.logic.manager.SettingManager;
import com.wf.irulu.module.order.listener.IOrderslListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @描述: 应用程序的主控制器
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.logic
 * @类名:IruluController
 * @作者: 左西杰
 * @创建时间:2015-5-27 上午10:42:59
 * 
 */

public class IruluController {

	private static IruluController instance;
	private Toast networkToast;
	/**
	 * Hanldr Looper
	 */
	public static Handler mMainHandler = new Handler(Looper.getMainLooper());

	/**
	 * 存储所有页面Handler键值对 key:getClass().getCanonicalName() value:页面Handler
	 * !注意页面销毁时remove掉!
	 */
	private Map<String, Handler> handlerMap = new HashMap<String, Handler>();

	/**
	 * 页面管理类
	 */
	private PageManager pageManager;
	/**
	 * 缓存管理类
	 */
	private CacheManager cacheManager;

	/**
	 * 客户端配置类
	 */
	private SettingManager settingManager;

	/**
	 * 数据库操作管理类
	 */
	private DaoManager daoManager;

	/**
	 * 接口管理类
	 */
	private ServiceManager serviceManager;

	/**
	 * 本地XML配置文件
	 */
	private ConfigXML configXml;

	/**
	 * 无网络连接的监听
	 */
	private List<NoInternetConnListener> noInternetConnList = new LinkedList<NoInternetConnListener>();

	/**
	 * 购物车数量的监听
	 */
	private List<ShoppingCartCountListener> shoppongCartCountList = new ArrayList<ShoppingCartCountListener>();

	/**
	 * WishList的数量接收监听
	 */
	private List<WishListCountListener> wishListCountList = new ArrayList<WishListCountListener>();

	/**
	 * 未付款订单的数量接收监听
	 */
	private List<UnpaidOrderCountListener> unpaidOrderCountList = new ArrayList<UnpaidOrderCountListener>();

	/**
	 * 清空购物车的监听
	 */
	private List<ShoppingCartEmptyListener> shoppingCartEmptyList = new ArrayList<ShoppingCartEmptyListener>();

	/**
	 * 接收融云消息数目监听列表
	 */
	private List<TotalUnreadCountListener> totalUnreadCountList = new LinkedList<TotalUnreadCountListener>();

	/**
	 * 判断是否是加入WishList的监听
	 */
	private List<IsAddWishListListener> isAddWishListListener = new ArrayList<IsAddWishListListener>();

	/**
	 * 订单列表监听
	 */
	private LinkedList<IOrderslListener> orderList = new LinkedList<IOrderslListener>();

	/**
	 * 通知上传图片的个数改变监听
	 */
	private List<NotifyPhotoSetChangedListener> notifyPhotoSetChangedList = new ArrayList<NotifyPhotoSetChangedListener>();

	/**
	 * 初始化项目用到的类
	 */
	public IruluController() {
		// 界面管理初始化
		pageManager = new PageManager();
		// 缓存管理初始化
		cacheManager = new CacheManager(this);
		// 数据库操作管理类初始化
		daoManager = new DaoManager(this);
		// 接口管理初始化
		serviceManager = new ServiceManager(this);
		configXml = new ConfigXML(this);
	}

	/**
	 * 获取主控制器的实例
	 * 
	 * @return 主控制器的实例
	 */
	public static IruluController getInstance() {
		synchronized (IruluController.class) {
			if (null == instance) {
				instance = new IruluController();
			}
		}
		return instance;
	}

	/**
	 * 无网络
	 */
	public void showNoNetworkToast() {
		mMainHandler.post(new Runnable() {
			@SuppressLint("ShowToast")
			@Override
			public void run() {
				if (networkToast == null) {
					String text = IruluApplication.getInstance().getString(R.string.server_network_failed);
					networkToast = Toast.makeText(IruluApplication.getInstance(), text, Toast.LENGTH_SHORT);
				}
				networkToast.show();
			}
		});
	}

	/***************************网络状态改变 start*************************/
	/**
	 * 注册无网络连接监听
	 * @param listener
	 */
	public void registNoInternetConnListener(NoInternetConnListener listener) {
		if (!noInternetConnList.contains(listener)) {
			noInternetConnList.add(listener);
		}
	}
	
	/**
	 * 清除无网络连接监听
	 * @param listener
	 */
	public void unRegistNoInternetConnListener(NoInternetConnListener listener) {
		if (noInternetConnList.contains(listener)) {
			noInternetConnList.remove(listener);
		}
	}
	
	/**
	 * 网络状态通知界面更新
	 * @param b
	 */
	public synchronized void postNoInternetConnCallback(final Boolean b) {
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (NoInternetConnListener listener : noInternetConnList) {
					listener.noInternetConn(b);
				}
			}
		});
	}
	/***************************网络状态改变 end*************************/

	/***************************购物车数量改变 start*************************/
	/**
	 * 注册购物车数量监听
	 * @param listener
	 */
	public void registShoppongCartCountListenert(ShoppingCartCountListener listener){
		if(!shoppongCartCountList.contains(listener)){
			shoppongCartCountList.add(listener);
		}
	}

	/**
	 * 清除注册购物车数量监听
	 * @param listener
	 */
	public void unRegistShoppongCartCounListenert(ShoppingCartCountListener listener){
		if(shoppongCartCountList.contains(listener)){
			shoppongCartCountList.remove(listener);
		}
	}

	/**
	 * 购物车数量通知界面更新
	 * @param count
	 */
	public void postShoppongCartCountCallback(final int count){
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (ShoppingCartCountListener listener : shoppongCartCountList) {
					listener.shoppongCartCount(count);
				}
			}
		});
	}
	/***************************购物车数量改变 end*************************/

	/***************************WishList数量改变 start*************************/
	/**
	 * 注册WishList数量监听
	 * @param listener
	 */
	public void registWishListCountListener(WishListCountListener listener){
		if(!wishListCountList.contains(listener)){
			wishListCountList.add(listener);
		}
	}

	/**
	 * 清除注册WishList数量监听
	 * @param listener
	 */
	public void unRegistWishListCountListener(WishListCountListener listener){
		if(wishListCountList.contains(listener)){
			wishListCountList.remove(listener);
		}
	}

	/**
	 * WishList数量通知界面更新
	 * @param count
	 */
	public void postWishListCountCallback(final int count){
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (WishListCountListener listener : wishListCountList) {
					listener.wishListCount(count);
				}
			}
		});
	}
	/***************************WishList数量改变  end*************************/

	/***************************未付款订单的数量改变 start*************************/
	/**
	 * 未付款订单数量监听
	 * @param listener
	 */
	public void registUnpaidOrderCountListener(UnpaidOrderCountListener listener){
		if(!unpaidOrderCountList.contains(listener)){
			unpaidOrderCountList.add(listener);
		}
	}

	/**
	 * 清除注册未付款订单数量监听
	 * @param listener
	 */
	public void unRegistUnpaidOrderCountListener(UnpaidOrderCountListener listener){
		if(unpaidOrderCountList.contains(listener)){
			unpaidOrderCountList.remove(listener);
		}
	}

	/**
	 * 未付款订单数量通知界面更新
	 * @param count
	 */
	public void postUnpaidOrderCountCallback(final int count){
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (UnpaidOrderCountListener listener : unpaidOrderCountList) {
					listener.UnpaidOrderCount(count);
				}
			}
		});
	}
	/***************************未付款订单的数量改变  end*************************/

	/***************************清空or刷新购物车 start*************************/
	/**
	 * 注册清空购物车监听
	 * @param listener
	 */
	public void registShoppingCartEmptyListener(ShoppingCartEmptyListener listener) {
		if (!shoppingCartEmptyList.contains(listener)) {
			shoppingCartEmptyList.add(listener);
		}
	}

	/**
	 * 清除清空购物车监听
	 * @param listener
	 */
	public void unRegistShoppingCartEmptyListener(ShoppingCartEmptyListener listener) {
		if (shoppingCartEmptyList.contains(listener)) {
			shoppingCartEmptyList.remove(listener);
		}
	}

	/**
	 * 通知界面更新
	 */
	public synchronized void postShoppingCartEmptyCallback(final ArrayList<ProductCart> productList) {
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (ShoppingCartEmptyListener listener : shoppingCartEmptyList) {
					listener.shoppingCartEmpty(productList);
				}
			}
		});
	}
	/***************************清空or刷新购物车 end*************************/

	/***************************注册融云消息总的未读数 start*************************/
	/**
	 * 注册融云消息总的未读数监听
	 * @param listener
	 */
	public void registTotalUnreadCountListener(TotalUnreadCountListener listener) {
		if (!totalUnreadCountList.contains(listener)) {
			totalUnreadCountList.add(listener);
		}
	}

	/**
	 * 清除注册融云消息总的未读数监听
	 * @param listener
	 */
	public void unRegistTotalUnreadCountListener(TotalUnreadCountListener listener) {
		if (totalUnreadCountList.contains(listener)) {
			totalUnreadCountList.remove(listener);
		}
	}

	/**
	 * 融云消息通知界面更新
	 * @param num
	 */
	public synchronized void postTotalUnreadCountCallback(final int num) {
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (TotalUnreadCountListener listener : totalUnreadCountList) {
					listener.totalUnreadCount(num);
				}
			}
		});
	}
	/***************************注册融云消息总的未读数 start*************************/

	/***************************订单列表刷新 start*************************/
	/**
	 * 注册订单列表刷新
	 * @param listener
	 */
	public void registerIOrderList(IOrderslListener listener){
		if (!orderList.contains(listener)) {
			orderList.add(listener);
		}
	}

	/**
	 * 反注册订单列表刷新
	 */
	public void unRegisterIOrderList(){
		orderList.clear();
	}

	/**
	 * 反注册订单列表刷新
	 * @param listener
	 */
	public void unRegisterIOrderList(IOrderslListener listener){
		if (orderList.contains(listener)) {
			orderList.remove(listener);
		}
	}

	/**
	 * 通知订单列表刷新
	 */
	public synchronized void postOrderListCallback(){
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (IOrderslListener listener : orderList) {
					listener.refresh();
				}
			}
		});
	}
	/***************************订单列表刷新 end*************************/

	/***************************注册通知上传图片的个数改变 start*************************/
	/**
	 * 注册通知上传图片的个数改变监听
	 * @param listener
	 */
	public void registNotifyPhotoSetChangedListener(NotifyPhotoSetChangedListener listener) {
		if (!notifyPhotoSetChangedList.contains(listener)) {
			notifyPhotoSetChangedList.add(listener);
		}
	}

	/**
	 * 清除通知上传图片的个数改变监听
	 * @param listener
	 */
	public void unRegistNotifyPhotoSetChangedListener(NotifyPhotoSetChangedListener listener) {
		if (notifyPhotoSetChangedList.contains(listener)) {
			notifyPhotoSetChangedList.remove(listener);
		}
	}

	/**
	 * 通知界面更新
	 * @param photos
	 */
	public synchronized void postNotifyPhotoSetChangedCallback(final ArrayList<String> photos) {
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (NotifyPhotoSetChangedListener listener : notifyPhotoSetChangedList) {
					listener.notifyPhotoSetChanged(photos);
				}
			}
		});
	}
	/***************************注册通知上传图片的个数改变 end*************************/

	/***************************判断是否是加入WishList的监听 start*************************/
	/**
	 * 注册清空购物车监听
	 * @param listener
	 */
	public void registIsAddWishListListener(IsAddWishListListener listener) {
		if (!isAddWishListListener.contains(listener)) {
			isAddWishListListener.add(listener);
		}
	}

	/**
	 * 清除清空购物车监听
	 * @param listener
	 */
	public void unRegistIsAddWishListListener(IsAddWishListListener listener) {
		if (isAddWishListListener.contains(listener)) {
			isAddWishListListener.remove(listener);
		}
	}

	/**
	 * 通知界面更新
	 */
	public synchronized void postIsAddWishListCallback(final String productId, final String addWishList) {
		mMainHandler.post(new Runnable() {
			@Override
			public void run() {
				for (IsAddWishListListener listener : isAddWishListListener) {
					listener.isAddWishList(productId,addWishList);
				}
			}
		});
	}
	/***************************判断是否是加入WishList的监听 end*************************/

	/************ Get方法******start ******/
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public SettingManager getSettingManager() {
		return settingManager;
	}

	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public DaoManager getDaoManager() {
		return daoManager;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public ConfigXML getConfigXml() {
		return configXml;
	}



	/************ Get方法******end ******/

}
