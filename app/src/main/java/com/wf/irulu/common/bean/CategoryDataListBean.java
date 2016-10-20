package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategoryDataListBean implements Parcelable{
    private int cateId;
    private String name;
    private ArrayList<CategoryProductListBean> list;

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CategoryProductListBean> getList() {
        return list;
    }

    public void setList(ArrayList<CategoryProductListBean> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cateId);
        dest.writeString(this.name);
        dest.writeTypedList(list);
    }

    public CategoryDataListBean() {
    }

    protected CategoryDataListBean(Parcel in) {
        this.cateId = in.readInt();
        this.name = in.readString();
        this.list = in.createTypedArrayList(CategoryProductListBean.CREATOR);
    }

    public static final Creator<CategoryDataListBean> CREATOR = new Creator<CategoryDataListBean>() {
        public CategoryDataListBean createFromParcel(Parcel source) {
            return new CategoryDataListBean(source);
        }

        public CategoryDataListBean[] newArray(int size) {
            return new CategoryDataListBean[size];
        }
    };
}
