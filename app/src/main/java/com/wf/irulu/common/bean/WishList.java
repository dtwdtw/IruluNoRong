package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: WishList的结合Bean
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:WishList
 * @作者: 左杰
 * @创建时间:2015/11/26 11:20
 */
public class WishList implements Parcelable {

    private ArrayList<WishList> list;

    public ArrayList<WishList> getList() {
        return list;
    }

    public void setList(ArrayList<WishList> list) {
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

    public WishList() {
    }

    protected WishList(Parcel in) {
        this.list = in.createTypedArrayList(WishList.CREATOR);
    }

    public static final Creator<WishList> CREATOR = new Creator<WishList>() {
        public WishList createFromParcel(Parcel source) {
            return new WishList(source);
        }

        public WishList[] newArray(int size) {
            return new WishList[size];
        }
    };
}
