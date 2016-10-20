package com.wf.irulu.module.product.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.module.product.activity.ShowPhotosActivity;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.product.adapter
 * @类名:ImageShowAdapter
 * @作者: Yuki
 * @创建时间:2015/10/28
 */
public class ImageShowAdapter extends PagerAdapter{

    private Context mContext = null;
    private ArrayList<String> mImages = null;
    private Picasso mPicasso = null;
    private int[] images = new int[]{R.mipmap.bg_picutre_show,R.mipmap.notify_image_xiaotu};

    public ImageShowAdapter(Context pContext){
        this.mContext = pContext;
    }

    public ImageShowAdapter(Context pContext,ArrayList<String> pImages){
        this.mContext = pContext;
        this.mImages = pImages;
        this.mPicasso = IruluApplication.getInstance().getPicasso();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return mImages == null ? 0 : mImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setMinimumHeight(ConstantsUtils.DISPLAYW);
        imageView.setMinimumWidth(ConstantsUtils.DISPLAYW);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (!TextUtils.isEmpty(mImages.get(position))) {
//            mPicasso.load(mImages.get(position) + "?imageView2/5/w/" + 500 + "/h/" + 500 + "/q/85").error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).into(imageView);
//			picasso.load(imgs.get(position) + "?imageMogr2/auto-orient/thumbnail/!50p").error(R.drawable.notify_image_xiaotu).placeholder(R.drawable.notify_image_xiaotu).into(imageView);
//            mPicasso.load(mImages.get(position) + "?imageView2/5/w/" + UIUtils.px2dip(ConstantsUtils.DISPLAYW) + "/h/" + UIUtils.px2dip(ConstantsUtils.DISPLAYW)  + "/format/jpg/interlace/1").error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).into(imageView);
            mPicasso.load(StringUtils.getFullImageUrlFormat(mImages.get(position))).error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).into(imageView);
        }
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    ShowPhotosActivity.startShowPhotosActivity((Activity) mContext, mImages, "comment", position);
            }
        });
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
