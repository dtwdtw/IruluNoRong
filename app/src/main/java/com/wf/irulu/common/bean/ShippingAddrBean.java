package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by daniel on 2015/11/7.
 */
public class ShippingAddrBean implements Parcelable {
    //		id		string		id
    private String id;
    //		firstName	string		Name
    private String firstName;
    //		lastName	string		LastName
    private String lastName;
    //		phone	string		手机号码
    private String phone;
    //		email	string		邮箱
    private String email;
    //		zipCode	string		邮政编码
    private String zipCode;
    // country string 国家代码
    private String country;
    // state string 州、省
    private String state;
    // city string 城市
    private String city;
    //		address1	string		发货详细地址1
    private String address1;
    //		address2	string		发货详细地址2
    private String address2;
    private int isDefault;
    private long createTime;
    private long updateTime;
    private String abbreviation;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.zipCode);
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.address1);
        dest.writeString(this.address2);
        dest.writeInt(this.isDefault);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeString(this.abbreviation);
    }

    public ShippingAddrBean() {
    }

    protected ShippingAddrBean(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.zipCode = in.readString();
        this.country = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.address1 = in.readString();
        this.address2 = in.readString();
        this.isDefault = in.readInt();
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.abbreviation = in.readString();
    }

    public static final Creator<ShippingAddrBean> CREATOR = new Creator<ShippingAddrBean>() {
        public ShippingAddrBean createFromParcel(Parcel source) {
            return new ShippingAddrBean(source);
        }

        public ShippingAddrBean[] newArray(int size) {
            return new ShippingAddrBean[size];
        }
    };
}
