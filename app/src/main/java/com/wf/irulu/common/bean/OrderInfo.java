package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @描述: 订单总信息描述
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:OrderInfo
 * @作者: Yuki
 * @创建时间:2015-7-28 下午6:51:15
 * 
 */
public class OrderInfo implements Parcelable {

	private Order all;
	private Order notpay;
	private Order notconfirm;

	@Override
	public String toString() {
		return "OrderInfo [all=" + all + ", notpay=" + notpay + ", notconfirm=" + notconfirm + "]";
	}

	public Order getAll() {
		return all;
	}

	public Order getNotpay() {
		return notpay;
	}

	public Order getNotconfirm() {
		return notconfirm;
	}

	public void setAll(Order all) {
		this.all = all;
	}

	public void setNotpay(Order notpay) {
		this.notpay = notpay;
	}

	public void setNotconfirm(Order notconfirm) {
		this.notconfirm = notconfirm;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.all, 0);
		dest.writeParcelable(this.notpay, 0);
		dest.writeParcelable(this.notconfirm, 0);
	}

	public OrderInfo() {
	}

	protected OrderInfo(Parcel in) {
		this.all = in.readParcelable(Order.class.getClassLoader());
		this.notpay = in.readParcelable(Order.class.getClassLoader());
		this.notconfirm = in.readParcelable(Order.class.getClassLoader());
	}

	public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
		public OrderInfo createFromParcel(Parcel source) {
			return new OrderInfo(source);
		}

		public OrderInfo[] newArray(int size) {
			return new OrderInfo[size];
		}
	};
}
