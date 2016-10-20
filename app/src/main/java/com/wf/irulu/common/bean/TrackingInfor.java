package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daniel on 2015/12/4.
 */
public class TrackingInfor implements Parcelable {
    private String itemValue;
    private String time;

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemValue);
        dest.writeString(this.time);
    }

    public TrackingInfor() {
    }

    protected TrackingInfor(Parcel in) {
        this.itemValue = in.readString();
        this.time = in.readString();
    }

    public static final Creator<TrackingInfor> CREATOR = new Creator<TrackingInfor>() {
        public TrackingInfor createFromParcel(Parcel source) {
            return new TrackingInfor(source);
        }

        public TrackingInfor[] newArray(int size) {
            return new TrackingInfor[size];
        }
    };
}
