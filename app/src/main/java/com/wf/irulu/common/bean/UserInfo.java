package com.wf.irulu.common.bean;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @描述: 普通用户信息描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:UserInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class UserInfo implements Parcelable,Serializable {

    /** 用户id*/
    private int uid;
    /** 昵称*/
    private String nickname;

//   用户头像
    private String headjpg;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "uid="+uid+"->nickname="+nickname+"->headjpg="+headjpg;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(headjpg);
    }

    public UserInfo() {
        this.uid = -1;
        this.nickname = "";
        this.headjpg="";
    }

    protected UserInfo(Parcel in) {
        this.uid = in.readInt();
        this.nickname = in.readString();
        this.headjpg=in.readString();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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
}
