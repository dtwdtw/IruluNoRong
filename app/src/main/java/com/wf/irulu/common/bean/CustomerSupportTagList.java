package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 顾客支持原因列表
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CustomerSupportTagList
 * @作者: 左杰
 * @创建时间:2015/11/20 13:29
 */
public class CustomerSupportTagList implements Parcelable {

    private String id;
    private String question;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.question);
    }

    public CustomerSupportTagList() {
    }

    protected CustomerSupportTagList(Parcel in) {
        this.id = in.readString();
        this.question = in.readString();
    }

    public static final Creator<CustomerSupportTagList> CREATOR = new Creator<CustomerSupportTagList>() {
        public CustomerSupportTagList createFromParcel(Parcel source) {
            return new CustomerSupportTagList(source);
        }

        public CustomerSupportTagList[] newArray(int size) {
            return new CustomerSupportTagList[size];
        }
    };
}
