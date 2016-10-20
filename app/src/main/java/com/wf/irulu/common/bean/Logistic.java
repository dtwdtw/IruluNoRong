package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 物流详情描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:Logistic
 * @作者: 左杰
 * @创建时间:2015/11/16 15:39
 */
public class Logistic  implements Parcelable {

    //		itemValue	string		物流单条记录
    private String itemValue;
    //		time	string		添加时间
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

    public Logistic() {
    }

    protected Logistic(Parcel in) {
        this.itemValue = in.readString();
        this.time = in.readString();
    }

    public static final Creator<Logistic> CREATOR = new Creator<Logistic>() {
        public Logistic createFromParcel(Parcel source) {
            return new Logistic(source);
        }

        public Logistic[] newArray(int size) {
            return new Logistic[size];
        }
    };
}
