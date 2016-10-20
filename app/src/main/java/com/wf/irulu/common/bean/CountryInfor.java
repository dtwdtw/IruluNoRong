package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/11.
 */
public class CountryInfor implements Parcelable {
    private ArrayList<CountryInforBean> list;
    private int total;

    public ArrayList<CountryInforBean> getList() {
        return list;
    }

    public void setList(ArrayList<CountryInforBean> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
        dest.writeInt(this.total);
    }

    public CountryInfor() {
    }

    protected CountryInfor(Parcel in) {
        this.list = in.createTypedArrayList(CountryInforBean.CREATOR);
        this.total = in.readInt();
    }

    public static final Creator<CountryInfor> CREATOR = new Creator<CountryInfor>() {
        public CountryInfor createFromParcel(Parcel source) {
            return new CountryInfor(source);
        }

        public CountryInfor[] newArray(int size) {
            return new CountryInfor[size];
        }
    };
}
