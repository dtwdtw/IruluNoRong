package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StarInfo implements Parcelable{

	//	total	评论总数
	private int total;
	//	ave	 获得的星星平均数
	private String ave;
	//	tarList	每种星星的情况
	private List<Star> starList;

	public int getTotal() {
		return total;
	}

	public String getAve() {
		return ave;
	}

	public List<Star> getStarList() {
		return starList;
	}

	@Override
	public String toString() {
		return "StarInfo [total=" + total + ", ave=" + ave + ", starList=" + starList + "]";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.total);
		dest.writeString(this.ave);
		dest.writeTypedList(starList);
	}

	public StarInfo() {
	}

	protected StarInfo(Parcel in) {
		this.total = in.readInt();
		this.ave = in.readString();
		this.starList = in.createTypedArrayList(Star.CREATOR);
	}

	public static final Creator<StarInfo> CREATOR = new Creator<StarInfo>() {
		public StarInfo createFromParcel(Parcel source) {
			return new StarInfo(source);
		}

		public StarInfo[] newArray(int size) {
			return new StarInfo[size];
		}
	};
}