package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 顾客支持原因
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ContactUsTag
 * @作者: 左杰
 * @创建时间:2015/11/20 12:58
 */
public class ContactUsTag implements Parcelable {

    private ArrayList<CustomerSupportTagList> list;
    private int total;

    public ArrayList<CustomerSupportTagList> getList() {
        return list;
    }

    public void setList(ArrayList<CustomerSupportTagList> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
        dest.writeInt(this.total);
    }

    public ContactUsTag() {
    }

    protected ContactUsTag(Parcel in) {
        this.list = in.createTypedArrayList(CustomerSupportTagList.CREATOR);
        this.total = in.readInt();
    }

    public static final Creator<ContactUsTag> CREATOR = new Creator<ContactUsTag>() {
        public ContactUsTag createFromParcel(Parcel source) {
            return new ContactUsTag(source);
        }

        public ContactUsTag[] newArray(int size) {
            return new ContactUsTag[size];
        }
    };
}
