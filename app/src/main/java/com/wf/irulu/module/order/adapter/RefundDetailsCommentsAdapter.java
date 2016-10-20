package com.wf.irulu.module.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.RefundDetailsDataBean;
import com.wf.irulu.common.bean.RefundDetailsListBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.module.order.holder.RefundDetailsCommentsHolder;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16. 售后详情回话的adapter
 */
public class RefundDetailsCommentsAdapter extends BaseAdapter {
    private ArrayList<RefundDetailsListBean> list;
    private Context mContext;
    private int num;
    public RefundDetailsCommentsAdapter(Context context, ArrayList<RefundDetailsListBean> list,int num) {
        this.mContext = context;
        this.list = list;
        this.num = num;
    }

    @Override
    public int getCount() {
            return num;
    }

    @Override
    public Object getItem(int i) {
        return (list != null) ? list.get(i) : null;



    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converview, ViewGroup viewGroup) {
        RefundDetailsCommentsHolder holder;
        if (converview == null) {
            holder = new RefundDetailsCommentsHolder();
            converview = View.inflate(mContext, R.layout.item_refund_details_lv, null);
            holder.content = (TextView) converview.findViewById(R.id.item_refund_details_content);
            holder.ll = (LinearLayout) converview.findViewById(R.id.item_refund_details_pic_ll);
            holder.name = (TextView) converview.findViewById(R.id.item_refund_details_name_tv);
            holder.time = (TextView) converview.findViewById(R.id.item_refund_details_time_tv);
            converview.setTag(holder);
        } else {
            holder = (RefundDetailsCommentsHolder) converview.getTag();
        }
        RefundDetailsListBean rdlb = list.get(i);
        holder.content.setText(rdlb.getContent());
        holder.time.setText(DateFormatUtils.getMMddyyyyHHmm(rdlb.getTime() * 1000));
        if (rdlb.getMerchantInfo() == null) {
            String email = CacheUtils.getString(mContext, "email");
            String[] split = email.split("@");
            if (split.length > 0) {
                holder.name.setText("From " + CacheUtils.getString(mContext, "nickname", split[0]));
            }
        } else {
            holder.name.setText(rdlb.getMerchantInfo().getName());
        }
        ArrayList<String> image = rdlb.getImage();
        if (image != null && image.size() > 0) {
            for (int j = 0; j < image.size(); j++) {
                ImageView iv = new ImageView(mContext);
                IruluApplication.getInstance().getPicasso().load(image.get(j) + "?imageView2/3/w/" + UIUtils.dip2px(70) + "/h/" + UIUtils.dip2px(70)).error(R.mipmap.bg_picutre_show).into(iv);
            }
        } else {
            holder.ll.setVisibility(View.GONE);
        }

        return converview;
    }
}
