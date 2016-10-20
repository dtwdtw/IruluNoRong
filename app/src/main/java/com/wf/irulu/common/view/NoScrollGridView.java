package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @描述: 使ScrollView适应ListView的效果
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.view
 * @类名:NoScrollGridView
 * @作者: 左西杰
 * @创建时间:2015-7-8 上午9:28:53
 * 
 */
public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context) {
		super(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	} 
}
