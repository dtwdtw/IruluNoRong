package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.TrackingInfor;
import com.wf.irulu.module.order.holder.TrackingHolder;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/12.  物流信息的adapter
 */
public class TrackingAdapter extends BaseAdapter {
    private ArrayList<TrackingInfor> datas;
    private Context mContext;

    public TrackingAdapter(ArrayList<TrackingInfor> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int i) {

        return datas == null ? null : datas.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        TrackingHolder tag;
        if (convertview == null) {
            tag = new TrackingHolder();
            convertview = View.inflate(mContext, R.layout.item_tracking_details, null);
            tag.point_iv = (ImageView) convertview.findViewById(R.id.item_tracking_point_iv);
            tag.v = convertview.findViewById(R.id.item_tracking_lines_v);
            tag.destition_tv = (TextView) convertview.findViewById(R.id.item_tracking_destination_tv);
            tag.time_tv = (TextView) convertview.findViewById(R.id.item_tracking_reach_time_tv);
            convertview.setTag(tag);
        } else {
            tag = (TrackingHolder) convertview.getTag();
        }
        if (position == 0) {

            tag.point_iv.setImageResource(R.mipmap.tracking_deyails_round_select);
        } else {
            tag.point_iv.setImageResource(R.mipmap.tracking_deyails_round_normal);
        }
        TrackingInfor ti = datas.get(position);

        tag.destition_tv.setText(ti.getItemValue());
        tag.time_tv.setText(ti.getTime());

        if (position == datas.size() - 1) {
            tag.v.setVisibility(View.GONE);
        }else{
            tag.v.setVisibility(View.VISIBLE);
        }
        return convertview;
    }
}

