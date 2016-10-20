package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daniel on 2015/11/17.
 */
public class ProductCategoryInformationBean implements Parcelable{
    private int id;
    private String name;
    private String icon;
    private int count;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.count);
    }

    public ProductCategoryInformationBean() {
    }

    protected ProductCategoryInformationBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<ProductCategoryInformationBean> CREATOR = new Creator<ProductCategoryInformationBean>() {
        public ProductCategoryInformationBean createFromParcel(Parcel source) {
            return new ProductCategoryInformationBean(source);
        }

        public ProductCategoryInformationBean[] newArray(int size) {
            return new ProductCategoryInformationBean[size];
        }
    };
}
