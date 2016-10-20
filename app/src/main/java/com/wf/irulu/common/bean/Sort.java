package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Sort implements Parcelable {

    //name	string		显示名称
    private String name;
    //word	string		GET传入参数
    private String word;
    //sort	int	0	排序方式 1：正序、-1：倒序、0：没排序
    private int sort;

    public Sort() {
        super();
        this.name = "";
        this.word = "";
    }

    public String getName() {
        return name;
    }

    public String getWord() {
        return word;
    }

    public int getSort() {
        return sort;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.word);
        dest.writeInt(this.sort);
    }

    protected Sort(Parcel in) {
        this.name = in.readString();
        this.word = in.readString();
        this.sort = in.readInt();
    }

    public static final Parcelable.Creator<Sort> CREATOR = new Parcelable.Creator<Sort>() {
        public Sort createFromParcel(Parcel source) {
            return new Sort(source);
        }

        public Sort[] newArray(int size) {
            return new Sort[size];
        }
    };
}