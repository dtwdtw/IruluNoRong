package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 售后信息描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ServInfo
 * @作者: 左杰
 * @创建时间:2015/11/16 16:10
 */
public class ServInfo implements Parcelable {

    // servId string 售后id
    private String servId;
    // status int 状态 0：关闭1：等待客服回复、2：客服已回复、3：拒绝售后
    private int status;
    //	type	int		售后类型 1：退款、2：退货
    private int type;

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.servId);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
    }

    public ServInfo() {
    }

    protected ServInfo(Parcel in) {
        this.servId = in.readString();
        this.status = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<ServInfo> CREATOR = new Creator<ServInfo>() {
        public ServInfo createFromParcel(Parcel source) {
            return new ServInfo(source);
        }

        public ServInfo[] newArray(int size) {
            return new ServInfo[size];
        }
    };
}
