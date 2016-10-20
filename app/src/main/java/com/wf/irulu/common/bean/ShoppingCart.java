package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 购物车信息描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:ShoppingCart
 * @作者: Yuki
 * @创建时间:2015/10/24
 */
public class ShoppingCart implements Parcelable {

    /** 保存的商品*/
    public CartInfo savedList;
    /** 当前购买商品*/
    public CartInfo shoppingList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.savedList, 0);
        dest.writeParcelable(this.shoppingList, 0);
    }

    public ShoppingCart() {
    }

    protected ShoppingCart(Parcel in) {
        this.savedList = in.readParcelable(CartInfo.class.getClassLoader());
        this.shoppingList = in.readParcelable(CartInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShoppingCart> CREATOR = new Parcelable.Creator<ShoppingCart>() {
        public ShoppingCart createFromParcel(Parcel source) {
            return new ShoppingCart(source);
        }

        public ShoppingCart[] newArray(int size) {
            return new ShoppingCart[size];
        }
    };
}
