package com.wf.irulu.module.setting.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.NewVersionInfors;
import com.wf.irulu.common.utils.CommonUtil;
import com.wf.irulu.common.utils.DataCleanManager;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.FileUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.manager.CacheManager;
import com.wf.irulu.module.aas.activity.LoginActivity;

import java.io.File;

/**
 * Created by daniel on 2015/10/30.
 * 设置页面
 */
public class SettingActivity extends CommonTitleBaseActivity implements ServiceListener {

    private TextView wipe_cache_tv;
    private TextView update_tv;
    private AlertDialog wipeCacheDialog;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification;
    private RelativeLayout update_rl;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if ((Integer) msg.obj == 0) {
                SystemClock.sleep(1000);
                wipeCacheDialog.dismiss();
                wipe_cache_tv.setText("0.0B");
            }
        }

        ;
    };

    @Override
    protected String setWindowTitle() {
        return getString(R.string.setting);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        RelativeLayout change_password_rl = (RelativeLayout) findViewById(R.id.setting_change_password_rl);
        RelativeLayout wipe_cache_rl = (RelativeLayout) findViewById(R.id.setting_wipe_cache_rl);
        RelativeLayout feedback_rl = (RelativeLayout) findViewById(R.id.setting_feedback_rl);
        update_rl = (RelativeLayout) findViewById(R.id.setting_update_rl);
        RelativeLayout terms_service_rl = (RelativeLayout) findViewById(R.id.setting_terms_of_service_rl);
        RelativeLayout privacy_policy_rl = (RelativeLayout) findViewById(R.id.setting_privacy_policy_rl);
        RelativeLayout about_irulu_rl = (RelativeLayout) findViewById(R.id.setting_about_irulu_rl);

        wipe_cache_tv = (TextView) findViewById(R.id.setting_wipe_cache_tv);
        update_tv = (TextView) findViewById(R.id.setting_update_tv);

        change_password_rl.setOnClickListener(this);
        wipe_cache_rl.setOnClickListener(this);
        feedback_rl.setOnClickListener(this);
        update_rl.setOnClickListener(this);
        terms_service_rl.setOnClickListener(this);
        privacy_policy_rl.setOnClickListener(this);
        about_irulu_rl.setOnClickListener(this);

    }

    @Override
    public void initData() {
        update_tv.setText("V" + CommonUtil.getVersionName());
        /**
         * 设置缓存大小
         */
        try {
            File cacheDir = getCacheDir();
            File fileDir = new File(CacheManager.SD_APP_DIR);
            long size1 = DataCleanManager.getFolderSize(cacheDir);
            long size2 = DataCleanManager.getFolderSize(fileDir);
            String cacheSize =  DataCleanManager.getFormatSize(size1 + size2);
            wipe_cache_tv.setText(cacheSize);
        } catch (Exception e) {
            wipe_cache_tv.setText("40.0B");
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 进入更改密码页面
             */
            case R.id.setting_change_password_rl:
                if (getIntent().getBooleanExtra("flags", false)) {
                    Intent change = new Intent(this, ChangePasswordActivity.class);
                    startActivity(change);
                } else {
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("flag", "changepassword");
                    startActivity(login);
                }
                break;
            case R.id.setting_wipe_cache_rl:
                /**
                 * 清理缓存
                 */
                if ("0.0B".equals(wipe_cache_tv.getText().toString().trim())) {
                    return;
                } else {
                    setWipeCacheDialog();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            wipeCache();
                        }
                    }).start();
                }
                break;
            case R.id.setting_feedback_rl:
                /**
                 * 进入建议页面
                 */
                Intent feedback = new Intent(this, FeedBackActivity.class);
                startActivity(feedback);
                break;
            case R.id.setting_update_rl:
                /**
                 * 获取更新版本的数据
                 */
                update_rl.setClickable(false);
                PageLoadDialog.showDialogForLoading(this, true, false);
                controller.getServiceManager().getAasService().getVersionUpdateInformation(this);

                break;
            case R.id.setting_terms_of_service_rl:
                Intent terms = new Intent(this, TermsServiceActivity.class);
                startActivity(terms);
                break;
            case R.id.setting_privacy_policy_rl:
                Intent privacy = new Intent(this, PrivacyPolicyActivity.class);
                startActivity(privacy);
                break;
            case R.id.setting_about_irulu_rl:
                Intent about = new Intent(this, AboutIruluActivity.class);
                startActivity(about);
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case CHECK_VERSION:
                PageLoadDialog.hideDialogForLoading();
                /**
                 * 成功获取版本更新的数据 来判断是否更新版本
                 */
                NewVersionInfors nvi = (NewVersionInfors) returnObj;
                String version = nvi.getVersion();
                update_rl.setClickable(true);
                if (TextUtils.isEmpty(version) || CommonUtil.getVersionName().equals(version)) {
                    Toast t = Toast.makeText(this, "Your application is up to date", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                } else {
                    setVersionUpdateDialog(nvi);
                }
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        /**
         * 联网失败
         */
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(getString(R.string.no_network));
            return;
        }
        switch (action) {
            case CHECK_VERSION:
                update_rl.setClickable(true);
                showToast((String) returnObj);
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
    }

    /**
     * 清理缓存的对话框
     */
    private void setWipeCacheDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_wipe_cache, null);
        ab.setView(view);
        wipeCacheDialog = ab.create();
        wipeCacheDialog.show();
    }

    /**
     * 清理缓存
     */
    private void wipeCache() {
        // 清除缓存操作
        File cacheDir = getCacheDir();
        DataCleanManager.deleteFolderFile(cacheDir.getPath(), false);
        FileUtils.deleteAllFile(CacheManager.SD_APP_DIR);
        Message msg = Message.obtain();
        msg.obj = 0;
        handler.sendMessage(msg);
    }

    /**
     * 设置版本更新的对话框
     */
    private void setVersionUpdateDialog(final NewVersionInfors infors) {
        final Dialog dialog = new Dialog(this, R.style.MyAlertDialog);
        View subView = View.inflate(this, R.layout.dialog_version_update, null);

        TextView titleView = (TextView) subView.findViewById(R.id.title);

        LinearLayout content = (LinearLayout) subView.findViewById(R.id.content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, UIUtils.dip2px(10));

        String[] strings = infors.getContent().split("\n");

        for (String string : strings) {
            TextView textView = new TextView(SettingActivity.this);
            textView.setTextSize(11);
            textView.setTextColor(SettingActivity.this.getResources().getColor(R.color.update_content_text_color));
            textView.setText(string);
            textView.setLayoutParams(params);
            content.addView(textView);
        }

        Button later = (Button) subView.findViewById(R.id.later);
        if (3 == infors.getUpdateType()) {
            later.setVisibility(View.INVISIBLE);
        }

        later.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button getNow = (Button) subView.findViewById(R.id.getNow);
        getNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //开始下载新版本的APP
                notificationInit();
                downLoad(infors.getUrl());
                dialog.dismiss();
            }
        });

        dialog.setContentView(subView);

        dialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = dialog.getWindow();
        WindowManager wm = (WindowManager) SettingActivity.this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        int SW = wm.getDefaultDisplay().getWidth();
        int SH = wm.getDefaultDisplay().getHeight();

        lp.width = (SW * 4) / 5;
        lp.x = (SW - lp.width) / 2; // 新位置X坐标
        lp.height = (773 * lp.width) / 667; // 高度
//        lp.alpha = 0.7f; // 透明度
        lp.y = (SH - lp.height) / 2;

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(UIUtils.dip2px(35), (lp.height * 2) / 5, 0, 0);
        titleView.setLayoutParams(params2);

        dialogWindow.setAttributes(lp);

        dialog.show();
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
                showToast(getString(R.string.failed));
            }
        });
    }
    /**
     * 通知栏内显示下载进度条
     */
    private void notificationInit() {
        Intent intent = new Intent(this, SettingActivity.class);// 点击进度条，进入程序
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher;
        mNotification.tickerText = getString(R.string.pull_to_refresh_refreshing_label);
        mNotification.contentView = new RemoteViews(getPackageName(), R.layout.notification_version_update);// 通知栏中进度布局

        mNotification.contentIntent = pIntent;
    }
}
