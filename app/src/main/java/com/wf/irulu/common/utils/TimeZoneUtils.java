package com.wf.irulu.common.utils;

import java.text.DateFormat;
import java.util.TimeZone;

/**
 * 
 * @描述: 时区转换问题
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:TimeZoneUtil
 * @作者: 左西杰
 * @创建时间:2015-6-4 下午2:03:11
 *
 */
public class TimeZoneUtils {
	/**
	 * 0区
	 */
	public static String Zone0 = "GMT+0";
	/**
	 * 东八区
	 */
	public static String Zone8 = "GMT+8";

	public static TimeZone localTimeZone = null;
	public static TimeZone GMT0_TIME_ZONE = TimeZone.getTimeZone(Zone0);
	public static TimeZone GMT8_TIME_ZONE = TimeZone.getTimeZone(Zone8);
	public static DateFormat dateFormat = null;

	public static DateFormat dateFormatTo = null;

	public static TimeZone getLocalTimeZone() {
		if (localTimeZone == null) {
			localTimeZone = TimeZone.getDefault();
		}
		return localTimeZone;
	}
	
}
