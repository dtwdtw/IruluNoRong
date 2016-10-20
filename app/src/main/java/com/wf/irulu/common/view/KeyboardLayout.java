package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @描述: 监听软键盘的显示和隐藏
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.view
 * @类名:KeyboardLayout
 * @作者: 左杰
 * @创建时间:2015/11/11 15:48
 */
public class KeyboardLayout extends RelativeLayout {
    public static final byte KEYBOARD_STATE_SHOW = -3;//软键盘打开状态
    public static final byte KEYBOARD_STATE_HIDE = -2;//软键盘隐藏状态
    public static final byte KEYBOARD_STATE_INIT = -1;//软键盘初始化状态

    private boolean mHasInit;
    private boolean mHasKeybord;
    private int mHeight;

    private onKybdsChangeListener mListener;

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLayout(Context context) {
        super(context);
    }

    /**
     * set keyboard state listener
     */
    public void setOnkbdStateListener(onKybdsChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (null != mListener) {
            if (!mHasInit) {
                mHasInit = true;
                mHeight = b;
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            } else {
                mHeight = mHeight < b ? b : mHeight;
            }
            if (mHasInit && mHeight > b) {
                mHasKeybord = true;
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
            }
            if (mHasInit && mHasKeybord && mHeight == b) {
                mHasKeybord = false;
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
            }
        }

    }

    public interface onKybdsChangeListener {
        public void onKeyBoardStateChange(int state);
    }
}
