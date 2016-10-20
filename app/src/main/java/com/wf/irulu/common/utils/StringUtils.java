package com.wf.irulu.common.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.wf.irulu.IruluApplication;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public final static String UTF_8 = "UTF-8";

	/** 手机号 */
    public final static String PHONE_PATTERN = "^[1][3-8]\\d{9}$";
    public final static String NUMBER_PATTERN = "^[0-9]\\d{10}$";
	/** 图片格式选择和渐加载模式*/
	private final static String IMAGE_FORMAT = "/format/jpg/interlace/1";
	/** 全屏看图测略*/
	private static final String FULL_IMAGE_URL = "?imageView2/4/w/";
	/** 缩略图策略*/
	private static final String THUMBNAIL_URL = "?imageView2/5/w/";



	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 返回一个高亮spannable
	 * 
	 * @param content
	 *            文本内容
	 * @param color
	 *            高亮颜色
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color,
			int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 获取链接样式的字符串，即字符串下面有下划线
	 * 
	 * @param resId
	 *            文字资源
	 * @return 返回链接样式的字符串
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId))
				.append(" </b></u></a>");
		return Html.fromHtml(sb.toString());
	}

	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024
						/ 1024 / (float) 100))
						+ "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100)
						+ "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024
						/ 1024 / (float) 10))
						+ "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10)
						+ "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100)
					+ "GB";
		}
		return size;
	}
	/** 获取访问网络的URL */
	public static String getUrlPath(int id){
		return UIUtils.getContext().getResources().getString(id);
	}
	
	/**
     * <br>正则表达式匹配 </br> 
     * @param patternStr
     * @param input
     * @return
     */
    public static boolean isMatcherFinded(String patternStr, CharSequence input) {
    	if (!isEmpty(patternStr) && null != input) {
    		Pattern pattern = Pattern.compile(patternStr);
    		Matcher matcher = pattern.matcher(input);
    		if (null != matcher && matcher.find()) {
    			return true;
    		}
		}
    	
		return false;
    }
    
    /**
     * 格式化小数点后两位
     * @param price
     * @return
     */
    public synchronized static String getPriceByFormat(Object price){
    	NumberFormat format = NumberFormat.getInstance();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		String num = format.format(price);
		String priceSymbol = CacheUtils.getString(IruluApplication.getInstance(), "priceSymbol","US $");
		return priceSymbol+num;
    }

	/**
	 * 邮箱匹配规则
	 *
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 全屏图地址
	 * @param pUrl
	 * @return
	 */
	public static String getFullImageUrlFormat(String pUrl){
		StringBuilder builder = new StringBuilder();
		builder.append(pUrl).append(FULL_IMAGE_URL).append(String.valueOf(ConstantsUtils.DISPLAYW)).append("/h/").append(String.valueOf(ConstantsUtils.DISPLAYW)).append(IMAGE_FORMAT);
		return builder.toString();
	}

	/**
	 * 缩略图地址
	 * @param pUrl
	 * @return
	 */
	public static String getThumbnailImageUrlFormat(String pUrl,int pWidth){
		if (TextUtils.isEmpty(pUrl)){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(pUrl).append(THUMBNAIL_URL).append(String.valueOf(pWidth)).append("/h/").append(String.valueOf(pWidth)).append(IMAGE_FORMAT);
		return builder.toString();
	}

	/**
	 * 格式化获取的时间
	 * @param time 时间戳,单位为秒
	 * @return
	 */
	public static String getTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long timestamp = Long.parseLong(time)*1000;//因为获取到的时间是秒单位的，我们这里是毫秒的，所以得乘以1000
		return format.format(timestamp);
	}

	/**
	 * 格式化获取的时间
	 * @param time 时间戳,单位为毫秒
	 * @return
	 */
	public static String getFormatTime(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long timestamp = Long.parseLong(time);
		return format.format(timestamp);
	}
}
