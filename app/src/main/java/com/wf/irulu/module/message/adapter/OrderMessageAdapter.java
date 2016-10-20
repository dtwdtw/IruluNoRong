package com.wf.irulu.module.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.component.rongcloud.message.OrderMessage;

import java.text.SimpleDateFormat;
import java.util.List;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * @描述: 订单消息的Adapter
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message.adapter
 * @类名:OrderMessageAdapter
 * @作者: 左西杰
 * @创建时间:2015-8-12 下午12:00:33
 * 
 */
public class OrderMessageAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	List<Message> messages = null;
	
	public OrderMessageAdapter(Context context,List<Message> list){
		mInflater = LayoutInflater.from(context);
		messages = list;
	}
	
	@Override
	public int getCount() {
		if(messages != null){
			System.out.println("-=messages.size()-="+messages.size());
			return messages.size();
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView != null){
			holder = (ViewHolder) convertView.getTag();
		}else{
//			convertView = mInflater.inflate(R.layout.item_order_message, null);
			convertView = mInflater.inflate(R.layout.item_order_message,parent, false);
			holder = new ViewHolder();
			holder.oder_message_time = (TextView) convertView.findViewById(R.id.oder_message_time);
			holder.oder_message_title = (TextView) convertView.findViewById(R.id.oder_message_title);
			holder.oder_message_content = (TextView) convertView.findViewById(R.id.oder_message_content);
			
			convertView.setTag(holder);
		}
		Message message = messages.get(position);
		MessageContent messageContent = message.getContent();
		if(messageContent instanceof OrderMessage){
			OrderMessage orderMessage = (OrderMessage) messageContent;
			holder.oder_message_time.setText(StringUtils.getTime(orderMessage.getTime()));
			holder.oder_message_title.setText(orderMessage.getTitle());
			holder.oder_message_content.setText(orderMessage.getContent());
		}
		return convertView;
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	private class ViewHolder {
		TextView oder_message_time;
		TextView oder_message_title;
		TextView oder_message_content;
	}
	
	public void refreshAdapter(List<Message> messages){
		messages = messages;
		notifyDataSetChanged();
	}
}


