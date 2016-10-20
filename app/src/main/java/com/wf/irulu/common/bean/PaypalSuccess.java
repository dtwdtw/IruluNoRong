package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: paypal支付成功后返回的Bean
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:PaypalSuccess
 * @作者: 左杰
 * @创建时间:2015/11/17 19:13
 */
public class PaypalSuccess implements Parcelable{

    /**订单号*/
    private ArrayList<String> orderList;
    /**Paypal返回的支付交易ID*/
    private String transactionId;
    /**货币类型*/
    private String currency;
    /**支付总金额*/
    private float total;
    /**支付状态，2支付成功，1待审核，0支付不成功*/
    private int payStatus;
    /** pending状态的说明*/
    private String pendingReason;
    /**商品列表*/
    private ArrayList<ProductInfo> itemList;

    public ArrayList<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<String> orderList) {
        this.orderList = orderList;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getPendingReason() {
        return pendingReason;
    }

    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
    }

    public ArrayList<ProductInfo> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ProductInfo> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.orderList);
        dest.writeString(this.transactionId);
        dest.writeString(this.currency);
        dest.writeFloat(this.total);
        dest.writeInt(this.payStatus);
        dest.writeString(this.pendingReason);
        dest.writeTypedList(itemList);
    }

    public PaypalSuccess() {
    }

    protected PaypalSuccess(Parcel in) {
        this.orderList = in.createStringArrayList();
        this.transactionId = in.readString();
        this.currency = in.readString();
        this.total = in.readFloat();
        this.payStatus = in.readInt();
        this.pendingReason = in.readString();
        this.itemList = in.createTypedArrayList(ProductInfo.CREATOR);
    }

    public static final Creator<PaypalSuccess> CREATOR = new Creator<PaypalSuccess>() {
        public PaypalSuccess createFromParcel(Parcel source) {
            return new PaypalSuccess(source);
        }

        public PaypalSuccess[] newArray(int size) {
            return new PaypalSuccess[size];
        }
    };
}
