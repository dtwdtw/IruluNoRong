package com.wf.irulu.module.comment.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.ImageCompress;
import com.wf.irulu.common.view.photo.PhotoView;
import com.wf.irulu.common.view.photo.PhotoViewAttacher.OnPhotoTapListener;
import com.wf.irulu.module.comment.activity.ShowPhotosActivity;
import com.wf.irulu.module.comment.listener.LayoutVisibilityListener;

import java.io.File;
import java.util.ArrayList;

/***
 * 查看大图适配器
 *
 * @author tony.liu
 */
public class ImagePagerAdapter extends PagerAdapter {

    private LayoutVisibilityListener listener;
    private Picasso mPicasso;
    private Context context;
    private ArrayList<String> photos;
    private String type;
    private final String TAG = getClass().getCanonicalName();

    public ImagePagerAdapter(Context context, ArrayList<String> photos, String type) {
        this.context = context;
        this.photos = photos;
        this.type = type;
//		mPicasso = PicassoTrustAll.getInstance(context);
        mPicasso = Picasso.with(context);
        ILog.e(TAG, photos.toString());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        // TODO
    }

    @Override
    public int getCount() {
        if (photos != null) {
            if (photos.size() > 0 && "default".equals(photos.get(photos.size() - 1))) {
                photos.remove(photos.size() - 1);
            }
            return photos.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.row_common_image_pager_layout, null);
        final PhotoView loadView = (PhotoView) imageLayout.findViewById(R.id.image);
        final ProgressBar loadingBar = (ProgressBar) imageLayout.findViewById(R.id.loading);
        // ImageView mDownloadIV = (ImageView)
        // imageLayout.findViewById(R.id.download_iv);
        if ("local".equals(type)) {
            // 图证临时图片
            // mDownloadIV.setImageResource(R.drawable.icon_del_image_prove);
            // loadingBar.setVisibility(View.GONE);
            File compressedFile = ImageCompress.getDefaultCompressFile(photos.get(position));
            ILog.i("WriteCommentActivity", "见旅途===" + compressedFile.getAbsolutePath());
            try {
//                ImageCompress.compressPicture(photos.get(position), compressedFile.getAbsolutePath());
                ILog.i("WriteCommentActivity", "宽=" + ConstantsUtils.DISPLAYW + "高" + ConstantsUtils.DISPLAYH);
                 ImageCompress.compress(photos.get(position), compressedFile.getAbsolutePath(), ConstantsUtils.DISPLAYW, ConstantsUtils.DISPLAYH);
            } catch (ImageCompress.CompressException e) {
                e.printStackTrace();
            }
            final RequestCreator requestCreator = mPicasso.load(compressedFile);
            // String compressBitmap = BitmapHelper.compressBitmap(context,

            // photos.get(position), 768, 1024, false);
            // final RequestCreator requestCreator = mPicasso.load(new
            // File(compressBitmap));
            requestCreator.noFade();
            requestCreator.into(loadView, new Callback() {
                @Override
                public void onSuccess() {
                    loadingBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    loadingBar.setVisibility(View.GONE);
                }
            });
        } else {
            String url = photos.get(position);
//			url = url + "?imageView2/4/w/" + ConstantsUtils.DISPLAYW + "/h/" + ConstantsUtils.DISPLAYW  + "/q/85";
            url = url + "?imageView2/4/w/" + ConstantsUtils.DISPLAYW + "/h/" + ConstantsUtils.DISPLAYW + "/format/jpg/interlace/1";
//			url = url + "?imageMogr2/auto-orient/thumbnail/!75p";
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
                }
            });
        }
        loadView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (!"local".equals(type)) {
                    ((ShowPhotosActivity) context).finish();
                    return;
                }
                listener.layoutVisibility();
            }
        });

        view.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public int getItemPosition(Object object) {
        if (photos.size() > 0) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public ArrayList<String> getImages() {
        return photos;
    }

    public void setOnLayoutVisibilityListener(LayoutVisibilityListener listener) {
        this.listener = listener;
    }

    public void setNotifyDataSetChanged(ArrayList<String> photos, String type) {
        this.photos = photos;
        this.type = type;
        notifyDataSetChanged();
    }
}
