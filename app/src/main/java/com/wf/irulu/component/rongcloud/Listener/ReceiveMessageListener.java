package com.wf.irulu.component.rongcloud.Listener;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.MessageBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.GetPathUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.component.rongcloud.message.DiscountMessage;
import com.wf.irulu.component.rongcloud.message.GroupInvitationNotification;
import com.wf.irulu.component.rongcloud.message.OrderMessage;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.module.message.activity.MessageActivity;
import com.wf.irulu.module.message.adapter.ChatMessageAdapter;
import com.wf.irulu.module.message.adapter.DiscountMessageAdapter;
import com.wf.irulu.module.message.adapter.MessageAdapter;
import com.wf.irulu.module.message.adapter.OrderMessageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.ProfileNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
/**
 * 
 * @描述:消息监听器
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.component.rongcloud.context
 * @类名:DemoContext
 * @作者: 左西杰
 * @创建时间:2015-8-18 上午9:19:41
 *
 */
public class ReceiveMessageListener {

    private static final String TAG = "ReceiveMessageListener";

	protected static final int MESSAGETYPE = 0;

	protected static final int CHATMESSAGETYPE = 1;

	protected static final int ORDERMESSAGETYPE = 2;

	protected static final int DISCOUNTMESSAGETYPE = 3;

    private static ReceiveMessageListener self;

    private SharedPreferences sharedPreferences;

    public Context mContext;

    public RongIMClient mRongIMClient;
    public BaseAdapter mMessgeAdapter;
    public OrderMessageAdapter mOrderMessageAdapter;
    public ChatMessageAdapter mChatMessageAdapter;
    public DiscountMessageAdapter mDiscountMessageAdapter;
    private ListView mListView;
    private ArrayList<MessageBean> mDataArrays;
    private List<Message> mMessageArrays;
    public String userId;
    private String pushContent = null ;
    /** 图片地址URL*/
	public static ArrayList<String> messageImage = new ArrayList<String>();
	
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case MESSAGETYPE:
				if(mMessgeAdapter !=null){
					MessageAdapter messageAdapter = (MessageAdapter)mMessgeAdapter;
					List<Conversation> conversationList = RongIMClient.getInstance().getConversationList();
					messageAdapter.refreshAdapter(conversationList);// 通知ListView，数据已发生改变
				}
				break;
			case CHATMESSAGETYPE:
				if(mChatMessageAdapter !=null){
					mChatMessageAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
					mListView.setSelection(mListView.getCount());
				}
				break;
			case ORDERMESSAGETYPE:
				if(mOrderMessageAdapter !=null){
					mOrderMessageAdapter.refreshAdapter(mMessageArrays);// 通知ListView，数据已发生改变
					mListView.setSelection(mListView.getCount());
				}
				break;
			case DISCOUNTMESSAGETYPE:
				Log.e("onReceived", "VoiceMessage--接收【折扣消息】 discountMessage.getContent()---if--" );
				if(mDiscountMessageAdapter !=null){
					Log.e("onReceived", "VoiceMessage--接收【折扣消息】 discountMessage.getContent()---if里--" );
					mDiscountMessageAdapter.refreshAdapter(mMessageArrays);// 通知ListView，数据已发生改变
					mListView.setSelection(mListView.getCount());
				}
				break;
			default:
				break;
			}
    	};
    };

    public static ReceiveMessageListener getInstance() {

        if (self == null) {
            self = new ReceiveMessageListener();
        }

        return self;
    }

    public ReceiveMessageListener() {
    }

    public ReceiveMessageListener(Context context) {
        self = this;
    }

    public void init(Context context) {

        mContext = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setAdapterInstance(ChatMessageAdapter adapter) {
    	mChatMessageAdapter = adapter;
    }
    
    public void setAdapterInstance(BaseAdapter adapter) {
    	mMessgeAdapter = adapter;
    }
    
    public void setAdapterInstance(OrderMessageAdapter adapter) {
    	mOrderMessageAdapter = adapter;
    }
    
    public void setAdapterInstance(DiscountMessageAdapter adapter) {
    	mDiscountMessageAdapter = adapter;
    }
    
    public void setListView(ListView listView) {
    	mListView = listView;
    }
    
    public ListView getListView(){
		return mListView;
    }
    
    public void setDataArrays(ArrayList<MessageBean> dataArrays) {
    	mDataArrays = dataArrays;
    }
    
    public void setMessageArrays(List<Message> messageArrays){
    	this.mMessageArrays = messageArrays;
    }
    
    public ArrayList<MessageBean> getDataArrays(){
    	return mDataArrays;
    }
    
    public void setRongIMClient(RongIMClient rongIMClient) {
    	mRongIMClient = rongIMClient;
    }
    
    public RongIMClient getRongIMClient(){
		return mRongIMClient;
    }

    public void registerReceiveMessageListener() {
        RongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);
    }
    
//    public void registerReceivePushMessageListener(){
//    	mRongIMClient.setOnReceivePushMessageListener(ReceivePushMessage);
//    }
    		
//	RongIMClient.OnReceivePushMessageListener ReceivePushMessage = new OnReceivePushMessageListener() {
//		/**
//    	 * 收到 push 消息的处理。
//    	 * 
//    	 * @param pushNotificationMessage push 消息实体。
//    	 * @return true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
//    	 */
//		@Override
//		public boolean onReceivePushMessage(PushNotificationMessage pushMessage) {
//			System.out.println("pushMessage.getPushContent()-里买那个的=" + pushMessage.getPushContent());
//			return false;// true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
//		}
//	};

    RongIMClient.OnReceiveMessageListener onReceiveMessageListener = new RongIMClient.OnReceiveMessageListener() {
    	/**
    	 * message 收到的消息实体。
    	 * left 剩余未拉取消息数目。
    	 * 返回:
    	 *	  收到消息是否处理完成。
    	 */
    	@Override
		public boolean onReceived(Message message, int left) {
    		//设置 push 通知的监听函数。 
//			RongIMClient.setOnReceivePushMessageListener(new ReceivePushMessage());
    		
    		android.os.Message msg = android.os.Message.obtain();
			msg.what = MESSAGETYPE;//消息类型
			handler.sendMessage(msg);

			MessageContent messageContent = message.getContent();

			int totalUnreadCount = RongIMClient.getInstance().getTotalUnreadCount();// 未读消息总数
			IruluController.getInstance().postTotalUnreadCountCallback(totalUnreadCount);
			
			if (message.getTargetId().equals(userId)) {
				System.out.println("userId-=-=" + userId);
				if (messageContent instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message.getContent();
					Log.d("onReceived", userId + "-接收到一条【文字消息】-----" + textMessage.getContent() + ",getExtra:" + textMessage.getExtra());
					MessageBean bean = new MessageBean();
					bean.setTime(System.currentTimeMillis());
					bean.setContent(textMessage.getContent());
					bean.setType(ChatMessageAdapter.RECEIVE_MESSAGE_TEXT);

					mDataArrays.add(bean);
					msg = android.os.Message.obtain();
					msg.what = CHATMESSAGETYPE;//消息类型
					handler.sendMessage(msg);
				} else if (messageContent instanceof ImageMessage) {
					final ImageMessage imageMessage = (ImageMessage) message.getContent();
					ILog.d("onReceived", "ImageMessage-接收到一条【图片消息】---LocalUri--" + imageMessage.getLocalUri());
					ILog.d("onReceived", "ImageMessage--接收到一条【图片消息】----Uri--" + imageMessage.getRemoteUri());
					ILog.d("onReceived", "ImageMessage--接收到一条【图片消息】----ThumUri--" + imageMessage.getThumUri());
					String path = null;
					if(imageMessage.getRemoteUri().toString().contains("file://")){
						ILog.d(TAG, "ImageMessage--接收到一条【图片消息】----包含file://--");
						path = GetPathUtils.getPath(IruluApplication.getInstance(),imageMessage.getThumUri());
					}else if(imageMessage.getRemoteUri().toString().contains("http://")){
						ILog.d(TAG, "ImageMessage--接收到一条【图片消息】----包含http://--");
						path = imageMessage.getRemoteUri().toString();
					}
					 new Thread(new Runnable() {
		                    @Override
		                    public void run() {
		                        mRongIMClient.downloadMedia(ConversationType.PRIVATE, userId, RongIMClient.MediaType.IMAGE, imageMessage.getRemoteUri().toString(), new RongIMClient.DownloadMediaCallback() {
		                            @Override
		                            public void onProgress(int i) {
		                                ILog.d(TAG, "onProgress:" + i);
		                            }
		                            @Override
		                            public void onSuccess(String s) {
		                                ILog.d(TAG, "onSuccess:" + s);
		                            }
		                            @Override
		                            public void onError(RongIMClient.ErrorCode errorCode) {
		                                ILog.d(TAG, "onError:" + errorCode.getValue());
		                            }
		                        });
		                    }
					 }).start();
					 
					String path1 = GetPathUtils.getPath(IruluApplication.getInstance(),imageMessage.getThumUri());
					ILog.d(TAG, "ImageMessage--接收到一条【图片消息】----getPath--" + path1);
					ILog.d(TAG, "ImageMessage--接收到一条【图片消息】----path--" + path);
					MessageBean bean = new MessageBean();
					bean.setTime(System.currentTimeMillis());
					bean.setContent(path);
//					bean.setUri(imageMessage.getRemoteUri());
					bean.setType(ChatMessageAdapter.RECEIVE_MESSAGE_IMAGE);
					messageImage.add(path);
					mDataArrays.add(bean);
					msg = android.os.Message.obtain();
					msg.what = CHATMESSAGETYPE;//消息类型
					handler.sendMessage(msg);
				} else if(messageContent instanceof OrderMessage){
					final OrderMessage orderMessage = (OrderMessage) message.getContent();
					ILog.e(TAG, "VoiceMessage--接收到一条【订单消息】 orderMessage.getContent()-----" + orderMessage.getContent());
					mMessageArrays = RongIMClient.getInstance().getLatestMessages(ConversationType.PRIVATE, userId, 10);
					msg = android.os.Message.obtain();
					msg.what = ORDERMESSAGETYPE;//消息类型
					handler.sendMessage(msg);
				}else if(messageContent instanceof DiscountMessage){
					final DiscountMessage discountMessage = (DiscountMessage) message.getContent();
					ILog.e(TAG, "VoiceMessage--接收到一条【折扣消息】 discountMessage.getContent()-----" + discountMessage.getContent());
					mMessageArrays = RongIMClient.getInstance().getLatestMessages(ConversationType.PRIVATE, userId, 10);
					msg = android.os.Message.obtain();
					msg.what = DISCOUNTMESSAGETYPE;//消息类型
					handler.sendMessage(msg);
				}else if (messageContent instanceof VoiceMessage) {
					final VoiceMessage voiceMessage = (VoiceMessage) message.getContent();
					ILog.e(TAG, "VoiceMessage--接收到一条【语音消息】 voiceMessage.getExtra-----" + voiceMessage.getExtra());
					new Thread(new Runnable() {
						@Override
						public void run() {
							MediaPlayer mMediaPlayer = new MediaPlayer();
							mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(MediaPlayer mp) {
									mp.start();
								}
							});
							try {
								mMediaPlayer.setDataSource(IruluApplication.getInstance(), voiceMessage.getUri());
								mMediaPlayer.prepare();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				} else if (messageContent instanceof GroupInvitationNotification) {

					GroupInvitationNotification groupInvitationNotification = (GroupInvitationNotification) message.getContent();

					ILog.d(TAG, "GroupInvitationNotification--接收到一条【群组邀请消息】-----" + groupInvitationNotification.getMessage());

				} else if (messageContent instanceof ContactNotificationMessage) {
					ContactNotificationMessage mContactNotificationMessage = (ContactNotificationMessage) message.getContent();
					ILog.d(TAG, "mContactNotificationMessage--接收到一条【联系人（好友）操作通知消息】-----" + mContactNotificationMessage.getMessage() + ",getExtra:" + mContactNotificationMessage.getExtra());

				} else if (messageContent instanceof ProfileNotificationMessage) {
					ProfileNotificationMessage mProfileNotificationMessage = (ProfileNotificationMessage) message.getContent();
					ILog.d(TAG, "GroupNotificationMessage--接收到一条【资料变更通知消息】-----" + mProfileNotificationMessage.getData() + ",getExtra:" + mProfileNotificationMessage.getExtra());

				} else if (messageContent instanceof CommandNotificationMessage) {
					CommandNotificationMessage mCommandNotificationMessage = (CommandNotificationMessage) message.getContent();
					ILog.d(TAG, "GroupNotificationMessage--接收到一条【命令通知消息】-----" + mCommandNotificationMessage.getData() + ",getName:" + mCommandNotificationMessage.getName());
				} else if (messageContent instanceof InformationNotificationMessage) {
					InformationNotificationMessage mInformationNotificationMessage = (InformationNotificationMessage) message.getContent();
					ILog.d(TAG, "InformationNotificationMessage--接收到一条【小灰条消息】-----" + mInformationNotificationMessage.getMessage() + ",getName:" + mInformationNotificationMessage.getExtra());

				}
        	}
			Intent intent = new Intent(IruluApplication.getInstance(), MessageActivity.class);
			String targetId = message.getTargetId();
			String targetUserName = "";
			if("1001".equals(targetId)){
				targetUserName = "Order Message";
			}else if("1002".equals(targetId)){
				targetUserName = "Discount Message";
			}else{
				targetUserName = "iRULU Support";
			}
			intent.putExtra("Uid",targetId);
			
			if (messageContent instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message.getContent();
				ILog.d(TAG, userId + "-接收到一条【文字消息】-----" + textMessage.getContent() + ",getExtra:" + textMessage.getExtra());
				pushContent = textMessage.getContent();
			} else if (messageContent instanceof ImageMessage) {
				final ImageMessage imageMessage = (ImageMessage) message.getContent();
				pushContent = "[图片]";
			} else if(messageContent instanceof OrderMessage){
				OrderMessage orderMessage = (OrderMessage)messageContent;
				pushContent = orderMessage.getContent();
			}else if(messageContent instanceof DiscountMessage){
				DiscountMessage discountMessage = (DiscountMessage)messageContent;
				pushContent = discountMessage.getContent();
			}
			
    		if(isBackground(mContext)){//后台
    			Notification notification = null;
    			if(intent != null){
//    				PendingIntent pendingIntent = PendingIntent.getActivity(IruluApplication.getInstance(), 0, intent,Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent pendingIntent = PendingIntent.getActivity(IruluApplication.getInstance(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//    				if (android.os.Build.VERSION.SDK_INT < 11) {
//    					notification = new Notification(R.mipmap.headpic,targetUserName+":"+pushContent, System.currentTimeMillis());
//    					notification.setLatestEventInfo(IruluApplication.getInstance(),targetUserName, pushContent, pendingIntent);
//    					notification.flags = Notification.FLAG_AUTO_CANCEL;
//    					notification.defaults = Notification.DEFAULT_SOUND;
//    				} else {
//    					notification = new Notification.Builder(IruluApplication.getInstance())
//    					.setLargeIcon(getAppIcon())
//    					.setSmallIcon(R.drawable.ic_launcher)//如果没有指定大的，小的会自动移动到大的图标上去
//    					.setTicker(targetUserName+":"+pushContent)
//    					.setContentTitle(targetUserName)
//    					.setContentText(pushContent)
//    					.setContentIntent(pendingIntent).setAutoCancel(true)
//    					.setDefaults(Notification.DEFAULT_ALL).build();
//    				}
					notification=new NotificationCompat.Builder(IruluApplication.getInstance())
							.setSmallIcon(R.drawable.ic_launcher)
							.setLargeIcon(getAppIcon())
							.setTicker(targetUserName+":"+pushContent)
							.setContentTitle(targetUserName)
							.setContentText(pushContent)
							.setContentIntent(pendingIntent).setAutoCancel(true)
							.setDefaults(Notification.DEFAULT_ALL)
							.build();

    			}
    			NotificationManager notificationManager  = (NotificationManager) IruluApplication.getInstance().getSystemService(IruluApplication.getInstance().NOTIFICATION_SERVICE);

    			notificationManager.notify(ConstantsUtils.notificationId, notification);
    		}
            return false;
        }

    };

    /**
     * 判断当前应用程序处于前台还是后台
     * @param context
     * @return true 为后台，false为前台
     */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					ILog.i(TAG, "Background App:" + appProcess.processName);
					return true;
				} else {
					ILog.i(TAG,"Foreground App:"+appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
	
	private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) IruluApplication.getInstance().getApplicationInfo().loadIcon(IruluApplication.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }
}