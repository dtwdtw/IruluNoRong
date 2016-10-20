package com.wf.irulu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.PermissionChecker;

import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.common.bean.Exittion;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.component.rongcloud.Listener.ReceiveMessageListener;
import com.wf.irulu.component.rongcloud.ReceivePushEvent;
import com.wf.irulu.component.rongcloud.message.DiscountMessage;
import com.wf.irulu.component.rongcloud.message.OrderMessage;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.homenew1_3_0.FontsOverride;

import java.security.acl.Permission;
import java.util.List;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.message.InformationNotificationMessage;

/**
 * @描述: 应用程序的入口
 * @项目名: irulu
 * @包名:com.wf.irulu
 * @类名:IruluApplication
 * @作者: 左西杰
 * @创建时间:2015-5-27 上午10:34:44
 */

public class IruluApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler {


    boolean isFirst;
    private static final String TAG = IruluApplication.class.getCanonicalName();
    private static IruluApplication instance = null;
    public static RongIMClient mRongIMClient;
    //获取到主线程的handler
    private static Handler mMainThreadHandler;
    //获取到主线程轮询器
    private static Looper mMainThreadLooper;
    //获取到主线程的Thread
    private static Thread mMainThread;
    //获取到主线程的id
    private static long mMainThreadId;
    private Picasso picasso = null;
    public GoogleAnalytics analytics;
    private Tracker mTracker;
    public Uri mDeferredDeepLink;

    public static boolean isAcitve = true;

    private int no = 1;

    public void setNo(int no){
        this.no=no;
    }
    public int getNo() {
        return no;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();


//        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/OpenSans-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "fonts/OpenSans-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/OpenSans-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "DEFAULT_BOLD", "fonts/OpenSans-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "sDefaultTypeface", "fonts/OpenSans-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "BOLD", "fonts/OpenSans-Regular.ttf");

        instance = this;

        this.mMainThreadHandler = new Handler();
        this.mMainThreadLooper = getMainLooper();
        this.mMainThread = Thread.currentThread();
        this.mMainThreadId = mMainThread.getId();

        // MobclickAgent.setDebugMode( true );
        // 友盟错误统计
//        Thread.currentThread().setUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        // 异常捕获及日志记录
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        /**
         * IMLib SDK调用第一步 初始化 context上下文
         */

        isFirst = CacheUtils.getBoolean(IruluApplication.this, getString(R.string.version_name) + "isfirst", true);
        if (!isFirst) {
            RongIMClient.init(this);
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

                ReceiveMessageListener.getInstance().init(this);
                // ReceiveMessageListener.getInstance().registerReceiveMessageListener();//注册接受消息监听事件
                ReceivePushEvent.init(this);
                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
                // 网络检测广播注册
                // NetworkChangedReceive.beginListenNetworkChange();
                try {
                    // 注册自定义消息类型
                    RongIMClient.registerMessageType(OrderMessage.class);
                    RongIMClient.registerMessageType(DiscountMessage.class);
                    RongIMClient.registerMessageType(InformationNotificationMessage.class);
                } catch (AnnotationNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //Add a exition information for already



        }

//        getAppInitLocation();
        initDeepLinkAnalystic();

        AppsFlyerLib.setAppsFlyerKey("HNRpkgD9imdPZWXTrv3Rca");
        AppsFlyerLib.sendTracking(getApplicationContext());

        // facebook的初始化
        FacebookSdk.sdkInitialize(this);
    }

    public static IruluApplication getInstance() {
        return instance;
    }

    /**
     * 对外暴露一个主线程的handelr
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 对外暴露一个主线程的Looper
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    /**
     * 对外暴露一个主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 对外暴露一个主线程ID
     */
    public static long getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取Picasso对象
     */
    public Picasso getPicasso() {
        if (picasso == null) {
            cancelPicassoCache();
        }
        return picasso;
    }

    /**
     * 清除缓存并创建对象
     */
    public void cancelPicassoCache() {
        if (null != picasso)
            picasso = null;
        LruCache lruCache = new LruCache(getApplicationContext());
        lruCache.clear();
        Picasso.Builder builder = new Picasso.Builder(getApplicationContext()).memoryCache(lruCache);
        lruCache.clear();
        picasso = builder.build();
    }

    /**
     * 获得当前进程号
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 卸载应用
     */
    public void uninstall(Context context) {
        Uri uri = Uri.parse("package:com.wf.irulu");
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 是否在后台
     *
     * @return
     */
    public boolean isbackground() {
        String packageName = "com.wf.irulu";
        ActivityManager activityManager = (ActivityManager) IruluApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            String topPackageName = tasksInfo.get(0).topActivity.getPackageName();
            ILog.i(TAG, "包名:" + topPackageName);
            // 应用程序位于堆栈的顶层
            if (!packageName.equals(topPackageName)) {
                return true;
            }
        }
        return false;

       /* ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(this.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    System.out.println("Background App:"+appProcess.processName);
                    return true;
                } else {
                    System.out.println("Foreground App:"+appProcess.processName);
                    return false;
                }
            }
        }
        return false;*/
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        MobclickAgent.onKillProcess(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void getAppInitLocation() {
        LocationManager mLocationManager = (LocationManager) instance.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);//设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
        String providerName = mLocationManager.getBestProvider(criteria, true); //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        double[] locations = new double[]{0, 0};
        if (providerName != null) {
            Location location = mLocationManager.getLastKnownLocation(providerName);
            if (location != null) {
                locations = new double[]{location.getLongitude(), location.getLatitude()};//获取维度信息
            } else {
            }

        }
        if (locations[0] == 0 && locations[1] == 0) {

        } else {

        }

        IruluController.getInstance().getConfigXml().save("X-LNG/LAT", "LNG/" + String.valueOf(locations[0] + " LAT/" + String.valueOf(locations[1])));
    }

   /* @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/

    public void initDeepLinkAnalystic() {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        analytics.setDryRun(false);
        mTracker = analytics.newTracker(getString(R.string.google_tracker_id)); // Replace with actual tracker/property Id
        mTracker.enableExceptionReporting(true);
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.enableAutoActivityTracking(true);
        AppLinkData.fetchDeferredAppLinkData(this,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        if (appLinkData != null) {
                            mDeferredDeepLink = appLinkData.getTargetUri();
                        }
                    }
                }
        );
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(getString(R.string.google_tracker_id));
//            mTracker.enableExceptionReporting(true);
//            mTracker.enableAdvertisingIdCollection(true);
//            mTracker.enableAutoActivityTracking(true);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}