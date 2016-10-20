package com.wf.irulu.common.view;

import android.content.Context;;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dtw on 16/4/14.
 */
public class Triangle extends View {

    Paint p;
    int width;
    int height;

    public Triangle(Context context) {
        super(context);
        p=new Paint();
        Log.v("hellolove","hellolove");
    }

    public Triangle(Context context, AttributeSet attr){
        super(context,attr);
        p=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        width=MeasureSpec.getSize(widthMeasureSpec);
        height=MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.lineTo(width, 0);
        path.close();
        canvas.drawPath(path, p);
    }
}
