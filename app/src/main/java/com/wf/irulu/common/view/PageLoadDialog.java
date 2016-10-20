package com.wf.irulu.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wf.irulu.R;

/**
 * 
 * @描述: 应用程序UI工具包：封装UI相关的一些操作
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.view
 * @类名:UIHelper
 * @作者: 左西杰
 * @创建时间:2015-7-29 下午3:29:54
 *
 */
public class PageLoadDialog {
	
	/** 加载数据对话框 */
	private static Dialog mLoadingDialog;
	
	/**
	 * 显示对话框，非单例
	 * @param context
	 * @param canCancel		是否允许点击back键取消对话框
	 * @param cancelable	是否允许页面退出
	 */
	public static void showDialogForLoading(final Context context, boolean canCancel, boolean cancelable) {
		mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
		ShowDialogOperation(context, canCancel, cancelable, mLoadingDialog);
	}

	/**
	 *	显示单例的对话框，防止多个对话框显示
	 * @param context
	 * @param canCancel		是否允许点击back键取消对话框
	 * @param cancelable	是否允许页面退出
	 */
	public static void showSingletonDialogForLoading(final Context context, boolean canCancel, boolean cancelable) {
		mLoadingDialog = getDialogInstance(context);
		ShowDialogOperation(context, canCancel, cancelable, mLoadingDialog);
	}

	private static void ShowDialogOperation(final Context context, boolean canCancel, boolean cancelable, final Dialog mLoadingDialog) {
		if (context == null || ((Activity)context).isFinishing()) {
			return;
		}
		View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
		mLoadingDialog.setCancelable(canCancel);//但是这样就不能响应返回键的事件了
		mLoadingDialog.setCanceledOnTouchOutside(false);
		if (cancelable && canCancel) {
			mLoadingDialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						boolean showing = mLoadingDialog.isShowing();
						if (mLoadingDialog!= null && showing) {
							hideDialogForLoading();
							((Activity)context).finish();
						}
					}
					return false;
				}
			});
		}
		mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mLoadingDialog.show();
	}

	/**
	 * 关闭加载对话框
	 */
	public static void hideDialogForLoading() {
		if(mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.cancel();
			mLoadingDialog = null;
		}
	}

	/**
	 * 获取单例的对话框
	 * @param context
	 * @return
	 */
	public static Dialog getDialogInstance(Context context){
		synchronized (context){
			if(null == mLoadingDialog)
				mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
		}
		return mLoadingDialog;
	}
}
