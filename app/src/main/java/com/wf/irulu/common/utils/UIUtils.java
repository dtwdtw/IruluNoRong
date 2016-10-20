package com.wf.irulu.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;

/**
 * @描述: 和UI操作相关的类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:UIUtils
 * @作者 左杰
 * @创建时间:2015-5-3 下午5:23:30
 * 
 */
public class UIUtils {

	/**
	 * 上下文的获取
	 * 
	 * @return
	 */
	public static Context getContext(){
		return IruluApplication.getInstance();
	}
	
	/**
	 * 获取资源
	 * @return
	 */
	public static Resources getResources(){
		return IruluApplication.getInstance().getResources();
	}

	/**
	 * 
	 * @param dip
	 * @return
	 */
	public static int dip2px(int dip){
		// 公式 1: px = dp * (dpi / 160)
		// 公式 2: dp = px / denistity;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		// metrics.densityDpi
		return (int) (dip * density + 0.5f);
	}

	public static int px2dip(int px){
		// 公式 1: px = dp * (dpi / 160)
		// 公式 2: dp = px / denistity;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		// metrics.densityDpi
		return (int) (px / density + 0.5f);
	}

	public static int sp2px(int sp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float fontScale = metrics.scaledDensity;
		return (int) (sp * fontScale + 0.5f);
	}

	/** 获取文字 */
	public static String getString(int resId){
		return getResources().getString(resId);
	}

	/**
	 * 获得应用程序的包名
	 * @return
	 */
	public static String getPackageName(){
		return IruluApplication.getInstance().getPackageName();
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId){
		return getResources().getStringArray(resId);
	}
	
	/** 获取颜色 */
	public static int getColor(int resId){
		return getResources().getColor(resId);
	}
	
	public static View inflate(int resId){
		return LayoutInflater.from(IruluApplication.getInstance()).inflate(resId, null);
	}
	
	public static void getToastShort(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void getToastLong(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return IruluApplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static long getMainThreadId() {
		return IruluApplication.getMainThreadId();
	}
	//判断当前的线程是不是在主线程
	public static boolean isRunInMainThread(long threadId) {
		return threadId == getMainThreadId();
	}
	/**
	 * 主线程中执行 任务
	 * @param runnable
	 */
	public static void runInMainThread(Runnable runnable,long threadId){
		if (isRunInMainThread(threadId)){// 如果在主线程中执行
			runnable.run();
		}else{
			// 需要转到主线程执行
			post(runnable);
		}
	}

	/**
	 * 获取首页图片的尺寸 300*300
	 * @return
	 */
	public static int getSecondWidth(){
		return (ConstantsUtils.DISPLAYW - (getResources().getDimensionPixelSize(R.dimen.home_product_item_margin * 3 + getResources().getDimensionPixelSize(R.dimen.home_product_image_margin) * 2))) / 2;
	}

	/**
	 * 获取分类二级、三级列表/wish list列表的尺寸 210*210
	 * @return
	 */
	public static int getThirdWidth(){
		return UIUtils.dip2px(getResources().getDimensionPixelSize(R.dimen.classify_product_image));
	}

	/**
	 * 获取用户推荐图片尺寸 200*200
	 * @return
	 */
	public static int getFourthWidth(){
		return UIUtils.dip2px(getResources().getDimensionPixelSize(R.dimen.product_image_love));
	}

	/**
	 * 获取购物车图片、单页面产品图片的尺寸 180*180
	 * @return
	 */
	public static int getFifthWidth(){
		return UIUtils.dip2px(getResources().getDimensionPixelSize(R.dimen.shoppingcart_image));
	}

	/**
	 * 获取订单商品、评论图片尺寸 120*120
	 * @return
	 */
	public static int getSixthWidth(){
		return UIUtils.dip2px(getResources().getDimensionPixelSize(R.dimen.order_product_image));
	}



}
