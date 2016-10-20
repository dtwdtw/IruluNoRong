package com.wf.irulu.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @描述: MD5加密
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:MD5Util
 * @作者: 左西杰
 * @创建时间:2015-5-29 下午2:49:32
 *
 */
public class MD5Util {
	/**
	 * 使用md5的算法进行加密
	 * 
	 * @param plainText
	 *            加密原文
	 * @return 加密密文
	 */
	public static String md5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(
					plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);
		
		//如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}
	/**
	 * 通过文件来获取MD5值
	 * @param file
	 *     文件
	 * @return
	 *     文件的MD5值
	 */
	public static String getFileMd5(File file){
		StringBuilder res = new StringBuilder("");
		try {
			MessageDigest instance = MessageDigest.getInstance("md5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			//时间换空间
			int len = fis.read(buffer);
			while (len != -1){
				instance.update(buffer,0,len);
				len = fis.read(buffer);
			}
			byte[] digest = instance.digest();
			for (byte b:digest){
				String hexString = Integer.toHexString(b & 0xff);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				res.append(hexString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res + "";
	}
}