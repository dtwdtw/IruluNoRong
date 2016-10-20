package com.wf.irulu.common.utils;

import android.os.Environment;
import android.util.Log;

import com.wf.irulu.logic.manager.SettingManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @描述: 日志的功能操作类 可将日志保存至SD卡
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:ILog
 * @作者: 左西杰
 * @创建时间:2015-8-21 下午7:03:09
 *
 */
public class ILog {

	/**
	 * DEBUG级别开关
	 */
	public static  boolean DEBUG = SettingManager.DEBUG;

	/**
	 * DEBUG_ERROR级别开关
	 */
	private static final boolean DEBUG_ERROR = SettingManager.DEBUG;

	/**
	 * 是否保存至SD卡
	 */
	private static final boolean SAVE_TO_SD = SettingManager.DEBUG;


//	/**
//     * DEBUG级别开关
//     */
//	public static  boolean DEBUG = true;
//
//    /**
//     * DEBUG_ERROR级别开关
//     */
//	private static final boolean DEBUG_ERROR = true;
//
//	/**
//	 * 是否保存至SD卡
//	 */
//	private static final boolean SAVE_TO_SD = true;

	/**
	 * 保存LOG日志的目录
	 */
	private static String LogDirPath;

	/**
	 * 日志打印时间Format
	 */
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat LOG_FMT = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * 设置Log保存目录
     * @param dir
     */
    public static void setLogDirPath(String dir) {
    	LogDirPath = dir;
    }
    
    /**
     * 获取Log保存目录
     * @return
     */
    public static String getLogDirPath() {
    	return LogDirPath;
    }

	/**
	 * 用于打印错误级的日志信息
	 * @param strModule LOG TAG
	 * @param strErrMsg 打印信息
	 */
	public static void e(String strModule, String strErrMsg) {
		if (DEBUG_ERROR) {
			Log.e(strModule, "----" + strErrMsg + "----");
			if (SAVE_TO_SD) {
				storeLog(strModule, strErrMsg);
			}
		}
	}
	
	/**
	 * 用于打印异常的的日志信息
	 * @param strModule LOG TAG
	 * @param strErrMsg 打印信息
	 */
	public static void e(String strModule, String strErrMsg, Exception e) {
		if (DEBUG_ERROR) {
			Log.e(strModule, "----" + strErrMsg + "----", e);
			if (SAVE_TO_SD) {
				storeLog(strModule, strErrMsg);
			}
		}
	}

    /**
     * 用于打印描述级的日志信息
     * @param strModule LOG TAG
     * @param strErrMsg 打印信息
     */
	public static void d(String strModule, String strErrMsg) {
		if (DEBUG) {
			Log.d(strModule, "----" + strErrMsg + "----");
            if (SAVE_TO_SD) {
                storeLog(strModule, strErrMsg);
            }
		}
	}
	
	/**
	 * 用于打印info级的日志信息
	 * @param strModule LOG TAG
	 * @param strErrMsg 打印信息
	 */
	public static void i(String strModule, String strErrMsg) {
		if (DEBUG) {
			Log.i(strModule, strErrMsg);
			if (SAVE_TO_SD) {
				storeLog(strModule, strErrMsg);
			}
		}
	}
	
	/**
	 * 用于打印v级的日志信息
	 * @param strModule LOG TAG
	 * @param strErrMsg 打印信息
	 */
	public static void v(String strModule, String strErrMsg) {
		if (DEBUG) {
			Log.v(strModule, "----" + strErrMsg + "----");
			if (SAVE_TO_SD) {
				storeLog(strModule, strErrMsg);
			}
		}
	}
	
	/**
	 * 用于打印w级的日志信息
	 * @param strModule LOG TAG
	 * @param strErrMsg 打印信息
	 */
	public static void w(String strModule, String strErrMsg) {
		if (DEBUG) {
			Log.w(strModule, "----" + strErrMsg + "----");
			if (SAVE_TO_SD) {
				storeLog(strModule, strErrMsg);
			}
		}
	}

    /**
     * 将日志信息保存至SD卡
     * @param strModule LOG TAG
     * @param strErrMsg 保存的打印信息
     */
	public static void storeLog(String strModule, String strErrMsg) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			synchronized (LOG_FMT) {
				String logname = LOG_FMT.format(new Date(System.currentTimeMillis())) + ".txt";
				
				File file = new File(getLogDirPath() + "/" + logname);
				// 判断日志文件是否已经存在
				if (!file.exists()) {
					try {
						file.createNewFile();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					// 输出
					FileOutputStream fos = new FileOutputStream(file, true);
					PrintWriter out = new PrintWriter(fos);
					
					out.println(fmt.format(System.currentTimeMillis()) + "  >>" + strModule + "<<  " + strErrMsg + '\r');
					out.flush();
					out.close();
					
				}
				catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 获取DEBUG状态
	 * @return
	 */
	public static boolean isDebuggable() {
	    return DEBUG;
	}
	
	/**
	 * 用于打印wr级的日志信息
	 * @param strModule LOG TAG
	 * @param strErrMsg 打印信息
	 */
	public static void wr(String strModule, String strErrMsg) {
		if (DEBUG) {
			Log.w(strModule, "----" + strErrMsg + "----");
			storeLog(strModule, strErrMsg);
		}
	}
}
