package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dtw on 16/4/22.
 */
public class SlideBean implements Parcelable {
    private String type;
    private String content;
    private int id;
    private String image;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeInt(this.id);
        dest.writeString(this.image);
    }

    public SlideBean() {
    }

    protected SlideBean(Parcel in) {
        this.type = in.readString();
        this.content = in.readString();
        this.id = in.readInt();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<SlideBean> CREATOR = new Parcelable.Creator<SlideBean>() {
        @Override
        public SlideBean createFromParcel(Parcel source) {
            return new SlideBean(source);
        }

        @Override
        public SlideBean[] newArray(int size) {
            return new SlideBean[size];
        }
    };
}