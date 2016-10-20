package com.wf.irulu.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.wf.irulu.R;

/**
 * @描述: 自定义控件重写dispatchKeyEventPreIme方法判断软键处于活动状态，并且用户按下了返回键盘
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.view
 * @类名:ResizeListView
 * @作者: 左杰
 * @创建时间:2015/11/11 22:44
 */
public class ResizeListView extends ListView {
    private Context context;

    public ResizeListView(Context context) {
        super(context);
        this.context = context;
    }

    public ResizeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    EditText editText1 = (EditText) view
                            .findViewById(R.id.shopping_quantity);
                    editText1.clearFocus();
                }
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

}
