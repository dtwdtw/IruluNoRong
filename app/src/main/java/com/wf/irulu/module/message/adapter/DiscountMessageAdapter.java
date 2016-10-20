package com.wf.irulu.module.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.component.rongcloud.message.DiscountMessage;

import java.util.List;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * @描述: 折扣消息的Adapter
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message.adapter
 * @类名:DiscountMessageAdapter
 * @作者: 左西杰
 * @创建时间:2015-8-12 下午4:22:19
 */
public class DiscountMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    List<Message> messages = null;
    private DiscountMessage discountMessage;
    private Picasso mPicasso;
    private String imageType;

    public DiscountMessageAdapter(Context context, List<Message> list) {
        mPicasso = IruluApplication.getInstance().getPicasso();
        mInflater = LayoutInflater.from(context);
        messages = list;
    }

    @Override
    public int getCount() {
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        MessageContent messageContent = message.getContent();
        if (messageContent instanceof DiscountMessage) {
            discountMessage = (DiscountMessage) messageContent;
            String imageTypeStr = discountMessage.getImageType();
            int imageType = Integer.parseInt(imageTypeStr) - 1;
            return imageType;//此返回的值应小与getViewTypeCount()方法返回的值
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            if (getItemViewType(position) == 1) {
                System.out.println("小图");//小图
                convertView = mInflater.inflate(R.layout.item_discount_message_thumbnails, parent, false);
            } else {
                System.out.println("大图");//大图
                convertView = mInflater.inflate(R.layout.item_discount_message_largermap, parent, false);
            }
            holder = new ViewHolder();
            holder.discount_message_time = (TextView) convertView.findViewById(R.id.discount_message_time);
            holder.discount_message_title = (TextView) convertView.findViewById(R.id.discount_message_title);
            holder.discount_message_img = (ImageView) convertView.findViewById(R.id.discount_message_img);
            holder.discount_message_content = (TextView) convertView.findViewById(R.id.discount_message_content);
            convertView.setTag(holder);
        }

        if (null != discountMessage) {
            holder.discount_message_time.setText(StringUtils.getTime(discountMessage.getTime()));
            holder.discount_message_title.setText(discountMessage.getTitle());
            holder.discount_message_content.setText(discountMessage.getContent());
            System.out.println("-=discountMessage.getImageUrl()-=" + discountMessage.getImageUrl());

            int width = ConstantsUtils.DISPLAYW *63/75;//大图的宽
            String imageType = discountMessage.getImageType();
            if (imageType != null && imageType.equals("1")) {//大图
                ILog.i("zxj", "大图");

                int height = width * 294 / 630;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                holder.discount_message_img.setLayoutParams(layoutParams);

                mPicasso.load(discountMessage.getImageUrl() + "?imageView2/3/w/" + (ConstantsUtils.DISPLAYW - UIUtils.dip2px(30)) + "/h/" + (ConstantsUtils.DISPLAYW - UIUtils.dip2px(30)) / 2 + "/interlace/1").into(holder.discount_message_img);
            } else if (imageType != null && imageType.equals("2")) {//小图
                ILog.i("zxj", "小图");

                int height = (ConstantsUtils.DISPLAYW - 120) * 10 / 63;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height, height);
                holder.discount_message_img.setLayoutParams(layoutParams);

                mPicasso.load(discountMessage.getImageUrl() + "?imageView2/3/w/" + UIUtils.dip2px(100) + "/h/" + UIUtils.dip2px(100) + "/interlace/1").into(holder.discount_message_img);
            }


        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView discount_message_time;
        TextView discount_message_title;
        ImageView discount_message_img;
        TextView discount_message_content;
        LinearLayout discount_message_detail;
    }

    public void refreshAdapter(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

}
