package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Star implements Parcelable{

	//starClass	1星：“1”，2星：“2”...5星：“5”
	private String starClass;
	//starCount	获得星级的数量
	private int starCount;

	public Star() {
		super();
		this.starClass = "0";
	}

	public String getStarClass() {
		return starClass;
	}

	public int getStarCount() {
		return starCount;
	}

	@Override
	public String toString() {
		return "Star [starClass=" + starClass + ", starCount=" + starCount + "]";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.starClass);
		dest.writeInt(this.starCount);
	}

	protected Star(Parcel in) {
		this.starClass = in.readString();
		this.starCount = in.readInt();
	}

	public static final Creator<Star> CREATOR = new Creator<Star>() {
		public Star createFromParcel(Parcel source) {
			return new Star(source);
		}

		public Star[] newArray(int size) {
			return new Star[size];
		}
	};
}