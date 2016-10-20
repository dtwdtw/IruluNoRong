package com.wf.irulu.module.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.comment.listener.PhotoSelectListener;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @描述: 写评论中选着照片或照相的照片的水平展示的Adapter
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.comment.adapter
 * @类名:WriteCommentImageAdapter
 * @作者: 左西杰
 * @创建时间:2015-9-12 上午9:59:14
 *
 */
public class MyReviewsImageAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater = null;
	private ArrayList<String> image = null;
	private PhotoSelectListener listener = null;
	private Picasso mPicasso = null;
	private int max;
	private int width = 0;

	public MyReviewsImageAdapter(Context mContext, ArrayList<String> image, PhotoSelectListener listener, int max) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
		this.mPicasso = Picasso.with(mContext);
		this.image = image;
		this.listener = listener;
		width = (ConstantsUtils.DISPLAYW - UIUtils.dip2px(20 + 4 * 5) ) / 5;
		this.max = max;
	}
	
	public void setNotifyDataSetChanged(ArrayList<String> image){
		this.image = image;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (image.size() > max) {
			return max;
		}
		return image.size();
	}

	@Override
	public Object getItem(int position) {
		return image.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int current = position;
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_comment_img_write, null);
			mHolder.item_commentlist_iv_imgshow = (ImageView) convertView.findViewById(R.id.item_commentlist_iv_imgshow);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.item_commentlist_iv_imgshow.setMinimumWidth(width);
		mHolder.item_commentlist_iv_imgshow.setMinimumHeight(width);
		
		if ("default".equals(image.get(current))) {
			mHolder.item_commentlist_iv_imgshow.setImageResource(R.mipmap.upload_photo);
		}else{
			if(width >= 0){
				mPicasso.load(new File(image.get(current))).resize(width, width).into(mHolder.item_commentlist_iv_imgshow);
			}
		}
		mHolder.item_commentlist_iv_imgshow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current == image.size() - 1) {
					//添加图片
					listener.selectPics();
				}else{
					//看大图
					listener.fullScreenShow(current);
				}
			}
		});
		return convertView;
	}

	private static class ViewHolder{
		private ImageView item_commentlist_iv_imgshow;
	}
}
