package com.wf.irulu.common.utils;

/**
 * @描述: 定义所有的服务器返回的错误码信息
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:ErrorCodeUtils
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午6:59:43
 * 
 */

public class ErrorCodeUtils {

	public static final int CODE_SUCCESS = 0; 							// 成功
	public static final int CODE_FAIL = -1;								// 失败

	public static final int OFF_LINE = 10000;							// 服务器维护
	public static final int ERROR_PARSER = -600;						// JSON解析错误

	public static final int ERROR_NO_RESPONSE = -601;					// 服务器无响应
	public static final int NO_NET_RESPONSE = -602;					// 服务器无响应

}
