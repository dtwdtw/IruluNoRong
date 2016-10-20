package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 规格参数
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:Standard
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class Standard implements Parcelable {

    /** 规格名称*/
    private String name;
    /** 规格内容*/
    private String content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.content);
    }

    public Standard() {
    }

    protected Standard(Parcel in) {
        this.name = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Standard> CREATOR = new Parcelable.Creator<Standard>() {
        public Standard createFromParcel(Parcel source) {
            return new Standard(source);
        }

        public Standard[] newArray(int size) {
            return new Standard[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
