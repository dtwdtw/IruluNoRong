package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @描述: 弹窗信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:PopupInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class PopupInfo implements Parcelable,Serializable {

    /** id*/
    private String id;
    /** slide广告图片*/
    private String image;
    /** 跳转内容*/
    private String content;
    /** 类型 1：产品列表、2：产品详情、3：app内部H5、4：H5（跳浏览器）*/
    private String type;
    private String time;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.image);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeString(this.time);
        dest.writeString(this.title);
    }

    public PopupInfo() {
    }

    protected PopupInfo(Parcel in) {
        this.id = in.readString();
        this.image = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.time = in.readString();
        this.title = in.readString();
    }

    public static final Creator<PopupInfo> CREATOR = new Creator<PopupInfo>() {
        public PopupInfo createFromParcel(Parcel source) {
            return new PopupInfo(source);
        }

        public PopupInfo[] newArray(int size) {
            return new PopupInfo[size];
        }
    };
}
