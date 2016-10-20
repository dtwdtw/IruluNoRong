package com.wf.irulu.module.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.MessageIds;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.CircleImageView;
import com.wf.irulu.component.rongcloud.message.DiscountMessage;
import com.wf.irulu.component.rongcloud.message.OrderMessage;
import com.wf.irulu.logic.IruluController;

import java.util.List;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * @描述: 会话列表的Adapter
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.message.adapter
 * @类名:MessageAdapter
 * @作者: 左杰
 * @创建时间:2015/11/23 17:59
 */
public class MessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<Conversation> mConversationList;
    private List<MessageIds> messageIds;
    private Picasso mPicasso;
    private int width = 0;

    public MessageAdapter(Context context,List<Conversation> list,List<MessageIds> messageIds){
        mInflater = LayoutInflater.from(context);
        mConversationList = list;
        this.messageIds = messageIds;
        mPicasso = IruluApplication.getInstance().getPicasso();
//        this.width = UIUtils.px2dip(90);//90转换成dip后的值为60
        this.width = ConstantsUtils.DISPLAYW*10/75;
    }

    @Override
    public int getCount() {
        if(mConversationList != null){
            return mConversationList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_message,null);
            holder = new ViewHolder();
            holder.message_img = (CircleImageView) convertView.findViewById(R.id.message_img);
            holder.message_name = (TextView) convertView.findViewById(R.id.message_name);
            holder.message_content = (TextView) convertView.findViewById(R.id.message_content);
            holder.message_tv_supportnum = (TextView) convertView.findViewById(R.id.message_tv_supportnum);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Conversation conversation = mConversationList.get(position);
        boolean top = conversation.isTop();
        System.out.println("-=position="+position+"获取置顶状态。"+top);
        String targetId = conversation.getTargetId();
        String chatUid = messageIds.get(0).getUid()+"";
        String orderUid = messageIds.get(1).getUid()+"";
        String discountUid = messageIds.get(2).getUid()+"";
        if(targetId.equals(chatUid)){
            String headjpg = messageIds.get(0).getHeadjpg();
            ILog.i("HomeActivity", "消息头像url=" + headjpg);
            if(headjpg != null){
                String str = "?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75";
                mPicasso.load(headjpg+str).placeholder(R.mipmap.notify_irulu_support).error(R.mipmap.notify_irulu_support).into(holder.message_img);
            }else
                holder.message_img.setImageResource(R.mipmap.notify_irulu_support);
            holder.message_name.setText( messageIds.get(0).getNickname());
        }else if(targetId.equals(orderUid)){
            String headjpg = messageIds.get(1).getHeadjpg();
            if(headjpg != null){
                mPicasso.load(headjpg+"?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75").placeholder(R.mipmap.notify_order_message).error(R.mipmap.notify_order_message).into(holder.message_img);
            }else
                holder.message_img.setImageResource(R.mipmap.notify_order_message);
            holder.message_name.setText(messageIds.get(1).getNickname());
        }else if(targetId.equals(discountUid)){
            String headjpg = messageIds.get(2).getHeadjpg();
            if(headjpg != null){
                mPicasso.load(headjpg+"?imageView2/0/w/" + width + "/h/" + width + "/format/jpg/interlace/1/q/75").placeholder(R.mipmap.notify_discount_messages).error(R.mipmap.notify_discount_messages).into(holder.message_img);
            }else
                holder.message_img.setImageResource(R.mipmap.notify_discount_messages);
            holder.message_name.setText(messageIds.get(2).getNickname());
        }else{
            holder.message_img.setImageResource(R.mipmap.notify_irulu_support);
            holder.message_name.setText(targetId);
        }

        conversation = RongIMClient.getInstance().getConversation(Conversation.ConversationType.PRIVATE,targetId);
        if(conversation != null){
            int unreadMessageCount = conversation.getUnreadMessageCount();//获取未读消息数。
            if(unreadMessageCount>0){
                holder.message_tv_supportnum.setVisibility(View.VISIBLE);
                holder.message_tv_supportnum.setText(unreadMessageCount+"");
            }else if(unreadMessageCount > 99){
                holder.message_tv_supportnum.setVisibility(View.VISIBLE);
                holder.message_tv_supportnum.setText("99+");
            }else{
                holder.message_tv_supportnum.setVisibility(View.GONE);
            }
            MessageContent latestMessage = conversation.getLatestMessage();//获取本会话最后一条消息。
            if(latestMessage instanceof TextMessage){
                TextMessage textMessage = (TextMessage) latestMessage;
                String content = textMessage.getContent();
                System.out.println("-=content-="+content);
                holder.message_content.setText(content);
            }else if(latestMessage instanceof ImageMessage){
                holder.message_content.setText("[图片]");
            }else if(latestMessage instanceof OrderMessage){
                OrderMessage orderMessage = (OrderMessage) latestMessage;
                String content = orderMessage.getContent();
                holder.message_content.setText(content);
            }else if(latestMessage instanceof DiscountMessage){
                DiscountMessage discountMessage = (DiscountMessage) latestMessage;
                String content = discountMessage.getContent();
                holder.message_content.setText(content);
            }
        }
        return convertView;
    }

    public void refreshAdapter(List<Conversation> conversations){
        mConversationList = conversations;
        notifyDataSetChanged();
    }

    class ViewHolder{
        CircleImageView message_img;
        TextView message_name;
        TextView message_content;
        TextView message_tv_supportnum;
    }
}
