package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @描述: APP初始化Bean
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.bean
 * @类名:InitAppBean
 * @作者: 左杰
 * @创建时间:2015/11/23 18:40
 */
public class InitAppBean implements Parcelable {

    private String currencyCode;// 货币符号
    private List<MessageIds> messageIds;// 客服消息用户信息
    private String priceSymbol;// 价格符号
    private String rongCloudToken;// 融云token
    private int creditCardAvailable;// 是否开启信用卡支付 1：是、0：否
    private ShareInfo shareInfo;// 分享信息
    private int nativePaymentEnabled=0;//0:m端支付 1:本地支付


    public int getCreditCardAvailable() {
        return creditCardAvailable;
    }

    public void setCreditCardAvailable(int creditCardAvailable) {
        this.creditCardAvailable = creditCardAvailable;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<MessageIds> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<MessageIds> messageIds) {
        this.messageIds = messageIds;
    }

    public String getPriceSymbol() {
        return priceSymbol;
    }

    public void setPriceSymbol(String priceSymbol) {
        this.priceSymbol = priceSymbol;
    }

    public String getRongCloudToken() {
        return rongCloudToken;
    }

    public void setRongCloudToken(String rongCloudToken) {
        this.rongCloudToken = rongCloudToken;
    }

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currencyCode);
        dest.writeTypedList(messageIds);
        dest.writeString(this.priceSymbol);
        dest.writeString(this.rongCloudToken);
        dest.writeInt(this.creditCardAvailable);
        dest.writeParcelable(this.shareInfo, 0);
        dest.writeInt(this.nativePaymentEnabled);
    }

    public InitAppBean() {
    }

    protected InitAppBean(Parcel in) {
        this.currencyCode = in.readString();
        this.messageIds = in.createTypedArrayList(MessageIds.CREATOR);
        this.priceSymbol = in.readString();
        this.rongCloudToken = in.readString();
        this.creditCardAvailable = in.readInt();
        this.shareInfo = in.readParcelable(ShareInfo.class.getClassLoader());
        this.nativePaymentEnabled=in.readInt();
    }

    public static final Creator<InitAppBean> CREATOR = new Creator<InitAppBean>() {
        public InitAppBean createFromParcel(Parcel source) {
            return new InitAppBean(source);
        }

        public InitAppBean[] newArray(int size) {
            return new InitAppBean[size];
        }
    };

    public int getNativePaymentEnabled() {
        return nativePaymentEnabled;
    }

    public void setNativePaymentEnabled(int nativePaymentEnabled) {
        this.nativePaymentEnabled = nativePaymentEnabled;
    }
}
