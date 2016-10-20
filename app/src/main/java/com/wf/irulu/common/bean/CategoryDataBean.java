package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategoryDataBean implements Parcelable{
    private int total;
    private String name;
    private ArrayList<CategoryDataListBean> list;
    private ArrayList<CategoryDataSortBean> sort;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CategoryDataListBean> getList() {
        return list;
    }

    public void setList(ArrayList<CategoryDataListBean> list) {
        this.list = list;
    }

    public ArrayList<CategoryDataSortBean> getSort() {
        return sort;
    }

    public void setSort(ArrayList<CategoryDataSortBean> sort) {
        this.sort = sort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeString(this.name);
        dest.writeTypedList(list);
        dest.writeList(this.sort);
    }

    public CategoryDataBean() {
    }

    protected CategoryDataBean(Parcel in) {
        this.total = in.readInt();
        this.name = in.readString();
        this.list = in.createTypedArrayList(CategoryDataListBean.CREATOR);
        this.sort = new ArrayList<CategoryDataSortBean>();
        in.readList(this.sort, List.class.getClassLoader());
    }

    public static final Creator<CategoryDataBean> CREATOR = new Creator<CategoryDataBean>() {
        public CategoryDataBean createFromParcel(Parcel source) {
            return new CategoryDataBean(source);
        }

        public CategoryDataBean[] newArray(int size) {
            return new CategoryDataBean[size];
        }
    };
}
