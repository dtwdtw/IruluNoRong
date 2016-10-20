package com.wf.irulu.module.message.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.MessageBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.ImageCompress;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.RoundImageView;
import com.wf.irulu.module.message.activity.ShowMessagePhotosActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @描述: 消息的Adapter
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message.adapter
 * @类名:MessageAdapter
 * @作者: 左西杰
 * @创建时间:2015-8-5 上午9:47:06
 * 
 */
public class ChatMessageAdapter extends BaseAdapter {
	private final String TAG = getClass().getCanonicalName();
	private int i = 0;
	private LayoutInflater mInflater = null;
	private ArrayList<MessageBean> messages = null;
	private Intent mIntent;
	private Activity activity;
	private ArrayList<String> messageImage;
	private static final int MAX_COUNT = 4;
	public static final int SEND_MESSAGE_TEXT = 0;
	public static final int SEND_MESSAGE_IMAGE = 1;
	public static final int RECEIVE_MESSAGE_TEXT = 2;
	public static final int RECEIVE_MESSAGE_IMAGE = 3;
	private Picasso mPicasso;

	public ChatMessageAdapter(Activity activity,LayoutInflater mInflater, ArrayList<MessageBean> messages,ArrayList<String> messageImage,Intent intent) {
		super();
		mPicasso = IruluApplication.getInstance().getPicasso();
		this.activity = activity;
		this.mInflater = mInflater;
		this.messageImage = messageImage;
		this.messages = messages;
		mIntent = intent;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position).getContent();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return MAX_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return messages.get(position).getType();
	}

	public void setNotifyDataSetChanged(ArrayList<MessageBean> messages){
		this.messages = messages;
		notifyDataSetChanged();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		/** 本地用户头像 */
		String head_url = CacheUtils.getString(IruluApplication.getInstance(), "head_url");
		/** 上传七牛上的用户头像*/
		String netHeadUrl = CacheUtils.getString(IruluApplication.getInstance(), "headjpg");
		String headJpg = mIntent.getStringExtra("headJpg");
		// TimeShowViewHolder time_show_viewHolder = null;
		SendTextViewHolder send_text_viewHolder = null;
		SendImageViewHolder send_image_viewHolder = null;
		ReceiveTextViewHolder receive_text_viewHolder = null;
		ReceiveImageViewHolder receive_image_viewHolder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case SEND_MESSAGE_TEXT:
				convertView = mInflater.inflate(R.layout.item_message_support_sendtext, null);
				send_text_viewHolder = new SendTextViewHolder();
				// 找控件
				send_text_viewHolder.send_message_txt = (TextView) convertView.findViewById(R.id.send_message_txt);
				send_text_viewHolder.send_message_txt_time = (TextView) convertView.findViewById(R.id.send_message_txt_time);
				send_text_viewHolder.send_message_headimg = (RoundImageView) convertView.findViewById(R.id.send_message_headimg);

				convertView.setTag(send_text_viewHolder);
				break;
			case SEND_MESSAGE_IMAGE:
				convertView = mInflater.inflate(R.layout.item_message_support_sendimage, null);
				send_image_viewHolder = new SendImageViewHolder();
				// 找控件
				send_image_viewHolder.send_message_img = (ImageView) convertView.findViewById(R.id.send_message_img);
				send_image_viewHolder.send_message_img_time = (TextView) convertView.findViewById(R.id.send_message_img_time);
				send_image_viewHolder.send_message_headimg = (RoundImageView) convertView.findViewById(R.id.send_message_headimg);

				convertView.setTag(send_image_viewHolder);
				break;
			case RECEIVE_MESSAGE_TEXT:
				convertView = mInflater.inflate(R.layout.item_message_support_receivetext, null);
				receive_text_viewHolder = new ReceiveTextViewHolder();
				// 找控件
				receive_text_viewHolder.receive_message_txt = (TextView) convertView.findViewById(R.id.receive_message_txt);
				receive_text_viewHolder.receive_message_txt_time = (TextView) convertView.findViewById(R.id.receive_message_txt_time);
				receive_text_viewHolder.receive_message_headimg = (RoundImageView) convertView.findViewById(R.id.receive_message_headimg);
				convertView.setTag(receive_text_viewHolder);
				break;
			case RECEIVE_MESSAGE_IMAGE:

				convertView = mInflater.inflate(R.layout.item_message_support_receiveimage, null);
				receive_image_viewHolder = new ReceiveImageViewHolder();
				// 找控件
				receive_image_viewHolder.receive_message_img = (ImageView) convertView.findViewById(R.id.receive_message_img);
				receive_image_viewHolder.receive_message_img_time = (TextView) convertView.findViewById(R.id.receive_message_img_time);
				receive_image_viewHolder.receive_message_headimg = (RoundImageView) convertView.findViewById(R.id.receive_message_headimg);
				convertView.setTag(receive_image_viewHolder);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case SEND_MESSAGE_TEXT:
				send_text_viewHolder = (SendTextViewHolder) convertView.getTag();
				break;
			case SEND_MESSAGE_IMAGE:
				send_image_viewHolder = (SendImageViewHolder) convertView.getTag();
				break;
			case RECEIVE_MESSAGE_TEXT:
				receive_text_viewHolder = (ReceiveTextViewHolder) convertView.getTag();
				break;
			case RECEIVE_MESSAGE_IMAGE:
				receive_image_viewHolder = (ReceiveImageViewHolder) convertView.getTag();
				break;

			default:
				break;
			}
		}
		/**
		 *  为控件赋值
		 */
		switch (type) {
		case SEND_MESSAGE_TEXT:
			if(head_url == null && netHeadUrl == null){
				send_text_viewHolder.send_message_headimg.setImageResource(R.mipmap.headpic);
			}else{
				if(!TextUtils.isEmpty(head_url)){
					imageShow(head_url, send_text_viewHolder.send_message_headimg);// 设置头像
				}else if(!TextUtils.isEmpty(netHeadUrl)){
					imageShow(netHeadUrl, send_text_viewHolder.send_message_headimg);// 设置头像
				}
//				ImageLoader.getInstance().displayImage(send_text_viewHolder.send_message_headimg, new HeadImageInfo(netHeadUrl, head_url));// 设置头像
			}
			if (showTime(position)) {// 显示时间
				send_text_viewHolder.send_message_txt_time.setVisibility(View.VISIBLE);
				MessageBean messageBean = messages.get(position);
				send_text_viewHolder.send_message_txt_time.setText(StringUtils.getFormatTime(String.valueOf(messageBean.getTime())));// 设置发送文本时间
			} else {
				send_text_viewHolder.send_message_txt_time.setVisibility(View.GONE);// 隐藏时间
			}
			send_text_viewHolder.send_message_txt.setText(messages.get(position).getContent());// 设置发送文本
			break;
		case SEND_MESSAGE_IMAGE:
			if(head_url == null && netHeadUrl == null){
				send_image_viewHolder.send_message_headimg.setImageResource(R.mipmap.headpic);
			}else{
				if(!TextUtils.isEmpty(head_url)){
					imageShow(head_url, send_image_viewHolder.send_message_headimg);// 设置头像
				}else if(!TextUtils.isEmpty(netHeadUrl)){
					imageShow(netHeadUrl, send_image_viewHolder.send_message_headimg);// 设置头像
				}
//				ImageLoader.getInstance().displayImage(send_image_viewHolder.send_message_headimg, new HeadImageInfo(netHeadUrl, head_url));// 设置头像
			}
			imageShow(messages.get(position).getContent(),send_image_viewHolder.send_message_img);
//			ImageLoader.getInstance().displayImage(send_image_viewHolder.send_message_img, new MessageImageInfo(null, messages.get(position).getContent()));
			if (showTime(position)) {// 显示时间
				send_image_viewHolder.send_message_img_time.setVisibility(View.VISIBLE);
				MessageBean messageBean = messages.get(position);
				send_image_viewHolder.send_message_img_time.setText(StringUtils.getFormatTime(String.valueOf(messageBean.getTime())));// 设置发送文本时间
			} else {
				send_image_viewHolder.send_message_img_time.setVisibility(View.GONE);// 隐藏时间
			}
			/**
			 * 发送图片的点击事件
			 */
			send_image_viewHolder.send_message_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String imaUrl = messages.get(position).getContent();
					int index = 0;
					int size = messageImage.size();
					for (int i = 0; i <size; i++) {
						String url = messageImage.get(i);
						if(url.equals(imaUrl)){
							index = i;
						}
					}
					ShowMessagePhotosActivity.startShowMessagePhotosActivity(activity, messageImage, index);
				}
			});
			break;
		case RECEIVE_MESSAGE_TEXT:
			imageShow(headJpg, receive_text_viewHolder.receive_message_headimg);// 设置头像
//			ImageLoader.getInstance().displayImage(receive_text_viewHolder.receive_message_headimg, new HeadImageInfo(headJpg, null));// 设置头像
			receive_text_viewHolder.receive_message_txt.setText(messages.get(position).getContent());
			if (showTime(position)) {// 显示时间
				receive_text_viewHolder.receive_message_txt_time.setVisibility(View.VISIBLE);
				MessageBean messageBean = messages.get(position);
				receive_text_viewHolder.receive_message_txt_time.setText(StringUtils.getFormatTime(String.valueOf(messageBean.getTime())));// 设置发送文本时间
			} else {
				receive_text_viewHolder.receive_message_txt_time.setVisibility(View.GONE);// 隐藏时间
			}
			break;
		case RECEIVE_MESSAGE_IMAGE:
			imageShow(headJpg, receive_image_viewHolder.receive_message_headimg);// 设置头像
//			ImageLoader.getInstance().displayImage(receive_image_viewHolder.receive_message_headimg, new HeadImageInfo(headJpg, null));// 设置头像
			
//			if (messages.get(position).getContent().contains("http://")) {
//				imageShow(messages.get(position).getContent(), receive_image_viewHolder.receive_message_img);
////				ImageLoader.getInstance().displayImage(receive_image_viewHolder.receive_message_img, new MessageImageInfo(messages.get(position).getContent(), null));
//			} else {
//				Bitmap image = BitmapFactory.decodeFile(messages.get(position).getContent());// FilePath为图片路径及名字
//				if (image != null) {
//					image = zoomBitmap(image, 300, 300);
//					receive_image_viewHolder.receive_message_img.setImageBitmap(image);
//				}
//			}
			mPicasso.load(netHeadUrl+"?imageView2/0/w/"+ ConstantsUtils.DISPLAYW/3+"/h/"+ ConstantsUtils.DISPLAYW/3).error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).into( receive_image_viewHolder.receive_message_img);
//			imageShow(messages.get(position).getContent(),  receive_image_viewHolder.receive_message_img);
			
			if (showTime(position)) {// 显示时间
				receive_image_viewHolder.receive_message_img_time.setVisibility(View.VISIBLE);
				MessageBean messageBean = messages.get(position);
				receive_image_viewHolder.receive_message_img_time.setText(StringUtils.getFormatTime(String.valueOf(messageBean.getTime())));// 设置发送文本时间
			} else {
				receive_image_viewHolder.receive_message_img_time.setVisibility(View.GONE);// 隐藏时间
			}
			/**
			 * 接收图片的点击事件
			 */
			receive_image_viewHolder.receive_message_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String imaUrl = messages.get(position).getContent();
					int index = 0;
					for (int i = 0; i < messageImage.size(); i++) {
						String url = messageImage.get(i);
						if(url.equals(imaUrl)){
							index = i;
						}
					}
					ShowMessagePhotosActivity.startShowMessagePhotosActivity(activity,messageImage, index);
				}
			});
			break;

		default:
			break;
		}
		return convertView;
	}

	/**
	 * Picasso加载图片
	 * @param netHeadUrl 网络图片地址
	 * @param imageView 控件
	 */
	private void imageShow(String netHeadUrl, ImageView imageView) {
		if(netHeadUrl.contains("http")){
			mPicasso.load(netHeadUrl).error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).into(imageView);
		}else{
			File compressedFile = ImageCompress.getDefaultCompressFile(netHeadUrl);
			try {
				ImageCompress.compressPicture(netHeadUrl, compressedFile.getAbsolutePath());
			} catch (ImageCompress.CompressException e) {
				e.printStackTrace();
			}
			mPicasso.load(compressedFile).error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).into(imageView);
		}
	}

	private static class SendTextViewHolder {
		TextView send_message_txt;
		TextView send_message_txt_time;
		RoundImageView send_message_headimg;
	}

	private static class SendImageViewHolder {
		ImageView send_message_img;
		TextView send_message_img_time;
		RoundImageView send_message_headimg;
	}

	private static class ReceiveTextViewHolder {
		TextView receive_message_txt;
		TextView receive_message_txt_time;
		RoundImageView receive_message_headimg;
	}

	private static class ReceiveImageViewHolder {
		ImageView receive_message_img;
		TextView receive_message_img_time;
		RoundImageView receive_message_headimg;
	}

	/**
	 * 对图片压缩
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// 比例
		float sx = ((float) width) / w;
		float sy = ((float) height) / h;
		// 矩阵
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		// 重新创建
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

		return bitmap;
	}

	/**
	 * 设置显示时间(大于5分钟就显示)
	 * @param position
	 * @return
	 */
	public boolean showTime(int position) {
		if (position == 0) {
			return true;
		} 
		long time = messages.get(position).getTime();
		long time2 = messages.get(position - 1).getTime();
		if (time - time2 > (5 * 60 * 1000)) {// 两个时间间隔大于为5分钟
			ILog.i(TAG,"显示了");
			return true;
		}
		ILog.i(TAG,"不显示了");
		return false;
	}
}
