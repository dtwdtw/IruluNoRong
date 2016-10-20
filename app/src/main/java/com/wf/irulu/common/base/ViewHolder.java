package com.wf.irulu.common.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;

/**
 * 通用的ViewHolder 
 * 
 * @author owen
 */
public class ViewHolder {

	/**
	 * 存储item中所用控件引用的容器
	 * 
	 * Key - 资源ID 
	 * Value - 控件的引用
	 */
	private SparseArray<View> views = null;

	private View convertView = null;

	private int position = 0;

	/**
	 * 私有化的构造函数，有类内部来管理该实例
	 * 
	 * @param context 上下文对象
	 * @param itemLayoutResId item的布局文件的资源ID
	 * @param position BaseAdapter.getView()的传入参数
	 * @param parent BaseAdapter.getView()的传入参数
	 */
	private ViewHolder(Context context, int itemLayoutResId, int position, ViewGroup parent) {
		this.views = new SparseArray<View>();
		this.position = position;
		this.convertView = LayoutInflater.from(context).inflate(itemLayoutResId, parent, false);
		
		convertView.setTag(this);
	}

	/**
	 * 得到一个ViewHolder对象
	 * 
	 * @param context 上下文对象
	 * @param itemLayoutResId item的布局文件的资源ID
	 * @param position BaseAdapter.getView()的传入参数
	 * @param convertView BaseAdapter.getView()的传入参数
	 * @param parent BaseAdapter.getView()的传入参数
	 * @return 一个ViewHolder对象
	 */
	public static ViewHolder getViewHolder(Context context, int itemLayoutResId, int position,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			return new ViewHolder(context, itemLayoutResId, position, parent);
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.position = position; // 这里要更新一下position，因为position一直发生变化
			return viewHolder;
		}		
	}

	public View getConvertView() {
		return convertView;
	}

	/**
	 * 【核心部分】
	 * 根据控件的资源ID，获取控件
	 * 
	 * @param viewResId 控件的资源ID
	 * @return 控件的引用
	 */
	public <T extends View> T getView(int viewResId) {
		View view = views.get(viewResId);
		
		if (view == null) {
			view = convertView.findViewById(viewResId);
			views.put(viewResId, view);
		}
		
		return (T) view;
	}
	
	/**
	 * 设置TextView的值
	 * @param viewResId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewResId, String text) {
		TextView tv = getView(viewResId);
		tv.setText(text);
		
		return this;
	}
	
	public ViewHolder setImageResource(int viewResId, int resId) {
		ImageView iv = getView(viewResId);
		iv.setImageResource(resId);
		
		return this;
	} 
	
	/** Add an action to set the image of an image view. Can be called multiple times. */
	public ViewHolder setImageBitmap(int viewResId, Bitmap bitmap) {
		ImageView iv = getView(viewResId);
		iv.setImageBitmap(bitmap);
		
		return this;
	} 
	
	public ViewHolder setBitmapUtils(int viewResId, String uri) {
		ImageView iv = getView(viewResId);
		BitmapUtils bitmapUtils = new BitmapUtils(IruluApplication.getInstance());
		bitmapUtils.display(iv, uri);
		return this;
	} 
	
	public ViewHolder setBackgroundResource(int viewResId, int resId) {
		ImageView iv = getView(viewResId);
		iv.setBackgroundResource(resId);
		return this;
	} 
	
	public ViewHolder setVisibility(int viewResId,int visibility){
		View view = getView(viewResId);
		view.setVisibility(visibility);
		return this;
	}
	
	/**
     * Will set the image of an ImageView from a drawable.
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseAdapterHelper for chaining.
     */
    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }
    
    public ViewHolder setImageUrl(Context context,int viewId, String imageUrl) {
        ImageView view = getView(viewId);
//        ImageLoader.getInstance().displayImage(view,new ProductHomePageInfo(imageUrl,null));
        if(!TextUtils.isEmpty(imageUrl))
        	Picasso.with(context).load(imageUrl).into(view);
        return this;
    }
	
	
	 /**
     * Sets the on click listener of the view.
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The BaseAdapterHelper for chaining.
     */
    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
    
    public ViewHolder setLayoutParams(int viewId, AbsListView.LayoutParams lp) {
    	View view = getView(viewId);
    	view.setLayoutParams(lp);
    	return this;
    }
}
