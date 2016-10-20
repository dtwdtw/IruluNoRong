package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 订单详情描述
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:OrderDetail
 * @作者: 左杰
 * @创建时间:2015/11/16 15:36
 */
public class OrderDetail implements Parcelable {

    // createTime string 交易时间（时间戳）
    private String createTime;
    // updateTime string 更新时间（时间戳）
    private String updateTime;
    // remainderDays int 剩余送货天数
    private int remainderDays;
    // orderId string 订单编号
    private String orderId;
    // status int 订单状态： 1 No-Pay 未付款 2 Pending 待审核 3 Paid 已付款 4 Shipped 已发货
    // 5 Frozen 交易冻结 6 Closed 交易关闭 7 Completed 完成
    private int status;
    // shipping float 商品物流费
    private float shipping;
    // savings float 节省费用
    private float savings;
    // sumPrice float 商品总价
    private float sumPrice;
    // totalPrice float 订单总金额
    private float totalPrice;
    // currencyCode string 货币代码
    private String currencyCode;
    // logisticsInfo json 物流信息
    private LogisticsInfo logisticsInfo;
    // shoppingAddr Json 配送地址
    private ShippingAddrBean shoppingAddr;
    // refundReason string 退单原因
    private String refundReason;
    // productList array 商品列表
    private ArrayList<OrderDetailProduct> productList;
    // payTime string 支付时间 0表示未支付
    private String payTime;
    // servStatus int 售后状态 0：暂停售后、1：可以售后
    private int servStatus;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getSavings() {
        return savings;
    }

    public void setSavings(float savings) {
        this.savings = savings;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LogisticsInfo getLogisticsInfo() {
        return logisticsInfo;
    }

    public void setLogisticsInfo(LogisticsInfo logisticsInfo) {
        this.logisticsInfo = logisticsInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public ArrayList<OrderDetailProduct> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<OrderDetailProduct> productList) {
        this.productList = productList;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public int getRemainderDays() {
        return remainderDays;
    }

    public void setRemainderDays(int remainderDays) {
        this.remainderDays = remainderDays;
    }

    public int getServStatus() {
        return servStatus;
    }

    public void setServStatus(int servStatus) {
        this.servStatus = servStatus;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public ShippingAddrBean getShoppingAddr() {
        return shoppingAddr;
    }

    public void setShoppingAddr(ShippingAddrBean shoppingAddr) {
        this.shoppingAddr = shoppingAddr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeInt(this.remainderDays);
        dest.writeString(this.orderId);
        dest.writeInt(this.status);
        dest.writeFloat(this.shipping);
        dest.writeFloat(this.savings);
        dest.writeFloat(this.sumPrice);
        dest.writeFloat(this.totalPrice);
        dest.writeString(this.currencyCode);
        dest.writeParcelable(this.logisticsInfo, 0);
        dest.writeParcelable(this.shoppingAddr, 0);
        dest.writeString(this.refundReason);
        dest.writeTypedList(productList);
        dest.writeString(this.payTime);
        dest.writeInt(this.servStatus);
    }

    public OrderDetail() {
    }

    protected OrderDetail(Parcel in) {
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.remainderDays = in.readInt();
        this.orderId = in.readString();
        this.status = in.readInt();
        this.shipping = in.readFloat();
        this.savings = in.readFloat();
        this.sumPrice = in.readFloat();
        this.totalPrice = in.readFloat();
        this.currencyCode = in.readString();
        this.logisticsInfo = in.readParcelable(LogisticsInfo.class.getClassLoader());
        this.shoppingAddr = in.readParcelable(ShippingAddrBean.class.getClassLoader());
        this.refundReason = in.readString();
        this.productList = in.createTypedArrayList(OrderDetailProduct.CREATOR);
        this.payTime = in.readString();
        this.servStatus = in.readInt();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        public OrderDetail createFromParcel(Parcel source) {
            return new OrderDetail(source);
        }

        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}
