package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/25.
 */
public class Product implements Parcelable {

    private String image;
    private String id;
    private String name;
    private float price;
    private int quantity;
    private ArrayList<String> sku;
    private int statu = 1;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getSku() {
        return sku;
    }

    public void setSku(ArrayList<String> sku) {
        this.sku = sku;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public Product() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeInt(this.quantity);
        dest.writeStringList(this.sku);
        dest.writeInt(this.statu);
    }

    protected Product(Parcel in) {
        this.image = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readFloat();
        this.quantity = in.readInt();
        this.sku = in.createStringArrayList();
        this.statu = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
