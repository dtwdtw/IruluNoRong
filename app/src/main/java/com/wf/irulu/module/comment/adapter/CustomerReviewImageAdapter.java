package com.wf.irulu.module.comment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.module.product.activity.ShowPhotosActivity;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.comment.adapter
 * @类名:CustomerReviewImageAdapter
 * @作者: Yuki
 * @创建时间:2015/11/25
 */
public class CustomerReviewImageAdapter extends RecyclerView.Adapter<CustomerReviewImageAdapter.ViewHolder> {

    private ArrayList<String> mImages;
    private Context mContext;
    private int mWidth;
    private Picasso mPicasso;

    public CustomerReviewImageAdapter(ArrayList<String> images, Context context, int width) {
        mImages = images;
        mContext = context;
        mWidth = width;
        mPicasso = IruluApplication.getInstance().getPicasso();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(mWidth, mWidth));
        ViewHolder mHolder = new ViewHolder(imageView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mPicasso.load(StringUtils.getThumbnailImageUrlFormat(mImages.get(position), mWidth)).error(R.mipmap.notify_image_xiaotu).placeholder(R.mipmap.notify_image_xiaotu).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.mImageView);
        holder.mImageView.setOnClickListener(new ImageClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView;
        }
    }

    class ImageClickListener implements View.OnClickListener {

        private int position;

        public ImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ShowPhotosActivity.startShowPhotosActivity((Activity) mContext, mImages, "comment", position);
        }
    }
}
