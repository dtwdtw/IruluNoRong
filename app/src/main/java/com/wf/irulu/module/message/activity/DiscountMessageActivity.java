package com.wf.irulu.module.message.activity;

import android.content.Intent;
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
import com.wf.irulu.module.message.adapter.DiscountMessageAdapter;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * @描述: 折扣消息的Activity
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message.activity
 * @类名:DiscountMessageActivity
 * @作者: 左西杰
 * @创建时间:2015-8-11 下午4:57:38
 * 
 */
public class DiscountMessageActivity extends CommonTitleBaseActivity {
	private ListView mListView;
	private List<Message> mLatestMessages;
	private DiscountMessage discountMessage;
	/**用户ID*/
	private String mUid;

	@Override
	protected String setWindowTitle() {
		return "Discount Message";
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_discount_message);
	}

	@Override
	public void initView() {
		mUid = getIntent().getStringExtra("Uid");
		System.out.println("-=-DiscountMessageActivity=uid-=" + mUid);
		mListView = (ListView) findViewById(R.id.discount_message_listview);
		mLatestMessages = RongIMClient.getInstance().getLatestMessages(ConversationType.PRIVATE, mUid, 10);
		if (mLatestMessages != null) {
			System.out.println("latestMessages不为null");
			for (Message message : mLatestMessages) {
				MessageContent messageContent = message.getContent();
				if(messageContent instanceof DiscountMessage){
					discountMessage = (DiscountMessage) messageContent;
					String content = discountMessage.getContent();
					String imageType = discountMessage.getImageType();
					System.out.println("-=-=content-=" + content);
					System.out.println("-=-=type-=" + imageType);
				}else if(messageContent instanceof OrderMessage){
					OrderMessage orderMessage = (OrderMessage) messageContent;
					String content = orderMessage.getContent();
					System.out.println("-=-=content-OrderMessage=" + content);
				}
			}
		}
		DiscountMessageAdapter mAdapter = new DiscountMessageAdapter(this, mLatestMessages);
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
			ILog.i("HomeActivity", "unreadMessageCount=" + unreadMessageCount);
			controller.postTotalUnreadCountCallback(unreadMessageCount);
		}

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MessageContent messageContent = mLatestMessages.get(position).getContent();
				if (messageContent instanceof DiscountMessage) {
					discountMessage = (DiscountMessage) messageContent;
					if (discountMessage != null) {
						int type = discountMessage.getType();
						String detail = discountMessage.getDetail();
						switch (type) {
							case 1://web URL
								String title = discountMessage.getTitle();
								Intent intent = new Intent(DiscountMessageActivity.this, DiscountWebViewActivity.class);
								intent.putExtra("detailUrl", detail);
								intent.putExtra("title", title);
								startActivity(intent);
								break;
							case 2://产品详情
								ProductDetailActivity.startProductDetailActivity(DiscountMessageActivity.this, detail);
								break;
							default:
								break;
						}
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {

	}
}
