package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @描述: 订单列表描述
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:Order
 * @作者: Yuki
 * @创建时间:2015-8-13 上午11:48:45
 * 
 */
public class Order implements Parcelable {
	
//	total	int		总数
	private int total;
//	list	array		数据列表
	private ArrayList<OrderDetail> list;

	@Override
	public String toString() {
		return "OrderDetail [total=" + total + ", orderList=" + list + "]";
	}

	public int getCount() {
		return total;
	}

	public ArrayList<OrderDetail> getOrderList() {
		return list;
	}

	public void setOrderList(ArrayList<OrderDetail> list) {
		this.list = list;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.total);
		dest.writeTypedList(list);
	}

	public Order() {
	}

	protected Order(Parcel in) {
		this.total = in.readInt();
		this.list = in.createTypedArrayList(OrderDetail.CREATOR);
	}

	public static final Creator<Order> CREATOR = new Creator<Order>() {
		public Order createFromParcel(Parcel source) {
			return new Order(source);
		}

		public Order[] newArray(int size) {
			return new Order[size];
		}
	};
}
