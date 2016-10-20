package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:HomeList
 * @作者: 左杰
 * @创建时间:2015/11/7 20:59
 */
public class HomeList implements Parcelable,Serializable {

    private ArrayList<HomeProduct> list;

    public ArrayList<HomeProduct> getList() {
        return list;
    }

    public void setList(ArrayList<HomeProduct> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }

    public HomeList() {
    }

    protected HomeList(Parcel in) {
        this.list = in.createTypedArrayList(HomeProduct.CREATOR);
    }

    public static final Creator<HomeList> CREATOR = new Creator<HomeList>() {
        public HomeList createFromParcel(Parcel source) {
            return new HomeList(source);
        }

        public HomeList[] newArray(int size) {
            return new HomeList[size];
        }
    };
}
