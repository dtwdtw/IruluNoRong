package com.wf.irulu.common.view.swipemenulistview;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.R;

import java.util.List;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuView extends LinearLayout implements OnClickListener {

	private SwipeMenuListView mListView;
	private SwipeMenuLayout mLayout;
	private SwipeMenu mMenu;
	private OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SwipeMenuView(SwipeMenu menu, SwipeMenuListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		List<SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private void addItem(SwipeMenuItem item, int id) {
		/************自己添加的代码 start***************/
		int getHeight = item.getHeight();
		int height = LayoutParams.MATCH_PARENT;
		if(getHeight > 0){
			height = getHeight;
		}
		LayoutParams params = new LayoutParams(item.getWidth(),height);
		/************自己添加的代码 start***************/
//		LayoutParams params = new LayoutParams(item.getWidth(),
//				LayoutParams.MATCH_PARENT);
//		LinearLayout parent = new LinearLayout(getContext());
		LinearLayout parent = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_swipe_option,null);
		parent.setId(id);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
		addView(parent);
		ImageView iv = (ImageView) parent.findViewById(R.id.swipe_iv_icon);
		if (item.getIcon() != null) {
//			parent.addView(createIcon(item));
			iv.setImageDrawable(item.getIcon());
			iv.setVisibility(VISIBLE);
		}else{
			iv.setVisibility(View.GONE);
		}
		TextView tv = (TextView) parent.findViewById(R.id.swipe_tv_title);
		if (!TextUtils.isEmpty(item.getTitle())) {
//			parent.addView(createTitle(item));
			tv.setText(item.getTitle());
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(item.getTitleSize());
			tv.setTextColor(item.getTitleColor());
			tv.setVisibility(VISIBLE);
		}else{
			tv.setVisibility(View.GONE);
		}
	}

	private ImageView createIcon(SwipeMenuItem item) {
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}

	private TextView createTitle(SwipeMenuItem item) {
		TextView tv = new TextView(getContext());
		tv.setText(item.getTitle());
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(item.getTitleSize());
		tv.setTextColor(item.getTitleColor());
		return tv;
	}

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
	}

	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setLayout(SwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	public static interface OnSwipeItemClickListener {
		void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
	}
}
