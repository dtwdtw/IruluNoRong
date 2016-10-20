package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CustomerReply
 * @作者: Yuki
 * @创建时间:2015/11/20
 */
public class CustomerReply implements Parcelable {

//    content	string		回复内容（iRULU Respective）
    private String content;
//    time	int		回复时间
    private int time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeInt(this.time);
    }

    public CustomerReply() {
    }

    protected CustomerReply(Parcel in) {
        this.content = in.readString();
        this.time = in.readInt();
    }

    public static final Parcelable.Creator<CustomerReply> CREATOR = new Parcelable.Creator<CustomerReply>() {
        public CustomerReply createFromParcel(Parcel source) {
            return new CustomerReply(source);
        }

        public CustomerReply[] newArray(int size) {
            return new CustomerReply[size];
        }
    };
}
