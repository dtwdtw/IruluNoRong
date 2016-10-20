package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iRULU on 2016/4/29.
 */
public class CommonProduct implements Parcelable {
    private String productId;
    private int id;
    private String name;
    private String image;
    private double oriPrice;
    private double price;
    private int addWishList;
    private int percent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(double oriPrice) {
        this.oriPrice = oriPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAddWishList() {
        return addWishList;
    }

    public void setAddWishList(int addWishList) {
        this.addWishList = addWishList;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeDouble(this.oriPrice);
        dest.writeDouble(this.price);
        dest.writeInt(this.addWishList);
        dest.writeInt(this.percent);
    }

    public CommonProduct() {
    }

    protected CommonProduct(Parcel in) {
        this.productId = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.oriPrice = in.readDouble();
        this.price = in.readDouble();
        this.addWishList = in.readInt();
        this.percent = in.readInt();
    }

    public static final Parcelable.Creator<CommonProduct> CREATOR = new Parcelable.Creator<CommonProduct>() {
        @Override
        public CommonProduct createFromParcel(Parcel source) {
            return new CommonProduct(source);
        }

        @Override
        public CommonProduct[] newArray(int size) {
            return new CommonProduct[size];
        }
    };
}
