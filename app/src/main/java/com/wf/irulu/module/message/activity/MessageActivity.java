package com.wf.irulu.module.message.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.InitAppBean;
import com.wf.irulu.common.bean.MessageIds;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenu;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuCreator;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuItem;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuListView;
import com.wf.irulu.component.rongcloud.Listener.ReceiveMessageListener;
import com.wf.irulu.logic.listener.TotalUnreadCountListener;
import com.wf.irulu.module.message.adapter.MessageAdapter;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @描述: 消息会话列表
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.message.activity
 * @类名:MessageActivity
 * @作者: 左杰
 * @创建时间:2015/11/23 17:38
 */
public class MessageActivity extends CommonTitleBaseActivity implements TotalUnreadCountListener {
    private final String TAG = getClass().getCanonicalName();
    private List<Conversation> mConversationList;
    private SwipeMenuListView mSwipeMenuListView;
    private String mUserId;//用户ID
    private MessageAdapter mMessageAdapter;
    private List<MessageIds> mMessageIds;

    @Override
    protected String setWindowTitle() {
        return "Messages";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    public void initView() {
        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.message_list);
        RelativeLayout messageEmptyLayout = (RelativeLayout)findViewById(R.id.message_empty_layout);
        mSwipeMenuListView.setEmptyView(messageEmptyLayout);
        mUserId = ReceiveMessageListener.getInstance().userId;
        mConversationList = RongIMClient.getInstance().getConversationList();

        String initAppBeanStr = CacheUtils.getString(this, "initAppBean");
        Gson gson = new Gson();
        InitAppBean initAppBean = gson.fromJson(initAppBeanStr, InitAppBean.class);
        mMessageIds = initAppBean.getMessageIds();

        controller.registTotalUnreadCountListener(this);
    }

    @Override
    public void initData() {
        //创建一个SwipeMenuCreator供ListView使用
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建一个侧滑菜单
                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                //给该侧滑菜单设置背景
                delItem.setBackground(new ColorDrawable(getResources().getColor(R.color.wish_delete)));
                int i = UIUtils.dip2px(70);
                float dimension = getResources().getDimension(R.dimen.swipe_menu_width);
                //设置宽度
                delItem.setWidth((int)dimension);
                // 设置图片
                delItem.setIcon(R.drawable.wish_delete_selector);
                menu.addMenuItem(delItem);
            }
        };

        mSwipeMenuListView.setMenuCreator(creator);

        //侧滑菜单的相应事件
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//				List<Conversation> conversationList = RongIMClient.getInstance().getConversationList();
                mConversationList = RongIMClient.getInstance().getConversationList();
                Conversation conversation = mConversationList.get(position);
                String targetId = conversation.getTargetId();
                mConversationList.remove(position);
                mMessageAdapter.refreshAdapter(mConversationList);

                /**
                 * 根据会话类型，清空某一会话的所有聊天消息记录，回调方式获取清空是否成功。
                 */
                RongIMClient.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean arg0) {
                        ILog.i(TAG, "删除成功了=" + arg0);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode arg0) {
                        ILog.i(TAG, "删除失败了=" + arg0);
                    }
                });
                return false;
            }

            @Override
            public void onSingleClick(int position) {

            }
        });

        mMessageAdapter = new MessageAdapter(this,mConversationList, mMessageIds);
        mSwipeMenuListView.setAdapter(mMessageAdapter);
        ReceiveMessageListener.getInstance().setAdapterInstance(mMessageAdapter);

        /**
         * ListView的item的点击事件
         */
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Conversation conversation = RongIMClient.getInstance().getConversationList().get(position);
                String targetId = conversation.getTargetId();
                //设置跳转哪个聊天页面
                setJumpPage(targetId);
            }
        });

        /** 判断是否是从后台消息(通知栏显示消息)进来的*/
        String uid = getIntent().getStringExtra("Uid");//从ReceivePushEvent类的onReceivePushMessage方法传过来的
        if(uid != null){
            //设置跳转哪个聊天页面
            setJumpPage(uid);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //当此activity重新获得焦点时，刷新融云消息数量
        mConversationList = RongIMClient.getInstance().getConversationList();
        mMessageAdapter.refreshAdapter(mConversationList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.registTotalUnreadCountListener(this);
        controller.unRegistTotalUnreadCountListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 设置跳转哪个聊天页面
     * @param targetId
     */
    private void setJumpPage(String targetId) {
        String chatUid = mMessageIds.get(0).getUid()+"";
        String orderUid = mMessageIds.get(1).getUid()+"";
        String discountUid = mMessageIds.get(2).getUid()+"";
        if(targetId.equals(chatUid)){
            Intent intent_support = new Intent(MessageActivity.this, ChatMessageActivity.class);
            intent_support.putExtra("Uid", chatUid);
            intent_support.putExtra("headJpg", mMessageIds.get(0).getHeadjpg());
            ReceiveMessageListener.getInstance().userId = chatUid;
            System.out.println("用户id为null-=-=" + chatUid);
            startActivity(intent_support);
        }else if(targetId.equals(orderUid)){
            Intent intent_order = new Intent(MessageActivity.this,OrderMessageActivity.class);
            intent_order.putExtra("Uid",orderUid);
//					DemoContext.getInstance().userId = messageIds.get(position).getUid()+"";// 给谁发消息的用户ID
            startActivity(intent_order);
        }else if(targetId.equals(discountUid)){
            Intent intent_discount = new Intent(MessageActivity.this,DiscountMessageActivity.class);
//					intent_discount.putExtra("Uid",mUserId);
            intent_discount.putExtra("Uid",discountUid);
//					DemoContext.getInstance().userId = messageIds.get(position).getUid()+"";// 给谁发消息的用户ID
            startActivity(intent_discount);
        }else{
            Intent intent_defaultt = new Intent(MessageActivity.this, ChatMessageActivity.class);
            if(targetId != null){
                intent_defaultt.putExtra("Uid", targetId);
                intent_defaultt.putExtra("headJpg", mMessageIds.get(0).getHeadjpg());
                ReceiveMessageListener.getInstance().userId = targetId;
            }
            startActivity(intent_defaultt);
        }
    }

    @Override
    public void totalUnreadCount(int totalUnreadCount) {

    }
}
