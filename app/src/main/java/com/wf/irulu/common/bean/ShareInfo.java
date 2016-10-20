package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ShareInfo
 * @作者: Yuki
 * @创建时间:2015/11/3
 */
public class ShareInfo implements Parcelable {

    private String content;
    private String icon;
    private String link;
    private String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.icon);
        dest.writeString(this.link);
        dest.writeString(this.title);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ShareInfo() {
    }

    protected ShareInfo(Parcel in) {
        this.content = in.readString();
        this.icon = in.readString();
        this.link = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<ShareInfo> CREATOR = new Parcelable.Creator<ShareInfo>() {
        public ShareInfo createFromParcel(Parcel source) {
            return new ShareInfo(source);
        }

        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };
}
