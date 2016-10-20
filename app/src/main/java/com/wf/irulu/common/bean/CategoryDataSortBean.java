package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategoryDataSortBean implements Parcelable{
    private String name; //分类的名字
    private int sort_type;//价格的升降序
    private String sort; //按照某种类型排序

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort_type() {
        return sort_type;
    }

    public void setSort_type(int sort_type) {
        this.sort_type = sort_type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.sort_type);
        dest.writeString(this.sort);
    }

    public CategoryDataSortBean() {
    }

    protected CategoryDataSortBean(Parcel in) {
        this.name = in.readString();
        this.sort_type = in.readInt();
        this.sort = in.readString();
    }

    public static final Creator<CategoryDataSortBean> CREATOR = new Creator<CategoryDataSortBean>() {
        public CategoryDataSortBean createFromParcel(Parcel source) {
            return new CategoryDataSortBean(source);
        }

        public CategoryDataSortBean[] newArray(int size) {
            return new CategoryDataSortBean[size];
        }
    };
}
