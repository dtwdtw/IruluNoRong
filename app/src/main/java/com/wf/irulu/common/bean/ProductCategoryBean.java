package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/17.
 */
public class ProductCategoryBean implements Parcelable {
    private ArrayList<ProductCategoryInformationBean> list;

    public ArrayList<ProductCategoryInformationBean> getList() {
        return list;
    }

    public void setList(ArrayList<ProductCategoryInformationBean> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

}
