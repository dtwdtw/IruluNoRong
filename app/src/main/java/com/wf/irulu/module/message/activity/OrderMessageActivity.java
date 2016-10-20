package com.wf.irulu.module.message.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.component.rongcloud.Listener.ReceiveMessageListener;
import com.wf.irulu.component.rongcloud.message.DiscountMessage;
import com.wf.irulu.component.rongcloud.message.OrderMessage;
import com.wf.irulu.module.message.adapter.OrderMessageAdapter;
import com.wf.irulu.module.order.activity.OrderDetailActivity;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * @描述: 订单消息的Activity
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message.activity
 * @类名:OrderMessageActivity
 * @作者: 左西杰
 * @创建时间:2015-8-11 下午4:55:47
 * 
 */
public class OrderMessageActivity extends CommonTitleBaseActivity {

	private ListView mListView;
	/**用户ID*/
	private String mUid;
	private List<Message> mLatestMessages;

	@Override
	protected String setWindowTitle() {
		return "Order Message";
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_order_message);
	}

	@Override
	public void initView() {
		mListView = (ListView)findViewById(R.id.order_message_listview);
		mUid = getIntent().getStringExtra("Uid");
		System.out.println("-=mUid()-="+mUid);
		mLatestMessages = RongIMClient.getInstance().getLatestMessages(ConversationType.PRIVATE, mUid, 10);
		/*if(mLatestMessages != null ){
			System.out.println("latestMessages不为null");
			for (Message message : mLatestMessages) {
				MessageContent messageContent = message.getContent();
				OrderMessage orderMessage = (OrderMessage) messageContent;
				String content = orderMessage.getContent();
				System.out.println("-=-=content-="+content);
			}
		}*/
		OrderMessageAdapter mAdapter = new OrderMessageAdapter(OrderMessageActivity.this,mLatestMessages);
		mListView.setAdapter(mAdapter);
		ReceiveMessageListener.getInstance().setListView(mListView);
		ReceiveMessageListener.getInstance().setMessageArrays(mLatestMessages);
		ReceiveMessageListener.getInstance().setAdapterInstance(mAdapter);
		mListView.setSelection(mAdapter.getCount() - 1);// 一进来默认显示最新的消息
	}

	@Override
	public void initData() {
		/**
		 * 获取未读消息数,好刷新侧栏的未读消息个数
		 */
		if(null != RongIMClient.getInstance()){
			Conversation conversation = RongIMClient.getInstance().getConversation(ConversationType.PRIVATE,mUid);
			int unreadMessageCount = conversation.getUnreadMessageCount();//获取未读消息数。
			controller.postTotalUnreadCountCallback(unreadMessageCount);
		}

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MessageContent messageContent = mLatestMessages.get(position).getContent();
				if (messageContent instanceof OrderMessage) {
					OrderMessage orderMessage = (OrderMessage) messageContent;
					if(null != orderMessage){
						OrderDetailActivity.startOrderDetailActivity(OrderMessageActivity.this, orderMessage.getOrderId());
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {

	}
}
