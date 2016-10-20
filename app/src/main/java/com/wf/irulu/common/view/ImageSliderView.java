package com.wf.irulu.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;

/**
 * Created by ximoon on 15/12/23.
 */
public class ImageSliderView extends BaseSliderView {

    public ImageSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_detail_image_show,null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ConstantsUtils.DISPLAYW,ConstantsUtils.DISPLAYW));
        bindEventAndShow(view,(ImageView) view.findViewById(R.id.detail_iv_image));
        return view;
    }
}
