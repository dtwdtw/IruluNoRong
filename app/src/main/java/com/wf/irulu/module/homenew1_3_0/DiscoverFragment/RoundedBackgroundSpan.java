package com.wf.irulu.module.homenew1_3_0.DiscoverFragment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * Created by dtw on 16/4/28.
 */
public class RoundedBackgroundSpan extends ReplacementSpan {
    private static int CORNER_RADIUS = 3;
    private int backgroundColor = 0;
    private int textColor = 0;

    public RoundedBackgroundSpan(int backgroundColor, int textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {

        return Math.round(paint.measureText(text, start, end));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                     int bottom, Paint paint) {
        RectF rectF = new RectF(x-5, top+2, x + measureText(paint, text, start, end)+5,bottom-2);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rectF, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}