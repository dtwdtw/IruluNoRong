package com.wf.irulu.common.bean;

import java.io.Serializable;

/**
 * @描述: 消息的信息描述
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:MessageBean
 * @作者: 左西杰
 * @创建时间:2015-8-5 上午9:48:15
 * 
 */
public class MessageBean implements Serializable {

	/** 指定是哪种类型*/
	private int type;
	/** 消息内容*/
	private String content;
	/** 消息日期*/
	private long time;
	
	public MessageBean() {
		super();
	}
	
	public MessageBean(int type, String content, long time) {
		super();
		this.type = type;
		this.content = content;
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
