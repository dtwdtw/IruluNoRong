package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 客服消息用户信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:MessageIds
 * @作者: 左杰
 * @创建时间:2015/11/23 18:40
 */
public class MessageIds implements Parcelable {

    /** 用户id*/
    private String uid;
    /** 昵称*/
    private String nickname;
    /** 头像*/
    private String headjpg;
    /** 用户类型 1：客服、2：订单、3：折扣*/
    private int userType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadjpg() {
        return headjpg;
    }

    public void setHeadjpg(String headjpg) {
        this.headjpg = headjpg;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.headjpg);
        dest.writeInt(this.userType);
    }

    public MessageIds() {
    }

    protected MessageIds(Parcel in) {
        this.uid = in.readString();
        this.nickname = in.readString();
        this.headjpg = in.readString();
        this.userType = in.readInt();
    }

    public static final Creator<MessageIds> CREATOR = new Creator<MessageIds>() {
        public MessageIds createFromParcel(Parcel source) {
            return new MessageIds(source);
        }

        public MessageIds[] newArray(int size) {
            return new MessageIds[size];
        }
    };
}