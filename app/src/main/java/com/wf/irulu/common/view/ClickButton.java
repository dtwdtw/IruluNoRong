package com.wf.irulu.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.wf.irulu.R;


/**
 * Created by iRULU on 2016/4/19.
 */
public class ClickButton extends Button {


    private void initSeletorDrawable(AttributeSet attrs) {

        TypedArray vTypedArray = getResources().obtainAttributes(attrs, R.styleable.ClickButton);
        Drawable normalDrawable = vTypedArray.getDrawable(R.styleable.ClickButton_normalDrawable);
        Drawable clickedDrawable = vTypedArray.getDrawable(R.styleable.ClickButton_clickedDrawable);
        Drawable disableDrawble = vTypedArray.getDrawable(R.styleable.ClickButton_disableDrawble);
        vTypedArray.recycle();

        if (normalDrawable == null && clickedDrawable == null && disableDrawble == null) {
            return;
        }
        StateListDrawable vStateListDrawable = new StateListDrawable();
        if (clickedDrawable != null) {
            vStateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, clickedDrawable);
        }

        if (disableDrawble != null) {
            vStateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawble);
        }
        if (normalDrawable != null) {
            vStateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        }


        setBackgroundDrawable(vStateListDrawable);
    }

    public ClickButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSeletorDrawable(attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = MotionEvent.actionToString(event.getAction());
        Log.d("TB", "action=" + action);
        return super.onTouchEvent(event);
    }
}
