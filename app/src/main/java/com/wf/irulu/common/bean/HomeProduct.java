package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @描述: 产品首页分类
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:HomeProductI
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class HomeProduct implements Parcelable,Serializable {

    /** id*/
    private String id;
    /** 标题*/
    private String title;
    /** 展示类型 1：显示多少折扣，2：显示星级，3：只有图片*/
    private String type;
    /** 广告列表*/
    private ArrayList<SlideBean> slide;
    /** 产品列表*/
    private ArrayList<ProductInfo>	productList;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<SlideBean> getSlide() {
        return slide;
    }

    public void setSlide(ArrayList<SlideBean> slide) {
        this.slide = slide;
    }

    public ArrayList<ProductInfo> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductInfo> productList) {
        this.productList = productList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeTypedList(slide);
        dest.writeTypedList(productList);
        dest.writeString(this.url);
    }

    public HomeProduct() {
    }

    protected HomeProduct(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.slide = in.createTypedArrayList(SlideBean.CREATOR);
        this.productList = in.createTypedArrayList(ProductInfo.CREATOR);
        this.url = in.readString();
    }

    public static final Creator<HomeProduct> CREATOR = new Creator<HomeProduct>() {
        public HomeProduct createFromParcel(Parcel source) {
            return new HomeProduct(source);
        }

        public HomeProduct[] newArray(int size) {
            return new HomeProduct[size];
        }
    };
}
