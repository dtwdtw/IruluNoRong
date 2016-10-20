package com.daimajia.slider.library;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.LinkedList;
import java.util.List;

/**
 * A slider adapter
 */
public class SliderAdapter extends PagerAdapter {

    public static final String TAG = "SliderAdapter";
    Context context;
    List<String> urls;
    LinkedList<View> cache;
    Picasso picasso;
    int errorImageId = -1;
    int emptyImageId = -1;
    boolean isNeedCache;
    BaseSliderView.ScaleType scaleType;
    OnSliderClickListener listener;

    public SliderAdapter(Context context) {
        this.context = context;
        cache = new LinkedList<>();
    }

    public void setConfigs(Picasso picasso, BaseSliderView.ScaleType scaleType, int errorImageId, int emptyImageId, boolean isNeedCache, OnSliderClickListener listener) {
        this.picasso = picasso;
        this.scaleType = scaleType;
        this.errorImageId = errorImageId;
        this.emptyImageId = emptyImageId;
        this.isNeedCache = isNeedCache;
        this.listener = listener;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
//        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (null == urls || 0 == urls.size())
            return POSITION_UNCHANGED;

        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        RelativeLayout relativeLayout=new RelativeLayout(context);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ProgressBar progressBar = new ProgressBar(context);
        RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(progressBarParams);

        relativeLayout.addView(imageView);
        relativeLayout.addView(progressBar);

        RequestCreator requestCreator = picasso.load(urls.get(position));

        if (-1 != errorImageId)
            requestCreator.error(errorImageId);

        if (-1 != emptyImageId)
            requestCreator.placeholder(emptyImageId);

        if (!isNeedCache) {
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        }

        switch (scaleType) {
            case Fit:
                requestCreator.fit();
                break;
            case CenterCrop:
                requestCreator.fit().centerCrop();
                break;
            case CenterInside:
                requestCreator.fit().centerInside();
                break;
        }

        progressBar.setVisibility(View.VISIBLE);
        requestCreator.into(imageView, new MyCallback(progressBar));

        relativeLayout.setOnClickListener(new MyOnClickListener(position));

        container.addView(relativeLayout);
        return relativeLayout;
    }

    private final class MyCallback implements Callback {
        ProgressBar progressBar;

        public MyCallback(ProgressBar progressBar) {
           this.progressBar=progressBar;
        }

        @Override
        public void onSuccess() {
            this.progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onError() {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private final class MyOnClickListener implements View.OnClickListener {

        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != listener)
                listener.onSliderClick(position);
        }
    }

    public interface OnSliderClickListener {
        void onSliderClick(final int position);
    }
}
