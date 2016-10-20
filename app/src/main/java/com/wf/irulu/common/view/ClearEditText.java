package com.wf.irulu.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.wf.irulu.R;

public class ClearEditText extends EditText implements OnFocusChangeListener{
	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	private Drawable mSearchIcon;
	private Drawable mSearchBox;
	private boolean isCanClear = true;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		mSearchIcon = getCompoundDrawables()[0];
		if (mClearDrawable == null) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.mipmap.search_delete);
			mClearDrawable = new BitmapDrawable(getResources(), bitmap);
		}
		if (mSearchIcon == null) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.search_nav_search_icon);
			mSearchIcon = new BitmapDrawable(getResources(), bitmap);
		}
		mClearDrawable.setBounds(3, 3, mClearDrawable.getIntrinsicWidth(),mClearDrawable.getIntrinsicHeight());
		mSearchIcon.setBounds(3, 3, mSearchIcon.getIntrinsicWidth(),mSearchIcon.getIntrinsicHeight());
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		setSingleLine();
		setSelectAllOnFocus(true);
		canScrollHorizontally(0);
		setBackgroundResource(R.drawable.cart_add_on_gift);
		setGravity(Gravity.CENTER_VERTICAL);
		setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth()
						- getPaddingRight() - mClearDrawable
						.getIntrinsicWidth())
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable && isCanClear) {
					this.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	public void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(mSearchIcon, getCompoundDrawables()[1], right,getCompoundDrawables()[3]);
		setPadding(5, 0, 15, 0);
	}

	/**
	 * 设置清除图标是否可以点击
	 * @param isClickable
	 */
	public void setClearIconClickable(boolean isClickable) {
		if (mClearDrawable != null){
			isCanClear = false;
		}
	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
