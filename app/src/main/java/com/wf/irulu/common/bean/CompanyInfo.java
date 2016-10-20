package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 物流公司描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CompanyInfo
 * @作者: 左杰
 * @创建时间:2015/11/16 15:38
 */
public class CompanyInfo implements Parcelable {

    // name string 物流公司名
    private String name;
    // icon string 物流公司icon
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.icon);
    }

    public CompanyInfo() {
    }

    protected CompanyInfo(Parcel in) {
        this.name = in.readString();
        this.icon = in.readString();
    }

    public static final Creator<CompanyInfo> CREATOR = new Creator<CompanyInfo>() {
        public CompanyInfo createFromParcel(Parcel source) {
            return new CompanyInfo(source);
        }

        public CompanyInfo[] newArray(int size) {
            return new CompanyInfo[size];
        }
    };
}
