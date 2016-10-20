package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @描述: 信用卡支付的bean
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:CreditCardPay
 * @作者: 左杰
 * @创建时间:2015/11/17 19:00
 */
public class CreditCardPay implements Parcelable{

    /**订单id*/
    private String orderid;
    /**信用卡号*/
    private String cardid;
    /**有效期月份*/
    private String month;
    /**有效期年*/
    private String year;
    /**cvv码*/
    private String cvv;
    /**名*/
    private String first;
    /**姓*/
    private String last;
    /**城市*/
    private String city;
    /**地址1*/
    private String line1;
    /**地址2*/
    private String line2;
    /**洲*/
    private String state;
    /**邮编*/
    private String postCode;
    /**国家 2个字母缩写*/
    private String country;
    /**电话*/
    private String phone;

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderid);
        dest.writeString(this.cardid);
        dest.writeString(this.month);
        dest.writeString(this.year);
        dest.writeString(this.cvv);
        dest.writeString(this.first);
        dest.writeString(this.last);
        dest.writeString(this.city);
        dest.writeString(this.line1);
        dest.writeString(this.line2);
        dest.writeString(this.state);
        dest.writeString(this.postCode);
        dest.writeString(this.country);
        dest.writeString(this.phone);
    }

    public CreditCardPay() {
    }

    protected CreditCardPay(Parcel in) {
        this.orderid = in.readString();
        this.cardid = in.readString();
        this.month = in.readString();
        this.year = in.readString();
        this.cvv = in.readString();
        this.first = in.readString();
        this.last = in.readString();
        this.city = in.readString();
        this.line1 = in.readString();
        this.line2 = in.readString();
        this.state = in.readString();
        this.postCode = in.readString();
        this.country = in.readString();
        this.phone = in.readString();
    }

    public static final Creator<CreditCardPay> CREATOR = new Creator<CreditCardPay>() {
        public CreditCardPay createFromParcel(Parcel source) {
            return new CreditCardPay(source);
        }

        public CreditCardPay[] newArray(int size) {
            return new CreditCardPay[size];
        }
    };
}
