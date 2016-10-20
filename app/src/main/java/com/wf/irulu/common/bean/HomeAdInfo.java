package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:HomeAdInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class HomeAdInfo implements Parcelable,Serializable {

    /** 启动广告信息*/
    private AdInfo adInfo;
    /** 弹窗信息*/
    private PopupInfo popupInfo;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.adInfo, 0);
        dest.writeParcelable(this.popupInfo, 0);
    }

    public HomeAdInfo() {
    }

    protected HomeAdInfo(Parcel in) {
        this.adInfo = in.readParcelable(AdInfo.class.getClassLoader());
        this.popupInfo = in.readParcelable(PopupInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<HomeAdInfo> CREATOR = new Parcelable.Creator<HomeAdInfo>() {
        public HomeAdInfo createFromParcel(Parcel source) {
            return new HomeAdInfo(source);
        }

        public HomeAdInfo[] newArray(int size) {
            return new HomeAdInfo[size];
        }
    };

    public AdInfo getAdInfo() {
        return adInfo;
    }

    public void setAdInfo(AdInfo adInfo) {
        this.adInfo = adInfo;
    }

    public PopupInfo getPopupInfo() {
        return popupInfo;
    }

    public void setPopupInfo(PopupInfo popupInfo) {
        this.popupInfo = popupInfo;
    }
}
