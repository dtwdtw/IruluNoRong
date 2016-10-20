package com.wf.irulu.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.wf.irulu.component.qiniu.common.Constants;

/**
 * 
 * @描述: 缓存工具类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:CacheUtils
 * @作者: 左西杰
 * @创建时间:2015-7-8 上午10:00:20
 *
 */
public class CacheUtils {

	private static final String SP_NAME = "irulu";
	private static SharedPreferences mSp;
	
	private static SharedPreferences getSp(Context context){
		if(mSp == null){
			mSp = context.getSharedPreferences(SP_NAME,context.MODE_PRIVATE);
		}
		return mSp;
	}
	/**
	 * 通过SP获得boolean类型的数据，没有,默认为false
	 * @param context	上下文
	 * @param key 存储的key
	 * @return
	 */
	public static boolean getBoolean(Context context ,String key){
		SharedPreferences sp = getSp(context);
		return sp.getBoolean(key, false);
	}
	/**
	 * 通过SP获得boolean类型的数据，没有默认为false
	 * @param context	上下文
	 * @param key	存储的key
	 * @param defValue	默认值
	 * @return
	 */
	public static boolean getBoolean(Context context ,String key,boolean defValue){
		SharedPreferences sp = getSp(context);
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 设置boolean的缓存数据
	 * @param context
	 * @param key	缓存对应的key
	 * @param value 缓存对应的值
	 */
	public static void setBoolean(Context context ,String key, boolean value) {
		SharedPreferences sp = getSp(context);
		sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * 通过SP获得boolean类型的数据，没有,默认为false
	 * @param context	上下文
	 * @param key 存储的key
	 * @return
	 */
	public static String getString(Context context ,String key){
		SharedPreferences sp = getSp(context);
		return sp.getString(key, null);
	}
	/**
	 * 通过SP获得String类型的数据，没有默认为null
	 * @param context	上下文
	 * @param key	存储的key
	 * @param defValue	默认值
	 * @return
	 */
	public static String getString(Context context ,String key,String defValue){
		SharedPreferences sp = getSp(context);
		return sp.getString(key, defValue);
	}

	/**
	 * 设置String的缓存数据
	 * @param context
	 * @param key	缓存对应的key
	 * @param value 缓存对应的值
	 */
	public static void setString(Context context ,String key, String value) {
		SharedPreferences sp = getSp(context);

		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * 通过SP获得Long类型的数据，没有默认为0
	 * @param context	上下文
	 * @param key 存储的key
	 * @return
	 */
	public static long getLong(Context context ,String key){
		SharedPreferences sp = getSp(context);
		return sp.getLong(key, 0);
	}
	/**
	 * 通过SP获得Long类型的数据
	 * @param context	上下文
	 * @param key	存储的key
	 * @param defValue	默认值
	 * @return
	 */
	public static long getLong(Context context ,String key,long defValue){
		SharedPreferences sp = getSp(context);
		return sp.getLong(key, defValue);
	}

	/**
	 * 设置Long的缓存数据
	 * @param context
	 * @param key	缓存对应的key
	 * @param value 缓存对应的值
	 */
	public static void setLong(Context context ,String key, long value) {
		SharedPreferences sp = getSp(context);
		sp.edit().putLong(key, value).commit();
	}
	/**
	 * 设置int的缓存数据
	 * @param context
	 * @param key	缓存对应的key
	 * @param value 缓存对应的值
	 */
	public static void setInt(Context context ,String key, int value) {
		SharedPreferences sp = getSp(context);
		sp.edit().putInt(key, value).commit();
	}
	/**
	 * 通过SP获得int类型的数据
	 * @param context	上下文
	 * @param key	存储的key
	 * @param defValue	默认值
	 * @return
	 */
	public static int getInt(Context context ,String key){
		SharedPreferences sp = getSp(context);
		return sp.getInt(key,0);
	}

	public static void remove(Context context,String key){
		SharedPreferences sp=getSp(context);
		sp.edit().remove(key).commit();
	}
	
}
