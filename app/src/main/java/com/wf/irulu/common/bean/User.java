package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by iRULU on 2016/4/26.
 */
public class User implements Parcelable ,Serializable{

    /**
     * uid : 0
     * nickname : A***a B
     */

    private int uid;
    private String nickname;
    private String headjpg;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.headjpg);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.uid = in.readInt();
        this.nickname = in.readString();
        this.headjpg = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
