package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 物流信息描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:LogisticsInfo
 * @作者: 左杰
 * @创建时间:2015/11/16 15:37
 */
public class LogisticsInfo implements Parcelable {

//    logisticsId	string		物流单号
    private String logisticsId;
    //		companyInfo	json		物流公司信息
    private CompanyInfo companyInfo;
    //		logisticsDetail	json array		物流详情
    private ArrayList<Logistic> logisticsDetail;

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public ArrayList<Logistic> getLogisticsDetail() {
        return logisticsDetail;
    }

    public void setLogisticsDetail(ArrayList<Logistic> logisticsDetail) {
        this.logisticsDetail = logisticsDetail;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.logisticsId);
        dest.writeParcelable(this.companyInfo, 0);
        dest.writeTypedList(logisticsDetail);
    }

    public LogisticsInfo() {
    }

    protected LogisticsInfo(Parcel in) {
        this.logisticsId = in.readString();
        this.companyInfo = in.readParcelable(CompanyInfo.class.getClassLoader());
        this.logisticsDetail = in.createTypedArrayList(Logistic.CREATOR);
    }

    public static final Creator<LogisticsInfo> CREATOR = new Creator<LogisticsInfo>() {
        public LogisticsInfo createFromParcel(Parcel source) {
            return new LogisticsInfo(source);
        }

        public LogisticsInfo[] newArray(int size) {
            return new LogisticsInfo[size];
        }
    };
}
