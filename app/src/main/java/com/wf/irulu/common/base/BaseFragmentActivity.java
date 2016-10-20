package com.wf.irulu.common.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.AdInfo;
import com.wf.irulu.common.bean.PopupInfo;
import com.wf.irulu.common.bean.StartAdvertising;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.advertising.dialog.AdvertisingDialog;

/**
 * @描述: FragmentActivity的基类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.base
 * @类名:BaseFragmentActivity
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午2:43:21
 * 
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements OnClickListener {
	
	private static final int DIALOG_PROGRESS = 0x8;
	public IruluController controller;
	private BaseFragment fragment;
	private Dialog dialog;
	public Toast toast;
	private AdvertisingDialog mDialog;
	public AppEventsLogger mLoger;
	/**app 从后台唤醒，进入前台 ,true:前台，false:后台*/
	public boolean isActive = true;
	/** 弹出广告当前时间*/
	private long currentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		controller = IruluController.getInstance();
		controller.getPageManager().addPage(this);
		
		setContentView();
		setView();
		initView();//	findView();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		controller.getPageManager().removePage(this);
	}
	
	/**
	 * 创建对话框
	 */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		dismissProgressDialog();
		if (null == dialog) {
			dialog = new Dialog(this, R.style.Theme_LargeDialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_progress_loading);
		}

		if (null != args && null != args.getString("msg")) {
			String msg = args.getString("msg");
			TextView tv = (TextView) dialog.findViewById(R.id.dialog_tv_tips);
			tv.setText(msg);
		}

		if(null == mDialog){
			mDialog = new AdvertisingDialog(this,R.style.Theme_Hold_Dialog_Base);
		}
		if(null != mDialog && null != args.getParcelable("popupInfo")){
			PopupInfo popupInfo = args.getParcelable("popupInfo");
			mDialog.setPopupInfo(popupInfo);
			return mDialog;
		}

		return dialog;
	}
	
	/**
	 * 显示进度对话框
	 */
	public void showProgressDialog() {
		if (!this.isFinishing()) {
			if (null == dialog) {
				showDialog(DIALOG_PROGRESS);
			}
			if (null != dialog && !dialog.isShowing()) {
				dialog.show();
			}
		}
	}

	/**
	 * 显示进度对话框
	 * @param msg
	 */
	public void showProgressDialog(String msg) {
		if (!this.isFinishing()) {
			if (null == dialog) {
				Bundle data = new Bundle();
				data.putString("msg", msg);
				showDialog(DIALOG_PROGRESS, data);
			}
			if (null != dialog && !dialog.isShowing()) {
				dialog.show();
			}
		}
	}

	/**
	 * 显示进度对话框
	 * @param resId
	 */
	public void showProgressDialog(int resId) {
		showProgressDialog(getString(resId));
	}
	
	/**
	 * 取消进度对话框
	 */
	public void dismissProgressDialog() {
		if (null != dialog && dialog.isShowing()) {
			dialog.cancel();
			removeDialog(DIALOG_PROGRESS);
		}
	}

	/**
	 * 显示土司
	 * @param resId
	 */
	public void showToast(int resId) {
		showToast(getString(resId));
	}

	/**
	 * 显示长土司
	 * @param resId
	 */
	public void showLongToast(int resId) {
		showLongToast(getString(resId));
	}

	/**
	 * 显示土司
	 * @param title
	 */
	public void showToast(String title) {
		if (null == toast) {
			toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		toast.setText(title);
		toast.show();
	}

	/**
	 * 显示长土司
	 * @param title 土司内容
	 */
	public void showLongToast(String title) {
		if (null == toast) {
			toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		}
		toast.setText(title);
		toast.show();
	}

	public Fragment findFragByTag(String tag) {
		Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
		return frag;
	}
	
	public void showFragment(BaseFragment frag) {
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		if (null != fragment) {
			ft.hide(fragment);
		}
		
		this.fragment = frag;
		
		ft.show(frag);
		ft.commitAllowingStateLoss();
		
		/*if (fragment instanceof MyFragment) {
			((MyFragment)fragment).initData();
		}*/
		
	}
	
	public void addFragment(int resId, BaseFragment frag) {
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		if (fragment != null ) {
			ft.hide(fragment);
		}
		
		this.fragment = frag;
		
		String tag = frag.getClass().getCanonicalName();
		ft.add(resId, frag, tag);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commitAllowingStateLoss();
	}

	public void replaceFragment(int resId, BaseFragment frag) {
		
		this.fragment = frag;
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		String tag = frag.getClass().getCanonicalName();
		ft.replace(resId, frag, tag);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commitAllowingStateLoss();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	/**
	 * 设置内容视图，让子类实现
	 */
	public abstract void setContentView();

	/**
	 * 如果子类需要，子类去实现
	 */
	protected void setView() {}

	/**
	 * 初始化视图，让子类实现
	 */
	public abstract void initView();
	/**
	 * 初始化数据，让子类实现
	 */
	public abstract void initData();
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isActive){
			//app 从后台唤醒，进入前台
			ILog.i("zxj", "app 从后台唤醒，进入前台--BaseFragmentActivity");
			long oldTime = CacheUtils.getLong(this, "currentTime");
			currentTime = System.currentTimeMillis();
			long time = currentTime - oldTime;
			if(time > 60*60*1000) {//60*60*1000
//			mDialog = new AdvertisingDialog(this);
//			AdvertisingServiceListener listener = AdvertisingServiceListener.getInstance(this, mDialog);
				//联网请求启动广告
				controller.getServiceManager().getHomeService().startAdvertising(listener);
			}
		}
		MobclickAgent.onResume(this);
		mLoger = AppEventsLogger.newLogger(this);
		mLoger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		boolean isbackground = IruluApplication.getInstance().isbackground();
		if (isbackground) {
			ILog.i("zxj", "app 进入后台--BaseFragmentActivity");
			//app 进入后台
			isActive = false;//全局变量isActive = false 记录当前已经进入后台
		}
	}

	ServiceListener listener = new ServiceListener() {
		@Override
		public void serviceSuccess(ServiceListener.ActionTypes action, Object bandObj, Object returnObj) {
			switch (action){
				case START_ADVERTISING:
					StartAdvertising startAdvertising = (StartAdvertising) returnObj;
					final AdInfo adInfo = startAdvertising.getAdInfo();//启动广告信息
					PopupInfo popupInfo = startAdvertising.getPopupInfo();//弹窗广告信息
//					AdvertisingDialog dialog = new AdvertisingDialog(BaseActivity.this,popupInfo);
//					mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//设定为系统级警告，关键
					String image = popupInfo.getImage();
					if (null == mDialog && !TextUtils.isEmpty(image) ) {
						Bundle data = new Bundle();
						data.putParcelable("popupInfo", popupInfo);
						showDialog(ConstantsUtils.DIALOG_PROGRESS, data);
					}
					if(null != mDialog && !mDialog.isShowing() && null != popupInfo){
						mDialog.show();
					}
					CacheUtils.setLong(BaseFragmentActivity.this, "currentTime", currentTime);
					break;
				default:
					break;
			}
		}

		@Override
		public void serviceFailure(ServiceListener.ActionTypes action, Object returnObj, int errorCode) {

		}

		@Override
		public void serviceCallback(ServiceListener.ActionTypes action, int type, Object returnObj) {

		}
	};
}
