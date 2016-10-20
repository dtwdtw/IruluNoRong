package com.wf.irulu.common.bean;

import android.os.Parcel;

import java.io.Serializable;

/**
 * @描述: 登录用户信息描述
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.common.bean
 * @类名:LoginUser
 * @作者: 左西杰
 * @创建时间:2015-5-27 下午7:06:27
 * 
 */

public class LoginUser extends UserInfo implements Serializable {

	/** 登录类型*/
	private int loginType; //
	/** 密码*/
	private String password; //
	/** 令牌*/
	private String token; //
	/** 邮箱*/
	private String email; //
	/** 姓*/
	private String lastname;//
	/** 名*/
	private String firstname;//
	/** 注册时间（时间戳）*/
	private int registerDate;//
	/** 注册来源：1 开机注册,2 MOBILE,3 PC*/
	private int froms;//

	/** 用户性别：M男，F女*/
	private String sex;//
	/** 融云令牌*/
	private String rongCloudToken;
	/** 用户类型 1 email 2 facebook 3 twiter*/
	private int userType;
	/** 用户状态，1：正常，2：禁用*/
	private int userStatus;
	/** 生日*/
	private String birthday;

	@Override
	public String toString() {
		return "LoginUser ["+super.toString()+"loginType=" + loginType + ", password=" + password + ", token=" + token + ", email=" + email + ", lastname=" + lastname + ", firstname=" + firstname + ", registerDate=" + registerDate + ", froms=" + froms + ", sex=" + sex + ", rongCloudToken=" + rongCloudToken + ", userType=" + userType + ", userStatus=" + userStatus + ", birthday=" + birthday + "]";
	}

	public LoginUser() {
		super();
		this.birthday = "";
		this.email = "";
		this.password = "";
		this.firstname = "";
		this.lastname = "";
		this.password = "";
		this.sex = "";
		this.token = "";
		this.rongCloudToken = "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(this.loginType);
		dest.writeString(this.password);
		dest.writeString(this.token);
		dest.writeString(this.email);
		dest.writeString(this.lastname);
		dest.writeString(this.firstname);
		dest.writeInt(this.registerDate);
		dest.writeInt(this.froms);
		dest.writeString(this.sex);
		dest.writeString(this.rongCloudToken);
		dest.writeInt(this.userType);
		dest.writeInt(this.userStatus);
		dest.writeString(this.birthday);
	}

	protected LoginUser(Parcel in) {
		super(in);
		this.loginType = in.readInt();
		this.password = in.readString();
		this.token = in.readString();
		this.email = in.readString();
		this.lastname = in.readString();
		this.firstname = in.readString();
		this.registerDate = in.readInt();
		this.froms = in.readInt();
		this.sex = in.readString();
		this.rongCloudToken = in.readString();
		this.userType = in.readInt();
		this.userStatus = in.readInt();
		this.birthday = in.readString();
	}

	public static final Creator<LoginUser> CREATOR = new Creator<LoginUser>() {
		public LoginUser createFromParcel(Parcel source) {
			return new LoginUser(source);
		}

		public LoginUser[] newArray(int size) {
			return new LoginUser[size];
		}
	};

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public int getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(int registerDate) {
		this.registerDate = registerDate;
	}

	public int getFroms() {
		return froms;
	}

	public void setFroms(int froms) {
		this.froms = froms;
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRongCloudToken() {
		return rongCloudToken;
	}

	public void setRongCloudToken(String rongCloudToken) {
		this.rongCloudToken = rongCloudToken;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
