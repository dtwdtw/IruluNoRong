package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 生成订单bean
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CreateOrder
 * @作者: 左杰
 * @创建时间:2015/11/17 14:56
 */
public class CreateOrder implements Parcelable {

    private String paynowId;// 分单与订单对应id，在支付成功调验证接口时使用
    private float totalPrice;// 订单总价（商品总价+商品配送价+促销折扣
    private float sumPrice;// 商品总价
    private float shipping;// 配送金额
    private float savings;// 促销折扣
    private String currencyCode;// 货币代码
    /**订单数据*/
    private ArrayList<OrderDetail> list;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public ArrayList<OrderDetail> getList() {
        return list;
    }

    public void setList(ArrayList<OrderDetail> list) {
        this.list = list;
    }

    public String getPaynowId() {
        return paynowId;
    }

    public void setPaynowId(String paynowId) {
        this.paynowId = paynowId;
    }

    public float getSavings() {
        return savings;
    }

    public void setSavings(float savings) {
        this.savings = savings;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paynowId);
        dest.writeFloat(this.totalPrice);
        dest.writeFloat(this.sumPrice);
        dest.writeFloat(this.shipping);
        dest.writeFloat(this.savings);
        dest.writeString(this.currencyCode);
        dest.writeTypedList(list);
    }

    public CreateOrder() {
    }

    protected CreateOrder(Parcel in) {
        this.paynowId = in.readString();
        this.totalPrice = in.readFloat();
        this.sumPrice = in.readFloat();
        this.shipping = in.readFloat();
        this.savings = in.readFloat();
        this.currencyCode = in.readString();
        this.list = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    public static final Creator<CreateOrder> CREATOR = new Creator<CreateOrder>() {
        public CreateOrder createFromParcel(Parcel source) {
            return new CreateOrder(source);
        }

        public CreateOrder[] newArray(int size) {
            return new CreateOrder[size];
        }
    };
}
