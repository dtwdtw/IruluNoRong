package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CheckoutBean
 * @作者: 左杰
 * @创建时间:2015/11/16 15:35
 */
public class CheckoutBean implements Parcelable {

    /**订单数据*/
    private ArrayList<OrderDetail> list;
    /**商品总金额*/
    private float total;
    /**优惠券信息*/
    private CouponInfo couponInfo;
    /** 促销信息*/
    private String discountMsg;
    /** 折扣比（例：2%）*/
    private int discountPrecent;

    public CouponInfo getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(CouponInfo couponInfo) {
        this.couponInfo = couponInfo;
    }

    public String getDiscountMsg() {
        return discountMsg;
    }

    public void setDiscountMsg(String discountMsg) {
        this.discountMsg = discountMsg;
    }

    public int getDiscountPrecent() {
        return discountPrecent;
    }

    public void setDiscountPrecent(int discountPrecent) {
        this.discountPrecent = discountPrecent;
    }

    public ArrayList<OrderDetail> getList() {
        return list;
    }

    public void setList(ArrayList<OrderDetail> list) {
        this.list = list;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
        dest.writeFloat(this.total);
        dest.writeParcelable(this.couponInfo, 0);
        dest.writeString(this.discountMsg);
        dest.writeInt(this.discountPrecent);
    }

    public CheckoutBean() {
    }

    protected CheckoutBean(Parcel in) {
        this.list = in.createTypedArrayList(OrderDetail.CREATOR);
        this.total = in.readFloat();
        this.couponInfo = in.readParcelable(CouponInfo.class.getClassLoader());
        this.discountMsg = in.readString();
        this.discountPrecent = in.readInt();
    }

    public static final Creator<CheckoutBean> CREATOR = new Creator<CheckoutBean>() {
        public CheckoutBean createFromParcel(Parcel source) {
            return new CheckoutBean(source);
        }

        public CheckoutBean[] newArray(int size) {
            return new CheckoutBean[size];
        }
    };
}
