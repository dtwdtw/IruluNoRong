package com.wf.irulu.module.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ImageCompress;
import com.wf.irulu.common.view.photo.PhotoView;
import com.wf.irulu.common.view.photo.PhotoViewAttacher.OnPhotoTapListener;
import com.wf.irulu.module.message.listener.LayoutVisibilityListener;

import java.io.File;
import java.util.ArrayList;

/**
 * @描述: 查看消息的大图Adapter
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.comment.adapter
 * @类名:MessageImagePagerAdapter
 * @作者: 左西杰
 * @创建时间:2015-9-17 下午2:54:06
 * 
 */
public class MessageImagePagerAdapter extends PagerAdapter {
	private LayoutVisibilityListener listener;
	private Picasso mPicasso;
	private Context context;
	private ArrayList<String> photos;
	
	public MessageImagePagerAdapter(Context context, ArrayList<String> photos) {
		this.context = context;
		this.photos = photos;
		mPicasso = IruluApplication.getInstance().getPicasso();
	}
	
	@Override
	public int getCount() {
		if(photos != null){
			return photos.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View imageLayout = LayoutInflater.from(context).inflate(R.layout.row_common_image_pager_layout, null);
		final PhotoView loadView = (PhotoView) imageLayout.findViewById(R.id.image);
		final ProgressBar loadingBar = (ProgressBar) imageLayout.findViewById(R.id.loading);
		int size = photos.size();
		for (int i = 0; i <size; i++) {
			String photosUrl = photos.get(position);
			if(photosUrl.contains("http")){//如果是网络图片URL
				String url = photos.get(position);
				url = url + "?imageView2/4/w/" + ConstantsUtils.DISPLAYW + "/h/" + ConstantsUtils.DISPLAYW;
				RequestCreator requestCreator = mPicasso.load(url).error(R.mipmap.bg_picutre_show).placeholder(R.mipmap.bg_picutre_show);
				requestCreator.noFade();
				requestCreator.into(loadView, new Callback() {
					@Override
					public void onSuccess() {
						loadingBar.setVisibility(View.GONE);
					}

					@Override
					public void onError() {
						loadingBar.setVisibility(View.GONE);
//						UIUtils.getToastShort(context, "failed");
					}
				});
			}else{//否则是本图片URL
				File compressedFile = ImageCompress.getDefaultCompressFile(photos.get(position));
				try {
					ImageCompress.compressPicture(photos.get(position), compressedFile.getAbsolutePath());
				} catch (ImageCompress.CompressException e) {
					e.printStackTrace();
				}
				final RequestCreator requestCreator = mPicasso.load(compressedFile);
				requestCreator.noFade();
				requestCreator.into(loadView, new Callback() {
					@Override
					public void onSuccess() {
						loadingBar.setVisibility(View.GONE);
					}

					@Override
					public void onError() {
						loadingBar.setVisibility(View.GONE);
//						UIUtils.getToastLong(IruluApplication.getInstance(), "");
					}
				});
			}
		}
		
		loadView.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
					((Activity) context).finish();
			}
		});

		view.addView(imageLayout);
		return imageLayout;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
