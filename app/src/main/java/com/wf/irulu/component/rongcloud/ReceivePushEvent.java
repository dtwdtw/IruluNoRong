package com.wf.irulu.component.rongcloud;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.MainActivity;
import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.framework.HomeActivity;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OnReceivePushMessageListener;
import io.rong.notification.PushNotificationMessage;

/**
 * @描述: 接收 push 消息的监听器。
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message
 * @类名:ReceivePushMessage
 * @作者: 左西杰
 * @创建时间:2015-8-11 上午11:10:54
 * 
 */
public class ReceivePushEvent implements OnReceivePushMessageListener, Callback {
	private static ReceivePushEvent mReceivePushMessage;
	private Context mContext;
	private Handler mHandler;
	
	 /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mReceivePushMessage == null) {

            synchronized (ReceivePushEvent.class) {

                if (mReceivePushMessage == null) {
                    mReceivePushMessage = new ReceivePushEvent(context);
                }
            }
        }
    }
    
    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private ReceivePushEvent(Context context) {
        mContext = context;
        initDefaultListener();
        mHandler = new Handler(this);
    }
	
    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
	private void initDefaultListener() {
//		RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
//      RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
//      RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
//      RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
//      RongIM.setConversationListBehaviorListener(this);
//      RongIM.getInstance().setMessageAttachedUserInfo(true);
	    RongIMClient.setOnReceivePushMessageListener(this);//自定义 push 通知。
	}

	/**
	 * 自定义 push 通知。
	 * 
	 * @param pushMessage
	 *            push 消息实体。
	 * @return true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
	 */
	@Override
	public boolean onReceivePushMessage(PushNotificationMessage pushMessage) {
		String pushContent = pushMessage.getPushContent();
		String targetUserName = pushMessage.getTargetUserName();
		String senderName = pushMessage.getSenderName();//它的值好像跟targetUserName的只一样
		if(TextUtils.isEmpty(targetUserName)){
			String senderUserId = pushMessage.getSenderUserId();
			if("1001".equals(senderUserId)){
				targetUserName = "Order Message";
			}else if("1002".equals(senderUserId)){
				targetUserName = "Discount Message";
			}else
				targetUserName = "iRULU Support";
		}
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra("tag","tag");

		//在通知栏弹出服务器端发送过来的通知信息
		Notification notification = null;
		if(intent != null){
//			PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pendingIntent = PendingIntent.getActivity(IruluApplication.getInstance(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//			if (android.os.Build.VERSION.SDK_INT < 11) {
//				notification = new Notification(R.mipmap.headpic,targetUserName+":"+pushContent, System.currentTimeMillis());
//				notification.setLatestEventInfo(mContext, targetUserName, pushContent, pendingIntent);
//				notification.flags = Notification.FLAG_AUTO_CANCEL;
//				notification.defaults = Notification.DEFAULT_SOUND;
//			} else {
//				notification = new Notification.Builder(mContext)
//				.setLargeIcon(getAppIcon())
//				.setSmallIcon(R.mipmap.ic_launcher)//如果没有指定大的，小的会自动移动到大的图标上去
//				.setTicker(targetUserName+":"+pushContent)
//				.setContentTitle(targetUserName)
//				.setContentText(pushContent)
//				.setContentIntent(pendingIntent).setAutoCancel(true)
//				.setDefaults(Notification.DEFAULT_ALL).build();
//			}
			notification=new NotificationCompat.Builder(mContext)
					.setLargeIcon(getAppIcon())
					.setSmallIcon(R.mipmap.ic_launcher)//如果没有指定大的，小的会自动移动到大的图标上去
					.setTicker(targetUserName+":"+pushContent)
					.setContentTitle(targetUserName)
					.setContentText(pushContent)
					.setContentIntent(pendingIntent).setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_ALL).build();
		}
		NotificationManager notificationManager  = (NotificationManager) IruluApplication.getInstance().getSystemService(IruluApplication.getInstance().NOTIFICATION_SERVICE);

		notificationManager.notify(ConstantsUtils.notificationId, notification);
		return true;// true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
	}

	private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) IruluApplication.getInstance().getApplicationInfo().loadIcon(IruluApplication.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
	
	/**
     * 收到push消息后做重连，重新连接融云
     *
     * @param token
     */
	private void reconnect(String token) {
		RongIMClient.connect(token,new RongIMClient.ConnectCallback() {

			@Override
			public void onTokenIncorrect() {
				
			}

			@Override
			public void onError(ErrorCode errorCode) {
				
			}

			@Override
			public void onSuccess(String userId) {
//				Toast.makeText(IruluApplication.getInstance(),"重新连接成功", 0).show();
			}
			
		});
	}
	
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
					System.out.println("Background App:"+appProcess.processName);
					return true;
				} else {
					System.out.println("Foreground App:"+appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
}
