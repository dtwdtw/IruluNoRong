package com.wf.irulu.module.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.PhotoDirInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * @ClassName: ImageListAdapter
 * @Description: TODO()
 * @author tony.liu
 */

public class ImageListAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private Context mContext;
	private ArrayList<PhotoDirInfo> listDir;
	private Picasso mPicasso;

	public ImageListAdapter(Context context, ArrayList<PhotoDirInfo> objects) {
		super();
		this.mContext = context;
		listDir = objects;
		mPicasso = Picasso.with(mContext);
		inflater = LayoutInflater.from(mContext);
	}

	/**
	 * @return
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return listDir != null ? listDir.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 * @see android.widget.ArrayAdapter#getView(int, View,
	 *      ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoDirInfo localPhotoDirInfo = listDir.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_local_imagelist_layout,parent, false);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iamge_icon);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.iamge_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(localPhotoDirInfo.getDirName() + "("+ localPhotoDirInfo.getPicCount() + ")");
		String path = localPhotoDirInfo.getFirstPicPath();
		mPicasso.load(new File(path)).resize(100, 100).into(viewHolder.imageView);
		return convertView;
	}

	private class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

}
