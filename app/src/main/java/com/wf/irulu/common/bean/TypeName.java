package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: SKU列表
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:TypeName
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class TypeName implements Parcelable {

    /** SKU名称 */
    private String name;
    /** SKU值 */
    private String[] list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringArray(this.list);
    }

    public TypeName() {
    }

    protected TypeName(Parcel in) {
        this.name = in.readString();
        this.list = in.createStringArray();
    }

    public static final Parcelable.Creator<TypeName> CREATOR = new Parcelable.Creator<TypeName>() {
        public TypeName createFromParcel(Parcel source) {
            return new TypeName(source);
        }

        public TypeName[] newArray(int size) {
            return new TypeName[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }
}
