package com.wf.irulu.module.message.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.MessageBean;
import com.wf.irulu.common.utils.GetPathUtils;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase.Mode;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshListView;
import com.wf.irulu.component.rongcloud.Listener.ReceiveMessageListener;
import com.wf.irulu.component.rongcloud.message.GroupInvitationNotification;
import com.wf.irulu.module.message.adapter.ChatMessageAdapter;
import com.wf.irulu.module.message.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import io.rong.message.LocationMessage;
import io.rong.message.ProfileNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.message.utils.BitmapUtil;

/**
 * @描述: 聊天界面
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.module.activity
 * @类名:ChatActivity
 * @作者: 左西杰
 * @创建时间:2015-6-2 下午3:02:10
 * 
 */
public class ChatMessageActivity extends CommonTitleBaseActivity implements OnRefreshListener<ListView> {
	public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
	public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
	private View mView;
	private PullToRefreshListView mListView;
	private Button btnSend;
	private EditText etContent;
	private ChatMessageAdapter mAdapter;
	private ArrayList<MessageBean> mDataArrays = new ArrayList<MessageBean>();
	private String uid;
	private RongIMClient mRongIMClient;
	private Message mMessage;
	private Dialog dialog;
	private static final int SELECT_IMAGE_REQUEST_CODE = 1;
	private Handler mHandler;
	private int oldestMessageId = -1;//没有消息第一次调用应设置为:-1。
	private Intent intent;
	private List<Message> latestMessages = null;
	/** 图片地址URL*/
	public static ArrayList<String> messageImage = ReceiveMessageListener.getInstance().messageImage;
	
	@Override
	protected String setWindowTitle() {
		return "iRULU Support";
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_chat);
	}

	@Override
	public void initView() {
		//初始化购物车数量的显示
		mHandler = new Handler();
		mListView = (PullToRefreshListView) findViewById(R.id.message_list_view);
		mListView.setBackgroundResource(R.color.gray);
		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setOnRefreshListener(this);
		
		btnSend = (Button) findViewById(R.id.message_btn_send);
		etContent = (EditText) findViewById(R.id.message_et_content);
		ImageView mCameraBtn = (ImageView) findViewById(R.id.message_iv_camera);//相机按钮
//		btnSend.setEnabled(false);
//		etContent.addTextChangedListener(this);
		btnSend.setOnClickListener(this);
		mCameraBtn.setOnClickListener(this);
		
		intent = getIntent();
		uid = intent.getStringExtra("Uid");
		System.out.println("-=-=uid="+uid);
		String headJpg = intent.getStringExtra("headJpg");
		
		mRongIMClient = ReceiveMessageListener.getInstance().getRongIMClient();
//		 List<Message> latestMessages = RongIMClient.getInstance().getHistoryMessages(ConversationType.PRIVATE,uid,latestMessageId, 10);
		getItems();

		mAdapter = new ChatMessageAdapter(this,LayoutInflater.from(this), mDataArrays,messageImage,intent);
		ReceiveMessageListener.getInstance().setAdapterInstance(mAdapter);
		mListView.getRefreshableView().setDividerHeight(0);//去掉ListView的横线
		mListView.setAdapter(mAdapter);
		mListView.getRefreshableView().setSelection(mAdapter.getCount() - 1);// 一进来默认显示最新的消息
		
	}

	private void getItems() {
		latestMessages = RongIMClient.getInstance().getLatestMessages(ConversationType.PRIVATE, uid,20);
		if (latestMessages != null) {
			oldestMessageId = latestMessages.get(0).getMessageId();
			System.out.println("-=-=oldestMessageId=latestMessages-=" + oldestMessageId);
			int size = latestMessages.size();
			for (int i = 0; i <size ; i++) {
				mMessage = latestMessages.get(i);
				String targetId = mMessage.getTargetId();
				System.out.println("targetId-=-=" + targetId);
				MessageContent messageContent = mMessage.getContent();
				if(messageContent instanceof ImageMessage){
					ImageMessage imageMessage = (ImageMessage)messageContent;
					if (mMessage.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {/** 接收*/
						final Uri remoteUri = imageMessage.getRemoteUri();//获取网络图片地址
						Uri localUri = imageMessage.getLocalUri();//获取本地图片地址
						Uri thumUri = imageMessage.getThumUri();//获取缩略图Uri。
						String path = null;
						if(remoteUri.toString().contains("http://")){
							path = remoteUri.toString();
						}else{
							path = GetPathUtils.getPath(IruluApplication.getInstance(), thumUri);
						}
						
						mDataArrays.add(new MessageBean(ChatMessageAdapter.RECEIVE_MESSAGE_IMAGE,path,mMessage.getReceivedTime()));
						messageImage.add(remoteUri.toString());
					} else if (mMessage.getMessageDirection().equals(Message.MessageDirection.SEND)) {/** 发送*/
						Uri remoteUri = imageMessage.getRemoteUri();//获取网络图片地址
						Uri localUri = imageMessage.getLocalUri();//获取本地图片地址
						Uri thumUri = imageMessage.getThumUri();//获取缩略图Uri。
						String getThumPath = GetPathUtils.getPath(IruluApplication.getInstance(), thumUri);
						mDataArrays.add(new MessageBean(ChatMessageAdapter.SEND_MESSAGE_IMAGE,getThumPath,mMessage.getSentTime()));
						messageImage.add(GetPathUtils.getPath(IruluApplication.getInstance(), remoteUri));
					}
				}else if(messageContent instanceof TextMessage){
					TextMessage textMessage = (TextMessage) messageContent;
					if (mMessage.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
						mDataArrays.add(new MessageBean(ChatMessageAdapter.RECEIVE_MESSAGE_TEXT, textMessage.getContent(),mMessage.getReceivedTime()));
					} else if (mMessage.getMessageDirection().equals(Message.MessageDirection.SEND)) {
						mDataArrays.add(new MessageBean(ChatMessageAdapter.SEND_MESSAGE_TEXT, textMessage.getContent(),mMessage.getSentTime()));
					}
				}
			}
		}
	}

	@Override
	public void initData() {
		// 注册接受消息监听事件
//		mRongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);
		ReceiveMessageListener.getInstance().setListView(mListView.getRefreshableView());
		ReceiveMessageListener.getInstance().setDataArrays(mDataArrays);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_btn_send:// 发送按钮点击事件
			String contString = etContent.getText().toString().trim();
			if (contString.length() > 0) {
				MessageBean bean = new MessageBean();
				bean.setTime(System.currentTimeMillis());
				bean.setContent(contString);
				bean.setType(ChatMessageAdapter.SEND_MESSAGE_TEXT);

				mDataArrays.add(bean);
				mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
				etContent.setText("");// 清空编辑框数据
				// mListView.setSelection(mListView.getCount());
				mListView.getRefreshableView().setSelection(mAdapter.getCount() - 1);

				TextMessage textMessage = TextMessage.obtain(contString);
				textMessage.setExtra("文字消息Extra");
				sendMessage(textMessage);
			}
			break;
		case R.id.message_iv_camera:
			LayoutInflater factory = LayoutInflater.from(ChatMessageActivity.this) ;
			View myView = factory.inflate(R.layout.dialog_choose_pic_layout, null) ;//将布局文件转换为View
			ImageView cancel_choose = (ImageView) myView.findViewById(R.id.cancel_choose);
			LinearLayout pic_layout = (LinearLayout) myView.findViewById(R.id.pic_layout);
			LinearLayout camera_layout = (LinearLayout) myView.findViewById(R.id.camera_layout);
			
			dialog = new AlertDialog.Builder(ChatMessageActivity.this).setView(myView).create();
			dialog.show() ;
			
			//对话框右上角的差的点击事件
			cancel_choose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			//对话框里的相册的点击事件
			pic_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					try {
						/*Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);//照片
						intent.setType("image/*");
						startActivityForResult(intent,SELECT_IMAGE_REQUEST_CODE);*/
						/*if (Build.VERSION.SDK_INT < 19) {
						    Intent intent = new Intent();
						    intent.setType("image/*");
						    intent.setAction(Intent.ACTION_GET_CONTENT);
						    startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
						}
						else {
						    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						    intent.addCategory(Intent.CATEGORY_OPENABLE);
						    intent.setType("image/jpeg");
						    startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
						}*/
						Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/jpeg");
						if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){//KitKat正是4.4的代号.                
						     intent.setAction(Intent.ACTION_OPEN_DOCUMENT); 
//						     startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE); 
						}else{              
						     intent.setAction(Intent.ACTION_GET_CONTENT);
//						     startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE); 
						} 
						
				  		/*Intent intent = new Intent();
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						//根据版本号不同使用不同的Action
						if (Build.VERSION.SDK_INT <19) {
							intent.setAction(Intent.ACTION_GET_CONTENT);
						}else {
							intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
						}*/
						startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
						/*Intent intent;
						intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);*/
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//对话框里的相机的点击事件
			camera_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					try {
						// 调用系统的拍照功能
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

						// 指定调用相机拍照后照片的储存路径
//						Uri fileUri = GetOutputMediaFileUriUtils.getOutputMediaFileUri(GetOutputMediaFileUriUtils.MEDIA_TYPE_IMAGE);
//						intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
						startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;
		default:
			break;
		}
	}

	/**
	 * 发送消息
	 */
	private void sendMessage(final MessageContent msg) {
		if (mRongIMClient != null) {
			mRongIMClient.sendMessage(ConversationType.PRIVATE, uid, msg, null, null, new RongIMClient.SendMessageCallback() {
				@Override
				public void onSuccess(Integer integer) {
					if (msg instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) msg;
//						Toast.makeText(IruluApplication.getInstance(), "发送成功", 1).show();
						Log.d("sendMessage", "TextMessage---发发发发发--发送了一条【文字消息】-----" + textMessage.getContent());
					} else if (msg instanceof ImageMessage) {
						ImageMessage imageMessage = (ImageMessage) msg;
						Log.d("sendMessage", "ImageMessage--发发发发发--发送了一条【图片消息】--uri---" + imageMessage.getThumUri());
					} else if (msg instanceof VoiceMessage) {
						VoiceMessage voiceMessage = (VoiceMessage) msg;
						Log.e("sendMessage", "VoiceMessage--发发发发发--发送了一条【语音消息】---getExtra--" + voiceMessage.getExtra());
						Log.d("sendMessage", "VoiceMessage--发发发发发--发送了一条【语音消息】--长度---" + voiceMessage.getDuration());
					} else if (msg instanceof LocationMessage) {
						LocationMessage location = (LocationMessage) msg;
						Log.d("sendMessage", "VoiceMessage--发发发发发--发送了一条【语音消息】---uri--" + location.getPoi());
					} else if (msg instanceof GroupInvitationNotification) {
						GroupInvitationNotification groupInvitationNotification = (GroupInvitationNotification) msg;
						Log.d("sendMessage", "VoiceMessage--发发发发发--发送了一条【群组邀请消息】---message--" + groupInvitationNotification.getMessage());
					} else if (msg instanceof ContactNotificationMessage) {
						ContactNotificationMessage mContactNotificationMessage = (ContactNotificationMessage) msg;
						Log.d("sendMessage", "ContactNotificationMessage--发发发发发--发送了一条【联系人（好友）操作通知消息】---message--" + mContactNotificationMessage.getMessage());
					} else if (msg instanceof ProfileNotificationMessage) {
						ProfileNotificationMessage mProfileNotificationMessage = (ProfileNotificationMessage) msg;
						Log.d("sendMessage", "ProfileNotificationMessage--发发发发发--发送了一条【资料变更通知消息】---message--" + mProfileNotificationMessage.getData());
					} else if (msg instanceof CommandNotificationMessage) {
						CommandNotificationMessage mCommandNotificationMessage = (CommandNotificationMessage) msg;
						Log.d("sendMessage", "CommandNotificationMessage--发发发发发--发送了一条【命令通知消息】---message--" + mCommandNotificationMessage.getData());
					} else if (msg instanceof InformationNotificationMessage) {
						InformationNotificationMessage mInformationNotificationMessage = (InformationNotificationMessage) msg;
						Log.d("sendMessage", "InformationNotificationMessage--发发发发发--发送了一条【小灰条消息】---message--" + mInformationNotificationMessage.getMessage());
					}
				}

				@Override
				public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
					Log.d("sendMessage", "----发发发发发--发送消息失败----ErrorCode----" + errorCode.getValue());
				}
			});
		} else {
			Toast.makeText(this, "请先连接。。。", Toast.LENGTH_LONG).show();
		}
	}

	/*@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onAfterTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		String content = etContent.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			btnSend.setEnabled(false);
		} else {
			btnSend.setEnabled(true);
		}
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case SELECT_IMAGE_REQUEST_CODE://RESULT_OK
			Intent intent = null;
			//选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
			if (data != null && data.getExtras() != null) {
				intent = getIntent();
				intent.putExtras(data.getExtras());
			}
//			setResult(1, intent);
			
			//取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
	        Uri uri = data.getData();  
	        System.out.println("uri="+uri);
	        //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
	        if(uri != null){  
	        	String path = GetPathUtils.getPath(IruluApplication.getInstance(), uri);
			    MessageBean bean = new MessageBean();
				bean.setTime(System.currentTimeMillis());
				bean.setType(ChatMessageAdapter.SEND_MESSAGE_IMAGE);
				bean.setContent(path);
//				bean.setUri(uri);
				
				mDataArrays.add(bean);
				mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
				etContent.setText("");// 清空编辑框数据
				mListView.getRefreshableView().setSelection(mAdapter.getCount() - 1);
				
//				String[] proj = { MediaStore.Images.Media.DATA };  
//				  
//				Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);  
//				  
//				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
//				  
//				actualimagecursor.moveToFirst();  
//				  
//				String img_path = actualimagecursor.getString(actual_image_column_index);  
//				System.out.println("img_path-="+img_path);//img_path-=/storage/sdcard0/DCIM/Camera/IMG_20150807_085716.jpg
//				File file = new File(img_path);
//				InputStream stream = null;
//				try {
//					stream = new FileInputStream(file);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			
//				Bitmap bitmap = BitmapUtils.getResizedBitmap(IruluApplication.getInstance(), uri, 960, 960);
				sendImageMessage(path);
				messageImage.add(path);
            }else{
            	Bundle extras = data.getExtras();
				if (extras != null) {
					//这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
					Bitmap image = extras.getParcelable("data");
					System.out.println("image->"+image);
					Uri uri1 = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), image, null,null));
					System.out.println("uri1-=>"+uri1);
					String path = GetPathUtils.getPath(IruluApplication.getInstance(), uri1);
					System.out.println("path-=-="+path);
					
					MessageBean bean = new MessageBean();
					bean.setTime(System.currentTimeMillis());
					bean.setType(ChatMessageAdapter.SEND_MESSAGE_IMAGE);
					bean.setContent(path);
					
					mDataArrays.add(bean);
					mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
					etContent.setText("");// 清空编辑框数据
					mListView.getRefreshableView().setSelection(mAdapter.getCount() - 1);
					
					sendImageMessage(path);
					messageImage.add(path);
				}
            }
	        break;
		default:
			break;
		}
		
	}

	/**
	 * 发送图片消息
	 * @param path
	 */
	private void sendImageMessage(String path) {
		Uri uri;
		Bitmap bitmap;
		try {
			File file = new File(path);
			InputStream stream = new FileInputStream(file);;
			uri = FileUtil.writeByte(Uri.parse(path), FileUtil.toByteArray(stream));
			bitmap = BitmapUtil.getResizedBitmap(IruluApplication.getInstance(), uri, 100, 100);
//			bitmap = MediaStore.Images.Media.getBitmap(IruluApplication.getInstance().getContentResolver(),uri);
			if (bitmap != null) {

				Uri thumUri = uri.buildUpon().appendQueryParameter("thum", "true").build();
				ImageMessage imageMessage = ImageMessage.obtain(thumUri, uri);
				
				//发送消息
				sendMessage(imageMessage);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// 初始化未读总数
		if (RongIMClient.getInstance() != null) {
			Conversation conversation = RongIMClient.getInstance().getConversation(ConversationType.PRIVATE, uid);
			conversation.setUnreadMessageCount(0);//设置未读消息数为0
			int totalUnreadCount = RongIMClient.getInstance().getTotalUnreadCount();// 未读消息总数
			controller.postTotalUnreadCountCallback(0);
		}
		controller.unRegistShoppongCartCounListenert(this);
		super.onDestroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				System.out.println("-=-=oldestMessageId-="+oldestMessageId);
				final List<Message> historyMessages = RongIMClient.getInstance().getHistoryMessages(ConversationType.PRIVATE,uid,oldestMessageId,20);
				if (historyMessages != null) {
					oldestMessageId = historyMessages.get(0).getMessageId();
					System.out.println("-=-=oldestMessageId=historyMessages-="+oldestMessageId);
					for (int i = historyMessages.size()-1; i >0; i--) {
						mMessage = historyMessages.get(i);
						MessageContent messageContent = mMessage.getContent();
						if(messageContent instanceof ImageMessage){
							ImageMessage imageMessage = (ImageMessage)messageContent;
							if (mMessage.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {//接收
								Uri remoteUri = imageMessage.getRemoteUri();//获取网络图片地址
								Uri thumUri = imageMessage.getThumUri();
								String path = null;
								if(remoteUri.toString().contains("http://")){
									path = remoteUri.toString();
								}else{
									path = GetPathUtils.getPath(IruluApplication.getInstance(), thumUri);
								}
								mDataArrays.add(0,new MessageBean(ChatMessageAdapter.RECEIVE_MESSAGE_IMAGE,path,mMessage.getReceivedTime()));
								messageImage.add(path);
							} else if (mMessage.getMessageDirection().equals(Message.MessageDirection.SEND)) {//发送
								Uri remoteUri = imageMessage.getRemoteUri();//获取网络图片地址
								String getRemotePath = GetPathUtils.getPath(IruluApplication.getInstance(), remoteUri);
								mDataArrays.add(0,new MessageBean(ChatMessageAdapter.SEND_MESSAGE_IMAGE,getRemotePath,mMessage.getSentTime()));
								messageImage.add(getRemotePath);
							}
						}else if(messageContent instanceof TextMessage){
							TextMessage textMessage = (TextMessage) messageContent;
							if (mMessage.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
								mDataArrays.add(0,new MessageBean(ChatMessageAdapter.RECEIVE_MESSAGE_TEXT, textMessage.getContent(),mMessage.getReceivedTime()));
							} else if (mMessage.getMessageDirection().equals(Message.MessageDirection.SEND)) {
								mDataArrays.add(0,new MessageBean(ChatMessageAdapter.SEND_MESSAGE_TEXT, textMessage.getContent(),mMessage.getSentTime()));
							}
						}
					}
				}
//				mAdapter.setNotifyDataSetChanged(mDataArrays);
				mAdapter.notifyDataSetChanged();
				
				mListView.post(new Runnable() {
					@Override
					public void run() {
						/*
						 * 方法一：
						 */
//						View v = mListView.getChildAt(0);
//						int top = (v == null) ? 0 : v.getTop();
//						mListView.setSelectionFromTop(historyMessages.size()-1, top);
						/*
						 * 方法二
						 */
						if(historyMessages != null){
							mListView.getRefreshableView().setSelection(historyMessages.size() -1);
						}else{
//							Toast.makeText(ChatMessageActivity.this,"no data !", 0).show();
						}
					}
				});
				
				mListView.onRefreshComplete();  
			}
		}, 1000);
		
	}
}
