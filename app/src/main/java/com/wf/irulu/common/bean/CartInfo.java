package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 购物车详细信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CartInfo
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class CartInfo implements Parcelable {

    /** 商品总价格*/
    private float total;
    /** 商品配送价格*/
    private float deliveryFee;
    /** 商品数据*/
    private ArrayList<ProductCart> productList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.total);
        dest.writeFloat(this.deliveryFee);
        dest.writeList(this.productList);
    }

    public CartInfo() {
    }

    protected CartInfo(Parcel in) {
        this.total = in.readFloat();
        this.deliveryFee = in.readFloat();
        this.productList = new ArrayList<ProductCart>();
        in.readList(this.productList, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartInfo> CREATOR = new Parcelable.Creator<CartInfo>() {
        public CartInfo createFromParcel(Parcel source) {
            return new CartInfo(source);
        }

        public CartInfo[] newArray(int size) {
            return new CartInfo[size];
        }
    };

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public ArrayList<ProductCart> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductCart> productList) {
        this.productList = productList;
    }
}
