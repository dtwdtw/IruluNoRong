package com.wf.irulu.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.utils.ConstantsUtils;

/**
 * 
 * @描述: 本地存储类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic
 * @类名:ConfigXML
 * @作者: 左西杰
 * @创建时间:2015-6-3 上午9:59:08
 *
 */
public class ConfigXML {
	
//	// 消息通知设置
//	public static final String KEY_MSG_SET_NOTIFY = "MsgSetNotify";
//	// 消息声音设置
//	public static final String KEY_MSG_SET_SOUND = "MsgSetSound";
//	// 消息振动设置
//	public static final String KEY_MSG_SET_TIP = "MsgSetTip";
//	// 群消息通知设置
//	public static final String KEY_GROUP_SET_NOTIFY = "GroupSetNotify";
//
//	// 位置
//	public static final String LATITUDE = "latitude";
//	public static final String LONGITUDE = "longitude";

	/**
	 * SharedPreference的 存储类和编辑类对象
	 */
	private SharedPreferences share;
	private Editor editor;

	/**
	 * 存储的全局的SharedPreferences文件名
	 */
	private final static String KKLINK = "com.wf.irulu";
	/**
	 * 读写权限
	 */
	private final static int MODE = Context.MODE_PRIVATE;
	
	private IruluController controller;


	//*************有SD卡时路径******start******//
	// SD卡路径
	public static final String SD_DIR = Environment.getExternalStorageDirectory().getPath();
	// 项目路径
	public static final String SD_APP_DIR = SD_DIR + "/" + ConstantsUtils.APP_NAME;
	// Log保存目录
	public static final String SD_LOG_DIR = SD_APP_DIR + "/.log";
	// 文件保存路径
	public static final String SD_SAVE_DIR = SD_APP_DIR + "/save";
	// 异常日志路径
	public static final String SD_CRASH_DIR = SD_APP_DIR + "/.crash";
	// 缓存路径
	public static final String SD_CACHE_DIR = SD_APP_DIR + "/.cache";
	// 图片路径
	public static final String SD_IMAGE_DIR = SD_APP_DIR + "/.image";
	// 音频路径
	public static final String SD_AUDIO_DIR = SD_APP_DIR + "/.audio";
	// 视频路径
	public static final String SD_VIDEO_DIR = SD_APP_DIR + "/video";

	//*************有SD卡时路径******end******//

	/**
	 * 初始化时默认读取
	 */
	@SuppressLint("CommitPrefEdits")
	public ConfigXML(IruluController controller) {
		this.controller = controller;
		share = IruluApplication.getInstance().getSharedPreferences(KKLINK, MODE);
		editor = share.edit();
	}

	/**
	 * 保存String型数据
	 * 
	 * @param key 数据键名
	 * @param value 数据键值
	 * @return boolean
	 */
	public boolean save(String key, String value) {
		int userid = controller.getCacheManager().getLoginUser().getUid();
		editor.putString(userid + key, value);
		return editor.commit();
	}
	
	public int read(String key, int back) {
		int userid = controller.getCacheManager().getLoginUser().getUid();
		int result = share.getInt(userid + key, back);
		return result;
	}
	
	public boolean save(String key, int value) {
		int userid = controller.getCacheManager().getLoginUser().getUid();
		editor.putInt(userid + key, value);
		return editor.commit();
	}
	
	public long read(String key, long back) {
		long result = share.getLong(key, back);
		return result;
	}
	
	public boolean save(String key, long value) {
		editor.putLong(key, value);
		return editor.commit();
	}
	
	public float read(String key, float back) {
		float result = share.getFloat(key, back);
		return result;
	}
	
	public boolean save(String key, float value) {
		editor.putFloat(key, value);
		return editor.commit();
	}

	/**
	 * 读取String型数据
	 * 
	 * @param key 所读取的数据的键值
	 * @param back 如果所读取数据的键值不存在，那么返回此默认值
	 * @return String
	 */
	public String read(String key, String back) {
		int userid = controller.getCacheManager().getLoginUser().getUid();
		String result = share.getString(userid + key, back);
		return result;
	}

	/**
	 * 保存Boolean型数据
	 * 
	 * @param key 数据键名
	 * @param value 数据键值
	 * @return boolean
	 */
	public boolean save(String key, Boolean value) {
		int userid = controller.getCacheManager().getLoginUser().getUid();
		editor.putBoolean(userid + key, value);
		return editor.commit();
	}

	/**
	 * 读取Boolean型数据
	 * 
	 * @param key 所读取的数据的键值
	 * @param back 如果所读取数据的键值不存在，那么返回此默认值
	 * @return boolean
	 */
	public Boolean read(String key, Boolean back) {
		int userid = controller.getCacheManager().getLoginUser().getUid();
		Boolean result = share.getBoolean(userid + key, back);
		return result;
	}

}
