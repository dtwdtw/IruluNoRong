package com.wf.irulu.module.homenew1_3_0.DiscoverFragment;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.util.Log;

import com.wf.irulu.R;


/**
 * Created by dtw on 16/4/27.
 */
public class TimeUtils {
    public static SpannableString getSPTime(Context context, String time) {
        Log.e("hellolove",time);
        SpannableString spannableString = new SpannableString(time);
        spannableString.setSpan(new RoundedBackgroundSpan(context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white)) , 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new RoundedBackgroundSpan(context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white)), 2, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new RoundedBackgroundSpan(context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white)), 6, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new RoundedBackgroundSpan(context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white)), 8, 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new RoundedBackgroundSpan(context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white)), 12, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new RoundedBackgroundSpan(context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white)), 14, 15, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(2.1f),1,2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(1.5f),3,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(1.5f),5,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(2.1f),7,8,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(1.5f),9,10,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(1.5f),11,12,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(2.1f),13,14,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orange)),4,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orange)),10,11,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
