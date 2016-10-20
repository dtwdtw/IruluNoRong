package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 首页产品详情信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ProductInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class ProductInfo implements Parcelable{

    private String id;
    /** 产品id*/
    private String productId;
    /** 产品图片*/
    private String image;
    /**唯一id*/
    private String uniq;
    // quantity int 商品数量
    private int quantity;
    /** 原价*/
    private String price;
    // sku array String 商品sku
    private ArrayList<String> sku;
    /** 折扣价*/
    private String discountPrice;
    // name string 商品名称
    private String name;
    // servInfo json 售后信息
    private ServInfo servInfo;
    /** 折扣比*/
    private String percent;
    /** 星级 0～5*/
    private String star;
    /** 评论数*/
    private String commentNum;
    /** 是否加入到Wish List 0：否、1：是*/
    private String addWishList;
    private int isCanNotSend;//是否限售
    private String msg;//限售消息提示

    public String getAddWishList() {
        return addWishList;
    }

    public void setAddWishList(String addWishList) {
        this.addWishList = addWishList;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
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

    public int getIsCanNotSend() {
        return isCanNotSend;
    }

    public void setIsCanNotSend(int isCanNotSend) {
        this.isCanNotSend = isCanNotSend;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ServInfo getServInfo() {
        return servInfo;
    }

    public void setServInfo(ServInfo servInfo) {
        this.servInfo = servInfo;
    }

    public ArrayList<String> getSku() {
        return sku;
    }

    public void setSku(ArrayList<String> sku) {
        this.sku = sku;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getUniq() {
        return uniq;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }


    @Override
    public boolean equals(Object o) {
        if(!(o instanceof  ProductInfo)){
            return false;
        }
        ProductInfo vProductInfo=(ProductInfo)o;
        return   vProductInfo.productId.equals(productId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.productId);
        dest.writeString(this.image);
        dest.writeString(this.uniq);
        dest.writeInt(this.quantity);
        dest.writeString(this.price);
        dest.writeStringList(this.sku);
        dest.writeString(this.discountPrice);
        dest.writeString(this.name);
        dest.writeParcelable(this.servInfo, 0);
        dest.writeString(this.percent);
        dest.writeString(this.star);
        dest.writeString(this.commentNum);
        dest.writeString(this.addWishList);
        dest.writeInt(this.isCanNotSend);
        dest.writeString(this.msg);
    }

    public ProductInfo() {
    }

    protected ProductInfo(Parcel in) {
        this.id = in.readString();
        this.productId = in.readString();
        this.image = in.readString();
        this.uniq = in.readString();
        this.quantity = in.readInt();
        this.price = in.readString();
        this.sku = in.createStringArrayList();
        this.discountPrice = in.readString();
        this.name = in.readString();
        this.servInfo = in.readParcelable(ServInfo.class.getClassLoader());
        this.percent = in.readString();
        this.star = in.readString();
        this.commentNum = in.readString();
        this.addWishList = in.readString();
        this.isCanNotSend = in.readInt();
        this.msg = in.readString();
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
}
