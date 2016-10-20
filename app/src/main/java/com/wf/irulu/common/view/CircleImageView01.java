package com.wf.irulu.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 控件会进行裁剪直到控件刚好容纳下一个圆主要是为了避免外围有点击效果
 * 必须设置头像充满整个控件   scaleType 属性必须是centerCrop
 * 不能存在padding
 * Created by iRULU on 2016/4/22.
 */
public class CircleImageView01 extends ImageView {

    private float radius;
    private float cx, cy;
    private Paint mNormalPaint;
    private Paint mCirclePaint;
    private RectF mRectF;

    public CircleImageView01(Context context) {
        this(context, null);
    }

    public CircleImageView01(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setFilterBitmap(true);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setFilterBitmap(true);
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int value = width > height ? height : width;
        setMeasuredDimension(value, value);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getDrawable() != null || getBackground() != null) {
            if (getPaddingLeft() != 0 || getPaddingTop() != 0 || getPaddingBottom() != 0 || getPaddingRight() != 0) {
                throw new RuntimeException("padding 必须为空");
            }

        }
        mRectF = new RectF(0, 0, right - left, bottom - top);
        cx = (right - left) / 2;
        cy = (bottom - top) / 2;
        radius = cx;


    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null || getBackground() != null) {
            canvas.saveLayer(mRectF, mNormalPaint, Canvas.ALL_SAVE_FLAG);
            canvas.drawCircle(cx, cy, radius, mNormalPaint);
            canvas.saveLayer(mRectF, mCirclePaint, Canvas.ALL_SAVE_FLAG);
        }
        super.onDraw(canvas);
        if (getDrawable() != null || getBackground() != null) {


            canvas.restoreToCount(2);
        }

    }
}
