package com.wf.irulu.common.utils;

import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * @描述: HTTP帮助类
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:HttpUtil
 * @作者: 左西杰
 * @创建时间:2015-5-28 上午9:31:54
 * 
 */

public class HttpUtil {

	private static final String TAG = HttpUtil.class.getCanonicalName();
	/** 超时时间*/
	public static final int TIME_OUT = 30 * 1000;
	/** 读取缓存*/
	public static final int BYTE_BUF = 1024;
	/** 默认编码：UTF-8*/
	public static final String ENCODE_DEFAULT = "UTF-8";
	/** 图片资源临时文件夹 */
	public static final String IMAGE_TEMP_FOLDER = ".tmp";

//	/**
//	 * HTTP-POST请求
//	 *
//	 * @param headerMap
//	 * @param data
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	public static String postMethodRequest(Map<String, String> headerMap, NameValuePair[] data, String url) {
//
//		String response = null;
//		PostMethod postMethod = null;
//		boolean isOffLine = false;
//
//		try {
//			// 构造HttpClient的实例
//			HttpClient httpClient = new HttpClient();
//
//			if (url.startsWith("https")) {
//				Protocol netHttps = new Protocol("https", new NetSSLProtocolSocketFactory(), 443);
//				Protocol.registerProtocol("https", netHttps);
//			}
//
//			HttpConnectionManagerParams params = httpClient.getHttpConnectionManager().getParams();
//			// 设置超时时间
//			params.setConnectionTimeout(TIME_OUT);
//			postMethod = new PostMethod(url);
//			postMethod.getParams().setContentCharset(ENCODE_DEFAULT);
//			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, getUserAgent());
//
//			if (null != headerMap) {
//				Set<Entry<String, String>> set = headerMap.entrySet();
//				if (null != set && !set.isEmpty()) {
//					for (Entry<String, String> entry : set) {
//
//						postMethod.setRequestHeader(entry.getKey(), entry.getValue());
//						ILog.e("--HeadMap--",entry.getKey() + "  " + entry.getValue());
//					}
//				}
//			}
//			if (null != data && data.length != 0) {
//				for (int i = 0; i < data.length; i++) {
//					Log.e(TAG, "http--data:" + data[i]);
//					data[i].setValue(data[i].getValue());
////					data[i].setValue(URLEncoder.encode(data[i].getValue(),ENCODE_DEFAULT));
//				}
//
//				// 填入各个表单域的值
//				// 将表单的值放入postMethod中
//				postMethod.setRequestBody(data);
//			}
//
//			httpClient.executeMethod(postMethod);
//
//			// 读取内容
//			response = new String(postMethod.getResponseBody(), "UTF-8");
//
//			Header[] headers = postMethod.getResponseHeaders();
//
//			ILog.e(TAG, "http--url:" + url);
//
//			if (!StringUtils.isEmpty(response)) {
//				JSONObject object = new JSONObject(response);
//				if (object.has("code")) {
//					int code = object.getInt("code");
//					if (code == 10107) {
//						//请重新登陆
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			// 没网
//			if (!CommonUtil.isNetworkAvailable()) {
//				IruluController.getInstance().showNoNetworkToast();
//			}
//		} finally {
//			if (null != postMethod) {
//				postMethod.releaseConnection();
//			}
//		}
//
//		// 服务器维护
//		if (isOffLine && !StringUtils.isEmpty(response)) {
//			try {
//				JSONObject jsonObj = new JSONObject(response);
//				if (jsonObj.has("code") && jsonObj.getInt("code") == ErrorCodeUtils.OFF_LINE) {
//					String msg = jsonObj.getString("msg");
//
//				}
//
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		ILog.e(TAG, response);
//
//		return response;
//
//	}
//
//	/**
//	 * HTTP-GET请求
//	 *
//	 * @param headerMap
//	 * @param data
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	public static String getMethodRequest(Map<String, String> headerMap, NameValuePair[] data, String url) {
//
//		String response = null;
//		GetMethod getMethod = null;
//		boolean isOffLine = false;
//
//		try {
//			// 构造HttpClient的实例
//			HttpClient httpClient = new HttpClient();
//			if (url.startsWith("https")) {
//				Protocol netHttps = new Protocol("https", new NetSSLProtocolSocketFactory(), 443);
//				Protocol.registerProtocol("https", netHttps);
//			}
//			HttpConnectionManagerParams params = httpClient.getHttpConnectionManager().getParams();
//			// 设置超时时间
//			params.setConnectionTimeout(TIME_OUT);
//			if (null != data && data.length != 0) {
//				StringBuilder buf = new StringBuilder();
//				for (int i = 0; i < data.length; i++) {
//					if (i == 0) {
//						buf.append("?");
//					} else {
//						buf.append("&");
//					}
//					buf.append(data[i].getName()).append("=").append(URLEncoder.encode(data[i].getValue(), ENCODE_DEFAULT));
//				}
//				url = url + buf.toString();
//			}
//
//			getMethod = new GetMethod(url);
//			getMethod.getParams().setContentCharset(ENCODE_DEFAULT);
//			getMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, getUserAgent());
//
//			if (null != headerMap) {
//				Set<Entry<String, String>> set = headerMap.entrySet();
//				if (null != set && !set.isEmpty()) {
//					for (Entry<String, String> entry : set) {
//						getMethod.setRequestHeader(entry.getKey(), entry.getValue());
//						ILog.e("--HeadMap--", entry.getKey() + "  " + entry.getValue());
//					}
//				}
//			}
//
//			httpClient.executeMethod(getMethod);
//
//			// 读取内容
//			response = new String(getMethod.getResponseBody(), "UTF-8");
//
//			Header[] headers = getMethod.getResponseHeaders();
//
//			ILog.e(TAG, "http--url:" + url);
//
//			if (!StringUtils.isEmpty(response)) {
//				JSONObject object = new JSONObject(response);
//				if (object.has("code")) {
//					int code = object.getInt("code");
//					if (code == 10107) {
//
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			// 没网
//			if (!CommonUtil.isNetworkAvailable()) {
//				IruluController.getInstance().showNoNetworkToast();
//			}
//		} finally {
//			if (null != getMethod) {
//				getMethod.releaseConnection();
//			}
//		}
//
//		// 服务器维护
//		if (isOffLine && !StringUtils.isEmpty(response)) {
//			try {
//				JSONObject jsonObj = new JSONObject(response);
//				if (jsonObj.has("code") && jsonObj.getInt("code") == ErrorCodeUtils.OFF_LINE) {
//					String msg = jsonObj.getString("msg");
////					IruluController.getInstance().signInElsewhere("服务器维护", msg);
//				}
//
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		ILog.e(TAG, response);
//		return response;
//	}

//	/**
//	 * 下载文件
//	 *
//	 * @param url 下载的url
//	 * @param fileDir 文件目录--此处需要用到临时目录
//	 * @param fileName 文件名
//	 * @return
//	 */
//	public static boolean uploadImg(String url, String fileDir, String fileName) {
//		OutputStream os = null;
//		InputStream is = null;
//		GetMethod getMethod = null;
//		try {
//
//			// 构造HttpClient的实例
//			HttpClient httpClient = new HttpClient();
//
//			// 设置超时时间
//			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIME_OUT);
//			getMethod = new GetMethod(url);
//
//			httpClient.executeMethod(getMethod);
//
//			int length = getMethod.getResponseBody().length;
//			is = getMethod.getResponseBodyAsStream();
//
//			if (length != -1) {
//
//				FileUtils.createDirs(fileDir);
//
//				// 由于文件读写操作需要区分，这里先把文件下载到一个临时文件夹中，等下载好在移动至目标文件夹，读取的线程既可以找到下载好的文件
//				String tempDir = fileDir + File.separator + IMAGE_TEMP_FOLDER;
//				FileUtils.createDirs(tempDir);
//
//				String tempPath = tempDir + File.separator + fileName;
//
//				ILog.e(TAG, "uploadImg:" + fileDir);
//				ILog.e(TAG, "uploadImg:" + tempDir);
//				ILog.e(TAG, "uploadImg:" + tempPath);
//
//				os = new FileOutputStream(tempPath);
//				byte[] buffer = new byte[1024];
//				int readLen = 0;
//				while ((readLen = is.read(buffer)) > 0) {
//					os.write(buffer, 0, readLen);
//				}
//				os.flush();
//				File file = new File(tempPath);
//				file.renameTo(new File(fileDir + "/" + fileName));
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			ILog.e(TAG, "uploadImg MalformedURLException: ", e);
//			return false;
//		} catch (IOException e) {
//			e.printStackTrace();
//			ILog.e(TAG, "uploadImg IOException: ", e);
//			return false;
//		} finally {
//			try {
//				if (os != null) {
//					os.close();
//				}
//				if (null != is) {
//					is.close();
//				}
//				if (null != getMethod) {
//					getMethod.releaseConnection();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//				ILog.e(TAG, "uploadImg IOException: ", e);
//			}
//		}
//		return true;
//	}

	private static String getUserAgent() {
		StringBuffer buf = new StringBuffer();
		buf.append("iRulu/").append(IruluApplication.getInstance().getString(R.string.version_name));
		buf.append("(");
		buf.append(android.os.Build.MODEL + ";" + "Android " + android.os.Build.VERSION.RELEASE);
		buf.append(") ");
		buf.append(HttpConstantUtils.getLocation());
		buf.append(" NetType/");
		buf.append(ConstantsUtils.NETWORK_TYPE);
		return buf.toString();
	}

	/**
	 * 设置携带参数和信息头的get请求对象
	 * @param url			地址
	 * @param headers		信息头
	 * @param data			参数信息
	 */
	public static Request getRequest(String url, Headers headers,Map<String,String> data) {
		if (null != data && data.size() != 0) {
			StringBuilder buf = new StringBuilder();

			if (null != data) {
				Set<Entry<String, String>> set = data.entrySet();
				if (null != set && !set.isEmpty()) {
					int i = 0;
					for (Entry<String, String> entry : set) {
						if (i == 0) {
							buf.append("?");
							i++;
						} else {
							buf.append("&");
						}
						try {
							buf.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), ENCODE_DEFAULT));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
			url = url + buf.toString();
		}

		Request.Builder builder = new Request.Builder();
		builder.url(url);
		builder = builder.headers(headers);
		ILog.d("TB","hello==>"+headers);
		ILog.e("hellolove",url);
		return builder.build();
	}

	/**
	 * 设置携带参数和信息头的post请求对象
	 * @param url		地址
	 * @param headers	信息头
	 * @param body		参数信息
	 */
	public static Request postRequest(String url, Headers headers, RequestBody body){
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		if (null != headers) {
			builder = builder.headers(headers);
		}

		return builder.post(body).build();
	}
}
