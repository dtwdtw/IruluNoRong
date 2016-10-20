package com.wf.irulu.logic.manager;

import android.os.Environment;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.FileUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.LogUtils;
import com.wf.irulu.logic.IruluController;

/**
 * 
 * @描述: 项目缓存管理类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.manager
 * @类名:CacheManager
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午3:57:51
 *
 */
public class CacheManager {
	
	private static final String TAG = CacheManager.class.getCanonicalName();

	public static final String JSON_CACHE_HOME_DATA = "home_data_1.3.0";

	public static String getJsonCacheMyVisitorPath() {
		return CacheManager.SD_CACHE_DIR + "/" + CacheManager.JSON_CACHE_HOME_DATA;
	}

	//*************有SD卡时路径******start******//
	// SD卡路径
	public static final String SD_DIR = Environment.getExternalStorageDirectory().getPath();
	// 项目路径
	public static final String SD_APP_DIR = SD_DIR + "/." + ConstantsUtils.APP_NAME;
	// Log保存目录
	public static final String SD_LOG_DIR = SD_APP_DIR + "/.log";
	// 文件保存路径
	public static final String SD_SAVE_DIR = SD_APP_DIR + "/.save";
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
	//*************无SD时提醒用户无SD卡图片无法加载************//
	/*登录用户*/
	private LoginUser loginUser;				// 登录用户
	private IruluController controller;

	public CacheManager(IruluController controller) {
		this.controller = controller;
		init();
	}
	
	public void init() {
		// 删除以前的残留文件夹
		FileUtils.deleteDirectory(SD_DIR + "/irulu");
		FileUtils.deleteDirectory(SD_DIR + "/irulu_");
		ILog.setLogDirPath(SD_LOG_DIR);
		createProjectDir();
	}
	
	private void createProjectDir() {
		LogUtils.d("createProjectDir:" + FileUtils.hasSDCard());
		if ( FileUtils.hasSDCard()) {
			FileUtils.createDirs(SD_APP_DIR);
			FileUtils.createDirs(SD_LOG_DIR);
			FileUtils.createDirs(SD_SAVE_DIR);
			FileUtils.createDirs(SD_CRASH_DIR);
			FileUtils.createDirs(SD_CACHE_DIR);
			FileUtils.createDirs(SD_IMAGE_DIR);
			FileUtils.createDirs(SD_AUDIO_DIR);
			FileUtils.createDirs(SD_VIDEO_DIR);
		} else {
			// do something 后续添加
			LogUtils.d("createProjectDir:手机无SD卡");
		}
	}

	/**
	 * 获取登录用户
	 * TODO
	 * @return
	 */
	public LoginUser getLoginUser() {
		IruluApplication instance = IruluApplication.getInstance();
		if (null == loginUser) {
			loginUser = new LoginUser();
		}
		if (loginUser.getUid() == 0 || loginUser.getUid() == -1) {
			loginUser = new LoginUser();
//			uid
			loginUser.setUid((int) CacheUtils.getLong(instance, "uid"));//用户id
//			email
			loginUser.setEmail(CacheUtils.getString(instance, "email"));//用户名 email
//			lastname
			loginUser.setFirstname(CacheUtils.getString(instance, "lastname"));//姓
//			firstname
			loginUser.setFirstname(CacheUtils.getString(instance, "firstname"));//名
//			nickname
			loginUser.setNickname(CacheUtils.getString(instance, "nickname"));//昵称
//			registerDate
			loginUser.setRegisterDate((int) CacheUtils.getLong(instance, "registerDate"));//注册时间（时间戳）
//			froms
			loginUser.setFroms((int) CacheUtils.getLong(instance, "froms"));//注册来源：1 开机注册,2 MOBILE,3 PC
//			headjpg
			loginUser.setHeadjpg(CacheUtils.getString(instance, "headjpg"));//头像URL
//			sex
			loginUser.setSex(CacheUtils.getString(instance, "sex"));//用户性别：M男，F女
//			token
			loginUser.setToken(CacheUtils.getString(instance, "token"));  // 令牌
//			rongCloudToken
			loginUser.setRongCloudToken(CacheUtils.getString(instance, "rong_cloud_token"));
//			birthday
			loginUser.setBirthday(CacheUtils.getString(instance, "birthday"));
			loginUser.setUserType((int) CacheUtils.getLong(instance, "user_type"));  // 用户类型
			loginUser.setPassword(CacheUtils.getString(instance, "password"));  // 密码
//			ILog.e(TAG, "loginUser.uid:" + loginUser.getUid());
		}
		return loginUser;
	}
	
	/**
	 * 设置登录用户
	 * @return
	 */
	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

}
