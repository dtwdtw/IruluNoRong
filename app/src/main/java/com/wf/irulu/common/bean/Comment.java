package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 评论内容
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:Comment
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class Comment implements Parcelable {

    /** 评论id*/
    private String id;
    /** 评论星级*/
    private int star;
    /** 评论内容*/
    private String content;
    /** 评论图片*/
    private ArrayList<String> image;
    /** 用户信息*/
    private UserInfo userInfo;

    private User user;
    /** 评论时间*/
    private long time;
    /** */
    private ArrayList<CustomerReply> child;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<CustomerReply> getChild() {
        return child;
    }

    public void setChild(ArrayList<CustomerReply> child) {
        this.child = child;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.star);
        dest.writeString(this.content);
        dest.writeStringList(this.image);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.time);
        dest.writeTypedList(child);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.id = in.readString();
        this.star = in.readInt();
        this.content = in.readString();
        this.image = in.createStringArrayList();
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.time = in.readLong();
        this.child = in.createTypedArrayList(CustomerReply.CREATOR);
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
