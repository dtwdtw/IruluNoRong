package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 买一送一信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:GiftInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class GiftInfo implements Parcelable {

    /** 礼物id*/
    private int id;
    /** 产品id*/
    private int productId;
    /** sku id*/
    private int skuId;
    /** 产品名称*/
    private String name;
    /** 数量*/
    private int num;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.productId);
        dest.writeInt(this.skuId);
        dest.writeString(this.name);
        dest.writeInt(this.num);
    }

    public GiftInfo() {
    }

    protected GiftInfo(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.skuId = in.readInt();
        this.name = in.readString();
        this.num = in.readInt();
    }

    public static final Parcelable.Creator<GiftInfo> CREATOR = new Parcelable.Creator<GiftInfo>() {
        public GiftInfo createFromParcel(Parcel source) {
            return new GiftInfo(source);
        }

        public GiftInfo[] newArray(int size) {
            return new GiftInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
