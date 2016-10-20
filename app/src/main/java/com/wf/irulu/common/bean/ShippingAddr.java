package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daniel on 2015/11/7.
 */
public class ShippingAddr implements Parcelable {
    private List<ShippingAddrBean> list;
    private int total;

    public List<ShippingAddrBean> getList() {
        return list;
    }

    public void setList(List<ShippingAddrBean> list) {
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

    public ShippingAddr() {
    }

    protected ShippingAddr(Parcel in) {
        this.list = in.createTypedArrayList(ShippingAddrBean.CREATOR);
        this.total = in.readInt();
    }

    public static final Creator<ShippingAddr> CREATOR = new Creator<ShippingAddr>() {
        public ShippingAddr createFromParcel(Parcel source) {
            return new ShippingAddr(source);
        }

        public ShippingAddr[] newArray(int size) {
            return new ShippingAddr[size];
        }
    };
}
