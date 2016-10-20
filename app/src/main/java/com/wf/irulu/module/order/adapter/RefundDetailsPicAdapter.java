package com.wf.irulu.module.order.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16.
 */
public class RefundDetailsPicAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> pics;

    public RefundDetailsPicAdapter(Context context, ArrayList<String> pics) {
        this.mContext = context;
        this.pics = pics;
    }

    @Override
    public int getCount() {
        if (pics == null) {
            return 1;
        } else if (pics.size() == 3) {
            return 3;
        } else {
            return pics.size() + 1;
        }
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView iv = new ImageView(mContext);
        iv.setMinimumHeight(100);

        if (pics.size() < 3 && i == pics.size()) {

            IruluApplication.getInstance().getPicasso().load(R.mipmap.upload_photo).error(R.mipmap.bg_picutre_show).into(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });
        } else {
            IruluApplication.getInstance().getPicasso().load(pics.get(i)).error(R.mipmap.bg_picutre_show).into(iv);
        }
        return iv;
    }
}
