package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 启动广告的信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:AdInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class AdInfo implements Parcelable {

    /** id*/
    private String id;
    /** slide广告图片*/
    private String image;
    /** 跳转内容*/
    private String content;
    /** 类型 1：产品列表、2：产品详情、3：app内部H5、4：H5（跳浏览器）*/
    private String type;
    /** 广告时间*/
    private int time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeInt(this.time);
    }

    public AdInfo() {
    }

    protected AdInfo(Parcel in) {
        this.id = in.readString();
        this.image = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.time = in.readInt();
    }

    public static final Creator<AdInfo> CREATOR = new Creator<AdInfo>() {
        public AdInfo createFromParcel(Parcel source) {
            return new AdInfo(source);
        }

        public AdInfo[] newArray(int size) {
            return new AdInfo[size];
        }
    };
}
