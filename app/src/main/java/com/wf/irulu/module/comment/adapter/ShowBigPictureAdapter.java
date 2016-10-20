package com.wf.irulu.module.comment.adapter;

import android.content.Context;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.photo.PhotoView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by daniel on 2015/12/2.
 */
public class ShowBigPictureAdapter extends PagerAdapter {
    private ArrayList<String> datas;
    private Context context;

    public ShowBigPictureAdapter(ArrayList<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView iv = new PhotoView(context);
        iv.setMaxHeight(ConstantsUtils.DISPLAYH - UIUtils.dip2px(110));
        IruluApplication.getInstance().getPicasso().load(Uri.fromFile(new File(datas.get(position)))).placeholder(R.mipmap.bg_picutre_show).into(iv);

        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
