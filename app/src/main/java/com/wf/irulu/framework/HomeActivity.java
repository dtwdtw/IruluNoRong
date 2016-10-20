package com.wf.irulu.framework;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.AdInfo;
import com.wf.irulu.common.bean.InitAppBean;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.bean.NewVersionInfors;
import com.wf.irulu.common.bean.PopupInfo;
import com.wf.irulu.common.bean.ShareInfo;
import com.wf.irulu.common.bean.StartAdvertising;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.CommonUtil;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.TwitterUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.component.rongcloud.Listener.ReceiveMessageListener;
import com.wf.irulu.component.slidingmenu.SlidingMenu;
import com.wf.irulu.component.slidingmenu.app.SlidingFragmentActivity;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.advertising.dialog.AdvertisingDialog;
import com.wf.irulu.module.homenew1_3_0.Fragment.ContentFragment1_3_0;
import com.wf.irulu.module.message.activity.MessageActivity;
import com.wf.irulu.module.newArrivals.NewArrivalsActivity;
import com.wf.irulu.module.product.dialog.ProductShareDialog;

import java.io.File;
import java.net.URL;

import io.fabric.sdk.android.Fabric;
import io.rong.imlib.RongIMClient;

/**
 * @描述: 主页面--侧滑
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.framework
 * @类名:MainActivity
 * @作者: 左杰
 * @创建时间:2015/10/24 12:09
 */
public class HomeActivity extends SlidingFragmentActivity implements ServiceListener, Dialog.OnDismissListener {

    private final String TAG = getClass().getCanonicalName();
    private static final String TAG_CONTENT = "content";
    private static final String TAF_MENU = "menu";
    public static RongIMClient mRongIMClient;
    private SlidingMenu menu;
    public AppEventsLogger mLogger;

    /**
     * 分享弹窗
     */
    private ProductShareDialog mProductShareDialog;
    /**
     * facebook 回调
     */
    private CallbackManager mCallbackManager;
    /**
     * facebook分享弹窗
     */
    private ShareDialog mShareDialog;
    private ShareInfo mShareInfo;
    private AdvertisingDialog mDialog;
    /**
     * app 从后台唤醒，进入前台 ,true:前台，false:后台
     */
    public boolean isActive = true;
    /**
     * 弹出广告当前时间
     */
    private long currentTime;

    private Dialog updateDialog;
    private int updateType;

    private NotificationManager mNotificationManager;
    private Notification mNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断token是否过期
        if (!TextUtils.isEmpty(CacheUtils.getString(this, "token"))) {
            long l = System.currentTimeMillis() - CacheUtils.getLong(this, "outtime", 0);
            if (l > 7 * 23 * 60 * 60 * 1000l && l < 2 * 365 * 24 * 60 * 60 * 1000l) {
                CacheUtils.setString(this, "token", null);
                CacheUtils.setLong(this, "uid", 0);
            }
        }
        // 1、设置主页内容
        setContentView(R.layout.fragment_content);
        // 2、设置菜单布局
        setBehindContentView(R.layout.fragment_menu);

        // 获得菜单的实例
        menu = getSlidingMenu();
        // 3、设置SlibingMenu的mode
        menu.setMode(SlidingMenu.LEFT);// 不设置默认是左边


        // 4、设置菜单的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置menu的旁边的宽度(不是menu本身的宽度)
        ILog.i(TAG, "屏幕宽：" + ConstantsUtils.DISPLAYW + ",屏幕高:" + ConstantsUtils.DISPLAYH);
        ILog.i(TAG, "系统版本=" + CommonUtil.getSDKVersionNumber());

		/*
         * 5、设置Touch Mode Above TOUCHMODE_FULLSCREEN：可以在哪都可以拖拽
		 * TOUCHMODE_MARGIN：只能在菜单与非菜单(主页面)之间的边缘拖拽 TOUCHMODE_NONE：不可以拖拽
		 */
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 可以在非菜单部分(主页面)拖拽
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        // //可以在菜单部分拖拽

        initView();
        // 加载菜单和主页
        initFragment();

        /**
         * 判断是否是push消息过来的
         */
        String messgeTag = getIntent().getStringExtra("tag");
        if (!TextUtils.isEmpty(messgeTag)) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        }
        String initAppBean = CacheUtils.getString(this, "initAppBean");
        if (!TextUtils.isEmpty(initAppBean)) {
            mShareInfo = new Gson().fromJson(initAppBean, InitAppBean.class).getShareInfo();
        }
        initShare();

        //检测版本信息
        controller.getServiceManager().getAasService().getVersionUpdateInformation(this);
    }

    private void initView() {
        //从登录后的缓存中获取融云Token
        String loginrongCloudToken = CacheUtils.getString(getApplicationContext(), "rong_cloud_token");
        if (!TextUtils.isEmpty(loginrongCloudToken)) {
            rongConnect(loginrongCloudToken);
        } else {//重新获取融云Toke
            LoginUser loginUser = controller.getCacheManager().getLoginUser();
            int uid = loginUser.getUid();
            if (uid != 0 && uid != -1) {//登陆后才获取融云Token
                controller.getServiceManager().getAasService().resetRongToken(this);
            }
        }
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        // 1、开启事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 2-1、加载切换主页面
        transaction.replace(R.id.main_container_content, new ContentFragment1_3_0(), TAG_CONTENT);
        // 2-1、加载切换菜单页面
        transaction.replace(R.id.main_container_menu, new MenuFragment(), TAF_MENU);

        // 3、提交事务
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获按下返回键事件
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "HomeActivity");
                ExitionUtil.getInstance().setEnable();
            } /** else {
             仍然将之前的页面设置为Exit Page
             } **/

            if (RongIMClient.getInstance() != null)
                RongIMClient.getInstance().disconnect();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if (null == mDialog) {
            mDialog = new AdvertisingDialog(this, R.style.Theme_Hold_Dialog_Base);
        }
        if (null != mDialog && null != args.getParcelable("popupInfo")) {
            PopupInfo popupInfo = args.getParcelable("popupInfo");
            mDialog.setPopupInfo(popupInfo);
        }
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            //app 从后台唤醒，进入前台
            ILog.i("zxj", "app 从后台唤醒，进入前台");
            long oldTime = CacheUtils.getLong(this, "currentTime");
            long currentTime = System.currentTimeMillis();
            long time = currentTime - oldTime;
            if (time > 60 * 60 * 1000) {//60*60*1000
                //联网请求启动广告
                controller.getServiceManager().getHomeService().startAdvertising(this);
            }
        }
        mLogger = AppEventsLogger.newLogger(this);
        mLogger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP);
        MobclickAgent.onResume(this);
        //取消通知栏
        NotificationManager notificationManager = (NotificationManager) IruluApplication.getInstance().getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(ConstantsUtils.notificationId);

        if (!DbHelper.getInstance().isDBOpen()) {
            DbHelper.getInstance().open();
        }
        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "HomeActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean isbackground = IruluApplication.getInstance().isbackground();
        if (isbackground) {
            ILog.i("zxj", "app 进入后台");
            //app 进入后台
            isActive = false;//全局变量isActive = false 记录当前已经进入后台
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtils.setString(this, "category", null);
        CacheUtils.setString(this,"CategoryHomeCache",null);
        CacheUtils.setString(this, "CountryInformationCache", null);
        if (!TextUtils.isEmpty(CacheUtils.getString(this, "token"))) {
            CacheUtils.setLong(this, "outtime", System.currentTimeMillis());
        }

        //正常退出
        DbHelper.getInstance().updateStatus(IruluApplication.getInstance().getNo(), 1);
        DbHelper.getInstance().close();
    }

    /**
     * 融云连接
     *
     * @param rongCloudToken 融云Token
     */
    private void rongConnect(String rongCloudToken) {
        try {
            mRongIMClient = RongIMClient.connect(rongCloudToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    ILog.d(TAG, "--connect--onSuccess----userId---" + userId);// 自己的用户ID(登录后，登录用户的ID)
//                    初始化未读总数
                    int totalUnreadCount = mRongIMClient.getTotalUnreadCount();// 未读消息总数
                    controller.postTotalUnreadCountCallback(totalUnreadCount);
                    ReceiveMessageListener.getInstance().userId = userId;// 给谁发消息的用户ID
                    ReceiveMessageListener.getInstance().setRongIMClient(mRongIMClient);
                    ReceiveMessageListener.getInstance().registerReceiveMessageListener();// 注册接受消息监听事件
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    ILog.d(TAG, "--connect--errorCode-------" + errorCode);
                }

                @Override
                public void onTokenIncorrect() {
                    ILog.d(TAG, "融云Token错啦，正在从新获取Token");
                    controller.getServiceManager().getAasService().resetRongToken(HomeActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initShare() {
        mProductShareDialog = new ProductShareDialog(this) {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mProductShareDialog.dismiss();
                // radiogroup一共有三个子控件,中间的是分割线
                if (checkedId == group.getChildAt(0).getId()) {
                    ((RadioButton) group.getChildAt(0)).setChecked(false);
                    shareFacebook();
                } else if (checkedId == group.getChildAt(2).getId()) {
                    ((RadioButton) group.getChildAt(2)).setChecked(false);
                    shareTwitter();
                }
            }
        };
        initFacebook();
        initTwitter();
    }

    public void initFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void initTwitter() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterUtils.TWITTER_API_KEY, TwitterUtils.TWITTER_API_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void shareFriend() {
        mProductShareDialog.show();
    }

    public void shareFacebook() {
        if (mShareInfo != null) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(mShareInfo.getTitle())
                        .setContentDescription(mShareInfo.getContent())
                        .setContentUrl(Uri.parse(mShareInfo.getLink()))
                        .setImageUrl(Uri.parse(mShareInfo.getIcon()))
                        .build();
                mShareDialog.show(this, linkContent);
            }
        }
    }

    public void shareTwitter() {
        if (mShareInfo != null) {
            try {
                URL url = new URL(mShareInfo.getLink());
                TweetComposer.Builder builder = new TweetComposer.Builder(this)
                        .url(url)
                        .text(mShareInfo.getContent())
                        .image(Uri.parse(mShareInfo.getIcon()));
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case RESET_RONG_TOKEN:
                String resetrongtoken = returnObj.toString();
                rongConnect(resetrongtoken);
                break;
            case START_ADVERTISING:
                StartAdvertising startAdvertising = (StartAdvertising) returnObj;
                final AdInfo adInfo = startAdvertising.getAdInfo();//启动广告信息
                PopupInfo popupInfo = startAdvertising.getPopupInfo();//弹窗广告信息
//					AdvertisingDialog dialog = new AdvertisingDialog(BaseActivity.this,popupInfo);
//					mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//设定为系统级警告，关键
                String image = popupInfo.getImage();
                if (null == mDialog && !TextUtils.isEmpty(image)) {
                    Bundle data = new Bundle();
                    data.putParcelable("popupInfo", popupInfo);
                    showDialog(ConstantsUtils.DIALOG_PROGRESS, data);
                }
                if (null != mDialog && !mDialog.isShowing() && null != popupInfo) {
                    mDialog.show();
                }
                CacheUtils.setLong(this, "currentTime", currentTime);
                break;
            case CHECK_VERSION:
                /**
                 * 成功获取版本更新的数据 来判断是否更新版本
                 */
                NewVersionInfors nvi = (NewVersionInfors) returnObj;
                String version = nvi.getVersion();
                if (!TextUtils.isEmpty(version) && !CommonUtil.getVersionName().equals(version)) {
                    setVersionUpdateDialog(nvi);
                } else {
                    //联网请求启动广告
                    currentTime = System.currentTimeMillis();
                    controller.getServiceManager().getHomeService().startAdvertising(this);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 通知栏内显示下载进度条
     */
    private void notificationInit() {
        Intent intent = new Intent(this, HomeActivity.class);// 点击进度条，进入程序
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher;
        mNotification.tickerText = getString(R.string.pull_to_refresh_refreshing_label);
        mNotification.contentView = new RemoteViews(getPackageName(), R.layout.notification_version_update);// 通知栏中进度布局

        mNotification.contentIntent = pIntent;
    }

    /**
     * 下载更新
     */
    private void downLoad(final String url) {
        HttpUtils utils = new HttpUtils();
        utils.download(url, "/sdcard/irulu.apk", new RequestCallBack<File>() {
            /**
             * 下载成功
             * @param arg0
             */
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file:///sdcard/irulu.apk"), "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);
                android.os.Process.killProcess(android.os.Process.myPid());
                mNotificationManager.cancel(0);
            }

            /**
             * 下载中
             * @param total
             * @param current
             * @param isUploading
             */
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                mNotification.contentView.setTextViewText(R.id.notification_version_update_tv, (int) (current * 100 / total) + "%");
                mNotification.contentView.setProgressBar(R.id.notification_version_update_pb, (int) total, (int) current, false);
                mNotificationManager.notify(0, mNotification);
                super.onLoading(total, current, isUploading);
            }
            /**
             * 下载失败
             * @param arg0
             * @param arg1
             */
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(HomeActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置版本更新的对话框
     */
    private void setVersionUpdateDialog(final NewVersionInfors infors) {
        updateDialog = new Dialog(this, R.style.MyAlertDialog);
        View subView = View.inflate(this, R.layout.dialog_version_update, null);

        TextView titleView = (TextView) subView.findViewById(R.id.title);

        LinearLayout content = (LinearLayout) subView.findViewById(R.id.content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, UIUtils.dip2px(10));

        String[] strings = infors.getContent().split("\n");

        for (String string : strings) {
            TextView textView = new TextView(HomeActivity.this);
            textView.setTextSize(11);
            textView.setTextColor(HomeActivity.this.getResources().getColor(R.color.update_content_text_color));
            textView.setText(string);
            textView.setLayoutParams(params);
            content.addView(textView);
        }

        updateType = infors.getUpdateType();

        ImageView later = (ImageView) subView.findViewById(R.id.later);
        if (3 == updateType) {
            later.setVisibility(View.INVISIBLE);
            updateDialog.setOnDismissListener(this);
        }

        later.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        Button getNow = (Button) subView.findViewById(R.id.getNow);
        getNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //开始下载新版本的APP
                notificationInit();
                downLoad(infors.getUrl());
                updateDialog.dismiss();
            }
        });

        updateDialog.setContentView(subView);

        updateDialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = updateDialog.getWindow();
        WindowManager wm = (WindowManager) HomeActivity.this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        int SW = wm.getDefaultDisplay().getWidth();
        int SH = wm.getDefaultDisplay().getHeight();

        lp.width = (SW * 4) / 5;
        lp.x = (SW - lp.width) / 2; // 新位置X坐标
        lp.height = (773 * lp.width) / 667; // 高度
//        lp.alpha = 0.7f; // 透明度
        lp.y = (SH - lp.height) / 2;

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(UIUtils.dip2px(35), (lp.height * 3) / 7, 0, 0);
        titleView.setLayoutParams(params2);

        dialogWindow.setAttributes(lp);

        updateDialog.show();
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {

        if (action == ServiceListener.ActionTypes.CHECK_VERSION && errorCode != ErrorCodeUtils.NO_NET_RESPONSE) {
            controller.getServiceManager().getHomeService().startAdvertising(this);
        }

        if (returnObj != null) {
            UIUtils.getToastShort(this, returnObj.toString());
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    public void test(View v){
        Intent intent=new Intent(this, NewArrivalsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (3 == updateType) {
            Toast.makeText(HomeActivity.this, "You need update the application to continue using.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}