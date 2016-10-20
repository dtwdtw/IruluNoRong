package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 启动广告Bean
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:StartAdvertising
 * @作者: 左杰
 * @创建时间:2015/11/25 11:50
 */
public class StartAdvertising implements Parcelable {

    private AdInfo adInfo;
    private PopupInfo popupInfo;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.adInfo, 0);
        dest.writeParcelable(this.popupInfo, 0);
    }

    public StartAdvertising() {
    }

    protected StartAdvertising(Parcel in) {
        this.adInfo = in.readParcelable(AdInfo.class.getClassLoader());
        this.popupInfo = in.readParcelable(PopupInfo.class.getClassLoader());
    }

    public static final Creator<StartAdvertising> CREATOR = new Creator<StartAdvertising>() {
        public StartAdvertising createFromParcel(Parcel source) {
            return new StartAdvertising(source);
        }

        public StartAdvertising[] newArray(int size) {
            return new StartAdvertising[size];
        }
    };
}
