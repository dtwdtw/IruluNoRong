package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategoryProductListBean implements Parcelable{
    private int id;
    private String uniq;
    private String name;
    private String cate;
    private int cateId;
    private String image;
    private float oriPrice;
    private float price;
    private float deliveryFee;
    private float saleprice;
    private float star;
    private int sales;
    private int commentNum;
    private int updateTime;
    private String percent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(float oriPrice) {
        this.oriPrice = oriPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public float getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(float saleprice) {
        this.saleprice = saleprice;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof CategoryProductListBean){
            CategoryProductListBean cpl = (CategoryProductListBean) o;
            return this.getId()==cpl.getId();
        }
        return super.equals(o);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.uniq);
        dest.writeString(this.name);
        dest.writeString(this.cate);
        dest.writeInt(this.cateId);
        dest.writeString(this.image);
        dest.writeFloat(this.oriPrice);
        dest.writeFloat(this.price);
        dest.writeFloat(this.deliveryFee);
        dest.writeFloat(this.saleprice);
        dest.writeFloat(this.star);
        dest.writeInt(this.sales);
        dest.writeInt(this.commentNum);
        dest.writeInt(this.updateTime);
        dest.writeString(this.percent);
    }

    public CategoryProductListBean() {
    }

    protected CategoryProductListBean(Parcel in) {
        this.id = in.readInt();
        this.uniq = in.readString();
        this.name = in.readString();
        this.cate = in.readString();
        this.cateId = in.readInt();
        this.image = in.readString();
        this.oriPrice = in.readFloat();
        this.price = in.readFloat();
        this.deliveryFee = in.readFloat();
        this.saleprice = in.readFloat();
        this.star = in.readFloat();
        this.sales = in.readInt();
        this.commentNum = in.readInt();
        this.updateTime = in.readInt();
        this.percent = in.readString();
    }

    public static final Creator<CategoryProductListBean> CREATOR = new Creator<CategoryProductListBean>() {
        public CategoryProductListBean createFromParcel(Parcel source) {
            return new CategoryProductListBean(source);
        }

        public CategoryProductListBean[] newArray(int size) {
            return new CategoryProductListBean[size];
        }
    };
}
