package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 猜你喜欢
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:MaybeLike
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class MaybeLike implements Parcelable {

    /**
     * id : 1913428
     * uniq :
     * name : iRULU eXpro X1 7 inch android 4.4.2 Kitkat quad core 5 Point Capacitive HD Touch Screen tablet pc
     * cate : Tablet
     * cateId : 1013446
     * image : https://images.irulu.com/product/cover/20150828/51e5cb1ffb3921530cd3e5.jpg
     * oriPrice : 131.99
     * price : 35.99
     * isMobilePrice : 1
     * status : 1
     * deliveryFee : 0
     * saleprice : 131.99
     * star : 4.8
     * sales : 879
     * commentNum : 75
     * addTime : 1440759975
     * updateTime : 1461639725
     * percentTag : 73%
     * addWishList : 0
     * percent : 73
     * tag : Mobile
     */

    private int id;
    private String uniq;
    private String name;
    private String cate;
    private int cateId;
    private String image;
    private double oriPrice;
    private double price;
    private int isMobilePrice;
    private int status;
    private int deliveryFee;
    private double saleprice;
    private double star;
    private int sales;
    private int commentNum;
    private int addTime;
    private int updateTime;
    private String percentTag;
    private int addWishList;
    private int percent;
    private String tag;

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

    public int getIsMobilePrice() {
        return isMobilePrice;
    }

    public void setIsMobilePrice(int isMobilePrice) {
        this.isMobilePrice = isMobilePrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(double saleprice) {
        this.saleprice = saleprice;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
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

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public String getPercentTag() {
        return percentTag;
    }

    public void setPercentTag(String percentTag) {
        this.percentTag = percentTag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        dest.writeDouble(this.oriPrice);
        dest.writeDouble(this.price);
        dest.writeInt(this.isMobilePrice);
        dest.writeInt(this.status);
        dest.writeInt(this.deliveryFee);
        dest.writeDouble(this.saleprice);
        dest.writeDouble(this.star);
        dest.writeInt(this.sales);
        dest.writeInt(this.commentNum);
        dest.writeInt(this.addTime);
        dest.writeInt(this.updateTime);
        dest.writeString(this.percentTag);
        dest.writeInt(this.addWishList);
        dest.writeInt(this.percent);
        dest.writeString(this.tag);
    }

    public MaybeLike() {
    }

    protected MaybeLike(Parcel in) {
        this.id = in.readInt();
        this.uniq = in.readString();
        this.name = in.readString();
        this.cate = in.readString();
        this.cateId = in.readInt();
        this.image = in.readString();
        this.oriPrice = in.readDouble();
        this.price = in.readDouble();
        this.isMobilePrice = in.readInt();
        this.status = in.readInt();
        this.deliveryFee = in.readInt();
        this.saleprice = in.readDouble();
        this.star = in.readDouble();
        this.sales = in.readInt();
        this.commentNum = in.readInt();
        this.addTime = in.readInt();
        this.updateTime = in.readInt();
        this.percentTag = in.readString();
        this.addWishList = in.readInt();
        this.percent = in.readInt();
        this.tag = in.readString();
    }

    public static final Parcelable.Creator<MaybeLike> CREATOR = new Parcelable.Creator<MaybeLike>() {
        @Override
        public MaybeLike createFromParcel(Parcel source) {
            return new MaybeLike(source);
        }

        @Override
        public MaybeLike[] newArray(int size) {
            return new MaybeLike[size];
        }
    };
}
