package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: SKU详情
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:Type
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class Type implements Parcelable {

    /** SKU id */
    private int id;
    /** 图片 */
    private List<String> image;
    /** 促销 */
    private float price;
    /** 折扣价 */
    private float discountPrice;
    /** 库存 */
    private int stock;
    /** 销量 */
    private int sales;
    /** SKU列表 */
    private ArrayList<String> type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeStringList(this.image);
        dest.writeFloat(this.price);
        dest.writeFloat(this.discountPrice);
        dest.writeInt(this.stock);
        dest.writeInt(this.sales);
        dest.writeStringList(this.type);
    }

    public Type() {
    }

    protected Type(Parcel in) {
        this.id = in.readInt();
        this.image = in.createStringArrayList();
        this.price = in.readFloat();
        this.discountPrice = in.readFloat();
        this.stock = in.readInt();
        this.sales = in.readInt();
        this.type = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        public Type createFromParcel(Parcel source) {
            return new Type(source);
        }

        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }
}
