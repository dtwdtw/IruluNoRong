package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/12/4.
 */
public class TrackingInforBean implements Parcelable {
    private ArrayList<TrackingInfor> items;
    private String logisticsCompany;
    private String logisticsId;
    private String orderid;

    public ArrayList<TrackingInfor> getItems() {
        return items;
    }

    public void setItems(ArrayList<TrackingInfor> items) {
        this.items = items;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(this.logisticsCompany);
        dest.writeString(this.logisticsId);
        dest.writeString(this.orderid);
    }

    public TrackingInforBean() {
    }

    protected TrackingInforBean(Parcel in) {
        this.items = in.createTypedArrayList(TrackingInfor.CREATOR);
        this.logisticsCompany = in.readString();
        this.logisticsId = in.readString();
        this.orderid = in.readString();
    }

    public static final Creator<TrackingInforBean> CREATOR = new Creator<TrackingInforBean>() {
        public TrackingInforBean createFromParcel(Parcel source) {
            return new TrackingInforBean(source);
        }

        public TrackingInforBean[] newArray(int size) {
            return new TrackingInforBean[size];
        }
    };
}
