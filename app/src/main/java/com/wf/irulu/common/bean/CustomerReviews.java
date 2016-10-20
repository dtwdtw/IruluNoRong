package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 
 * @描述: 评论信息描述
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:Comment
 * @作者: Yuki
 * @创建时间:2015-7-15 下午5:31:42
 *
 */

public class CustomerReviews implements Parcelable{
//	commentList	array		产品列表
	private ArrayList<Comment> commentList = null;
//	starInfo	json		评论星星信息
	private StarInfo starInfo = null;
//	commentPower	int		评论权限（0：无，1有）
	private int commentPower ;

	@Override
	public String toString() {
		return "CustomerReviews [commentList=" + commentList + ", starInfo=" + starInfo + "]";
	}

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public StarInfo getStarInfo() {
		return starInfo;
	}
 
	public int getCommentPower() {
		return commentPower;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(commentList);
		dest.writeParcelable(this.starInfo, 0);
		dest.writeInt(this.commentPower);
	}

	public CustomerReviews() {
	}

	protected CustomerReviews(Parcel in) {
		this.commentList = in.createTypedArrayList(Comment.CREATOR);
		this.starInfo = in.readParcelable(StarInfo.class.getClassLoader());
		this.commentPower = in.readInt();
	}

	public static final Creator<CustomerReviews> CREATOR = new Creator<CustomerReviews>() {
		public CustomerReviews createFromParcel(Parcel source) {
			return new CustomerReviews(source);
		}

		public CustomerReviews[] newArray(int size) {
			return new CustomerReviews[size];
		}
	};
}