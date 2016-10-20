package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @描述: 心愿单描述
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:WishListInfo
 * @作者: Yuki
 * @创建时间:2015-7-14 上午10:37:42
 *
 */
public class WishInfo implements Parcelable {

//	id	int
	private int id;
//	mid	int
	private int mid;
//	pid	int
	private int pid;
//	skuId	int
	private int skuId;
//	oriPrice	float
	private float oriPrice;
//	currPrice	float
	private float currPrice;
//	name	str
	private String name;
//	image	str
	private String image;
//	star	float
	private float star;
//	commentNum	int
	private int commentNum;

	private String createTime;

	private int status;
	
	public WishInfo() {
		super();
		this.name = "";
		this.image = "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WishInfo wishInfo = (WishInfo) o;

		return pid == wishInfo.pid;

	}

	public WishInfo(int pid) {
		this.pid = pid;
	}

	@Override
	public int hashCode() {
		return pid;
	}

	@Override
	public String toString() {
		return "WishInfo{" +
				"commentNum=" + commentNum +
				", id=" + id +
				", mid=" + mid +
				", pid=" + pid +
				", skuId=" + skuId +
				", oriPrice=" + oriPrice +
				", currPrice=" + currPrice +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				", star=" + star +
				", createTime='" + createTime + '\'' +
				'}';
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public float getCurrPrice() {
		return currPrice;
	}

	public void setCurrPrice(float currPrice) {
		this.currPrice = currPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(float oriPrice) {
		this.oriPrice = oriPrice;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getSkuId() {
		return skuId;
	}

	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.mid);
		dest.writeInt(this.pid);
		dest.writeInt(this.skuId);
		dest.writeFloat(this.oriPrice);
		dest.writeFloat(this.currPrice);
		dest.writeString(this.name);
		dest.writeString(this.image);
		dest.writeFloat(this.star);
		dest.writeInt(this.commentNum);
		dest.writeString(this.createTime);
		dest.writeInt(this.status);
	}

	protected WishInfo(Parcel in) {
		this.id = in.readInt();
		this.mid = in.readInt();
		this.pid = in.readInt();
		this.skuId = in.readInt();
		this.oriPrice = in.readFloat();
		this.currPrice = in.readFloat();
		this.name = in.readString();
		this.image = in.readString();
		this.star = in.readFloat();
		this.commentNum = in.readInt();
		this.createTime = in.readString();
		this.status = in.readInt();
	}

	public static final Creator<WishInfo> CREATOR = new Creator<WishInfo>() {
		public WishInfo createFromParcel(Parcel source) {
			return new WishInfo(source);
		}

		public WishInfo[] newArray(int size) {
			return new WishInfo[size];
		}
	};
}
