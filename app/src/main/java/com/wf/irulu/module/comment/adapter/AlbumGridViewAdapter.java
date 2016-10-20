package com.wf.irulu.module.comment.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.squareup.picasso.Picasso;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;

import java.io.File;
import java.util.ArrayList;

/** 
 * @ClassName: AlbumGridViewAdapter 
 * @Description: TODO() 
 * @author tony.liu
 */

public class AlbumGridViewAdapter extends BaseAdapter implements OnClickListener {
	private Picasso mPicasso;
	private Context mContext;
	private ArrayList<String> dataList;
	private ArrayList<String> selectedDataList;
	private DisplayMetrics dm;

	public AlbumGridViewAdapter(Context c, ArrayList<String> dataList, ArrayList<String> selectedDataList) {
		mContext = c;
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		mPicasso = Picasso.with(c);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.row_local_select_imageview_layout, parent, false);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.selectimage_iv_show);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.selectimage_cb_sure);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (dataList != null && dataList.size() > position){
			path = dataList.get(position);
		}else{
			path = "camera_default";
		}
		
		int width = (ConstantsUtils.DISPLAYW - 10 - 5 * 3) / 4;
		LayoutParams params = new LayoutParams(width,width);
		viewHolder.imageView.setLayoutParams(params);
		
		if (path.contains("default")) {
			viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			mPicasso.load(new File(path)).resize(100, 100).into(viewHolder.imageView);
		}
		viewHolder.checkBox.setTag(position);
		viewHolder.checkBox.setOnClickListener(this);
		viewHolder.imageView.setOnClickListener(new OnClick(viewHolder.checkBox,viewHolder));
		if (isInSelectedDataList(path)) {
			viewHolder.checkBox.setChecked(true);
		} else {
			viewHolder.checkBox.setChecked(false);
		}
		return convertView;
	}

	private boolean isInSelectedDataList(String selectedString) {
		for (int i = 0; i < selectedDataList.size(); i++) {
			if (selectedDataList.get(i).equals(selectedString)) {
				return true;
			}
		}
		return false;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	@Override
	public void onClick(View view) {
		if (view instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) view;
			int position = (Integer) checkBox.getTag();
			if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
				mOnItemClickListener.onItemClick(checkBox, position,dataList.get(position), checkBox.isChecked());
			}
		}
	}

	private OnItemClickListener mOnItemClickListener;
	
	private class OnClick implements OnClickListener{
		private CheckBox checkBox;
		private ViewHolder viewHolder;
		public OnClick(CheckBox checkBox,ViewHolder viewHolder){
			this.checkBox = checkBox;
			this.viewHolder = viewHolder;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) checkBox.getTag();
			checkBox.setChecked(checkBox.isChecked()?false:true);
			if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
				mOnItemClickListener.onItemClick(checkBox, position,dataList.get(position), checkBox.isChecked());
				notifyDataSetChanged();
			}
		}
		
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(CheckBox view, int position, String path, boolean isChecked);
	}

}
