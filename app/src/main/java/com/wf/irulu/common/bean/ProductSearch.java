package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ProductSearch
 * @作者: Yuki
 * @创建时间:2015/11/26
 */
public class ProductSearch implements Parcelable {

//    cate	Phone
    private String cate;
//    cateId	1313436
    private int cateId;
//    commentNum	23
    private int commentNum;
//    deliveryFee	54
    private float deliveryFee;
//    id	1913486
    private int id;
//    image	http://7xjxdy.com1.z0.glb.clouddn.com/product/cover/20150803/51c5e3b74a39bc3e120ea.jpg
    private String image;
//    isMobilePrice	0
    private int isMobilePrice;
//    name	White iRulu 5
    private String name;
//    oriPrice	900
    private float oriPrice;
//    percentTag	80%
    private String percentTag;
//    price	180
    private float price;
//    saleprice	900
    private float saleprice;
//    sales	13682
    private int sales;
//    star	4.7
    private float star;
//    tag	Mobile
    private String tag ;
//    uniq
    private String uniq;
//    updateTime	1448518367
    private long updateTime;

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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.deliveryFee = deliveryFee;
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

    public int getIsMobilePrice() {
        return isMobilePrice;
    }

    public void setIsMobilePrice(int isMobilePrice) {
        this.isMobilePrice = isMobilePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(float oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getPercentTag() {
        return percentTag;
    }

    public void setPercentTag(String percentTag) {
        this.percentTag = percentTag;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(float saleprice) {
        this.saleprice = saleprice;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cate);
        dest.writeInt(this.cateId);
        dest.writeInt(this.commentNum);
        dest.writeFloat(this.deliveryFee);
        dest.writeInt(this.id);
        dest.writeString(this.image);
        dest.writeInt(this.isMobilePrice);
        dest.writeString(this.name);
        dest.writeFloat(this.oriPrice);
        dest.writeString(this.percentTag);
        dest.writeFloat(this.price);
        dest.writeFloat(this.saleprice);
        dest.writeInt(this.sales);
        dest.writeFloat(this.star);
        dest.writeString(this.tag);
        dest.writeString(this.uniq);
        dest.writeLong(this.updateTime);
    }

    public ProductSearch() {
    }

    protected ProductSearch(Parcel in) {
        this.cate = in.readString();
        this.cateId = in.readInt();
        this.commentNum = in.readInt();
        this.deliveryFee = in.readFloat();
        this.id = in.readInt();
        this.image = in.readString();
        this.isMobilePrice = in.readInt();
        this.name = in.readString();
        this.oriPrice = in.readFloat();
        this.percentTag = in.readString();
        this.price = in.readFloat();
        this.saleprice = in.readFloat();
        this.sales = in.readInt();
        this.star = in.readFloat();
        this.tag = in.readString();
        this.uniq = in.readString();
        this.updateTime = in.readLong();
    }

    public static final Parcelable.Creator<ProductSearch> CREATOR = new Parcelable.Creator<ProductSearch>() {
        public ProductSearch createFromParcel(Parcel source) {
            return new ProductSearch(source);
        }

        public ProductSearch[] newArray(int size) {
            return new ProductSearch[size];
        }
    };
}
