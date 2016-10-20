package com.wf.irulu.common.bean;

import java.io.Serializable;

/**
 * @描述: TODO
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:PhotoDirInfo
 * @作者: Yuki
 * @创建时间:2015-9-7 下午6:49:58
 * 
 */
public class PhotoDirInfo implements Serializable{
	
	private String dirId;
	private String dirName;
	private String firstPicPath;
	private boolean isUserOtherPicSoft;
	private int picCount = 0;

	public PhotoDirInfo() {
	}

	public PhotoDirInfo(String paramString, boolean paramBoolean) {
		this.dirName = paramString;
		this.isUserOtherPicSoft = paramBoolean;
	}

	public String getDirId() {
		return this.dirId;
	}

	public String getDirName() {
		return this.dirName;
	}

	public String getFirstPicPath() {
		return this.firstPicPath;
	}

	public int getPicCount() {
		return this.picCount;
	}

	public boolean isUserOtherPicSoft() {
		return this.isUserOtherPicSoft;
	}

	public void setDirId(String paramString) {
		this.dirId = paramString;
	}

	public void setDirName(String paramString) {
		this.dirName = paramString;
	}

	public void setFirstPicPath(String paramString) {
		this.firstPicPath = paramString;
	}

	public void setPicCount(int paramInt) {
		this.picCount = paramInt;
	}

	public void setUserOtherPicSoft(boolean paramBoolean) {
		this.isUserOtherPicSoft = paramBoolean;
	}
}
