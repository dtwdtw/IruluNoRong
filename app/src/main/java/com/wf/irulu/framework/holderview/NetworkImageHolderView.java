package com.wf.irulu.framework.holderview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;

/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/5/16 下午2:47
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
//            RelativeLayout relativeLayout = new RelativeLayout(context);
//            relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;

//            ProgressBar progressBar = new ProgressBar(context);
//            RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            progressBar.setLayoutParams(progressBarParams);
//
//            relativeLayout.addView(imageView);
//            relativeLayout.addView(progressBar);

//            RequestCreator requestCreator = Picasso.load(urls.get(position));
//
//            if (-1 != errorImageId)
//                requestCreator.error(errorImageId);
//
//            if (-1 != emptyImageId)
//                requestCreator.placeholder(emptyImageId);
//
//            if (!isNeedCache) {
//                requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
//            }
//
//            switch (scaleType) {
//                case Fit:
//                    requestCreator.fit();
//                    break;
//                case CenterCrop:
//                    requestCreator.fit().centerCrop();
//                    break;
//                case CenterInside:
//                    requestCreator.fit().centerInside();
//                    break;
//            }

//            progressBar.setVisibility(View.VISIBLE);
//            requestCreator.into(imageView, new MyCallback(progressBar));

//            relativeLayout.setOnClickListener(new MyOnClickListener(position));

//            return relativeLayout;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.mipmap.notify_image_xiaotu);
        IruluApplication.getInstance().getPicasso().load(data).into(imageView);
    }

//    private final class MyCallback implements Callback {
//        ProgressBar progressBar;
//
//        public MyCallback(ProgressBar progressBar) {
//            this.progressBar = progressBar;
//        }
//
//        @Override
//        public void onSuccess() {
//            this.progressBar.setVisibility(View.INVISIBLE);
//
//        }
//
//        @Override
//        public void onError() {
//            this.progressBar.setVisibility(View.INVISIBLE);
//        }
//
//    }
}