package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by daniel on 2015/11/11.
 */
public class CountryInforBean implements Parcelable{
    private String countryName;
    private String firstLetter;
    private String icon;
    private String id;
    private int nunberIso;
    private int sortNumber;
    private String telcode;
    private String threeLetterIso;
    private String twoLetterIso;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getNunberIso() {
        return nunberIso;
    }

    public void setNunberIso(int nunberIso) {
        this.nunberIso = nunberIso;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(int sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getTelcode() {
        return telcode;
    }

    public void setTelcode(String telcode) {
        this.telcode = telcode;
    }

    public String getThreeLetterIso() {
        return threeLetterIso;
    }

    public void setThreeLetterIso(String threeLetterIso) {
        this.threeLetterIso = threeLetterIso;
    }

    public String getTwoLetterIso() {
        return twoLetterIso;
    }

    public void setTwoLetterIso(String twoLetterIso) {
        this.twoLetterIso = twoLetterIso;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryName);
        dest.writeString(this.firstLetter);
        dest.writeString(this.icon);
        dest.writeString(this.id);
        dest.writeInt(this.nunberIso);
        dest.writeInt(this.sortNumber);
        dest.writeString(this.telcode);
        dest.writeString(this.threeLetterIso);
        dest.writeString(this.twoLetterIso);
    }

    public CountryInforBean() {
    }

    protected CountryInforBean(Parcel in) {
        this.countryName = in.readString();
        this.firstLetter = in.readString();
        this.icon = in.readString();
        this.id = in.readString();
        this.nunberIso = in.readInt();
        this.sortNumber = in.readInt();
        this.telcode = in.readString();
        this.threeLetterIso = in.readString();
        this.twoLetterIso = in.readString();
    }

    public static final Creator<CountryInforBean> CREATOR = new Creator<CountryInforBean>() {
        public CountryInforBean createFromParcel(Parcel source) {
            return new CountryInforBean(source);
        }

        public CountryInforBean[] newArray(int size) {
            return new CountryInforBean[size];
        }
    };
}
