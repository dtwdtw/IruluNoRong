package com.wf.irulu.component.rongcloud.message;

import io.rong.imlib.MessageTag;
import io.rong.imlib.ipc.utils.ParcelUtils;
import io.rong.imlib.model.MessageContent;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

import com.wf.irulu.common.utils.ILog;

/**
 * @描述: 自定义订单消息
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.component.rongcloud.message
 * @类名:OrderMessage
 * @作者: 左西杰
 * @创建时间:2015-8-11 下午3:37:34
 * 
 */
@MessageTag(value = "WF:Order", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class OrderMessage extends MessageContent {
	private String title;// 标题
	private String orderId;// 订单id
	private String content;// 内容
	private String time;// 时间戳

	/**
	 * 默认构造函数。
	 */
	public OrderMessage() {
		super();
	}

	/**
	 * 构造函数。 该方法将对收到的消息进行解析
	 * 
	 * @param data
	 *            初始化传入的二进制数据。
	 */
	public OrderMessage(byte[] data) {
		String jsonStr = null;

		try {
			jsonStr = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			ILog.e("JSONException", e.getMessage());

		}

		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			System.out.println("-=OrderMessage-=jsonStr"+jsonStr);
			if (jsonObj.has("title")) {
				setTitle(jsonObj.getString("title"));
			}
			if (jsonObj.has("orderId")) {
				setOrderId(jsonObj.getString("orderId"));
			}
			if (jsonObj.has("content")) {
				setContent(jsonObj.getString("content"));
			}
			if (jsonObj.has("time")) {
				setTime(jsonObj.getString("time"));
			}
		} catch (JSONException e) {
			ILog.e("JSONException", e.getMessage());
		}
	}

	/**
	 * 构造函数。 给消息赋值。
	 * 
	 * @param in
	 *            初始化传入的 Parcel。
	 */
	public OrderMessage(Parcel in) {
		setTitle(ParcelUtils.readFromParcel(in));
		setOrderId(ParcelUtils.readFromParcel(in));
		setContent(ParcelUtils.readFromParcel(in));
		setTime(ParcelUtils.readFromParcel(in));
	}

	/**
	 * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
	 */
	public static final Creator<OrderMessage> CREATOR = new Creator<OrderMessage>() {

		@Override
		public OrderMessage createFromParcel(Parcel source) {
			return new OrderMessage(source);
		}

		@Override
		public OrderMessage[] newArray(int size) {
			return new OrderMessage[size];
		}
	};

	/**
	 * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
	 * 
	 * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 将类的数据写入外部提供的 Parcel 中。
	 * 
	 * @param dest
	 *            对象被写入的 Parcel。
	 * @param flags
	 *            对象如何被写入的附加标志。
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		ParcelUtils.writeToParcel(dest, title);
		ParcelUtils.writeToParcel(dest, orderId);
		ParcelUtils.writeToParcel(dest, content);
		ParcelUtils.writeToParcel(dest, time);
	}

	/**
	 * 将本地消息对象序列化为消息数据。 该方法的功能是将消息属性封装成 json 串，再将 json 串转成 byte 数组，该方法会在发消息时调用
	 * 
	 * @return 消息数据。
	 */
	@Override
	public byte[] encode() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("title", getTitle());
			jsonObj.put("orderId", getOrderId());
			jsonObj.put("content", getContent());
			jsonObj.put("time", getTime());

			/*
			 * if (!TextUtils.isEmpty(getExtra())) jsonObj.put("extra",
			 * getExtra());
			 */

		} catch (JSONException e) {
			ILog.e("JSONException", e.getMessage());
		}

		try {
			return jsonObj.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
