package com.wf.irulu.common.bean;

import java.io.Serializable;

/**
 * @描述: 网络接口请求的公共类--包含"code":0
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:CommonServiceBean
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午6:58:38
 * 
 */

public class CommonServiceBean implements Serializable {

	/** 服务器返回结果码*/
	public int code;
	/** 返回数据*/
	public Object data;
	/** 错误码消息描述*/
	public String msg;

	@Override
	public String toString() {
		return "CommonServiceBean [code=" + code + ", data=" + data + ", msg=" + msg + "]";
	}
}
