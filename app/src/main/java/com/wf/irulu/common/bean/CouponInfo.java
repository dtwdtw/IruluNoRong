package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 优惠券描述信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CouponInfo
 * @作者: 左杰
 * @创建时间:2015/11/16 15:51
 */
public class CouponInfo implements Parcelable {

    /**优惠券状态 1：可用、0：不可用*/
    private int status;
    /**优惠券提示文字*/
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.msg);
    }

    public CouponInfo() {
    }

    protected CouponInfo(Parcel in) {
        this.status = in.readInt();
        this.msg = in.readString();
    }

    public static final Creator<CouponInfo> CREATOR = new Creator<CouponInfo>() {
        public CouponInfo createFromParcel(Parcel source) {
            return new CouponInfo(source);
        }

        public CouponInfo[] newArray(int size) {
            return new CouponInfo[size];
        }
    };
}
