package com.wf.irulu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.bean.Exittion;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 启动页面
 */
public class MainActivity extends BaseActivity implements ServiceListener, AppsFlyerConversionListener {

    private int no = 1;
    private final int StoragePermissionRequest = 22;
    private boolean isFirst;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==StoragePermissionRequest){
//            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
//
//            }
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED
                && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
            if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                List<Exittion> exittions = DbHelper.getInstance().queryAll();

                if (null == exittions || 0 == exittions.size()) {
                    no = 1;
                } else {
                    no = exittions.get(exittions.size() - 1).getNo() + 1;
                }
                DbHelper.getInstance().insert(no);
                IruluApplication.getInstance().setNo(no);
            }

            CacheUtils.remove(this, "StateInformationCache");
            // 设置appsflyer接收消息
            AppsFlyerLib.registerConversionListener(this, this);
            //联网请求初始化
            isFirst = CacheUtils.getBoolean(MainActivity.this, getString(R.string.version_name) + "isfirst", true);
            controller.getServiceManager().getHomeService().appInit(isFirst, this);
            // 如果不是第一次加载,则直接进入跳转，否则等待数据请求成功后方可跳转
            if (!isFirst) {
                startAPP();
            }
        } else {
            this.setTheme(android.R.style.ThemeOverlay_Material_Dark);
            Toast.makeText(MainActivity.this, "You need to access all of permission to use the app!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_welcome);

    }

    @Override
    public void initView() {
        if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_PHONE_STATE}, StoragePermissionRequest);

        } else {
            if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                List<Exittion> exittions = DbHelper.getInstance().queryAll();

                if (null == exittions || 0 == exittions.size()) {
                    no = 1;
                } else {
                    no = exittions.get(exittions.size() - 1).getNo() + 1;
                }
                DbHelper.getInstance().insert(no);
                IruluApplication.getInstance().setNo(no);
            }

            CacheUtils.remove(this, "StateInformationCache");
            // 设置appsflyer接收消息
            AppsFlyerLib.registerConversionListener(this, this);
            //联网请求初始化
            isFirst = CacheUtils.getBoolean(MainActivity.this, getString(R.string.version_name) + "isfirst", true);
            controller.getServiceManager().getHomeService().appInit(isFirst, this);
            // 如果不是第一次加载,则直接进入跳转，否则等待数据请求成功后方可跳转
            if (!isFirst) {
                startAPP();
            }
        }
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {

    }

    private void startAPP() {
        controller.mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                //判断是否是push消息传过来的tag
                String messgeTag = getIntent().getStringExtra("tag");
                intent = new Intent(MainActivity.this, HomeActivity.class);
                if (!TextUtils.isEmpty(messgeTag)) {
                    intent.putExtra("tag", "tag");
                }
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    private void InitAPP() {
        controller.mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //            Query all exition record from database


                Intent intent = new Intent(MainActivity.this, RefrenceActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case START_ADVERTISING:
//				StartAdvertising startAdvertising = (StartAdvertising) returnObj;
//				final AdInfo adInfo = startAdvertising.getAdInfo();//启动广告信息
//				PopupInfo popupInfo = startAdvertising.getPopupInfo();//弹窗广告信息
//				String image = popupInfo.getImage();
                /*final String image = adInfo.getImage();
                    controller.mMainHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if(TextUtils.isEmpty(image)){//如果启动广告信息的广告图片为空
								Boolean isFirst = CacheUtils.getBoolean(MainActivity.this, getString(R.string.version_name) + "isfirst", true);
								Intent intent = null;
								if (isFirst) {
									intent = new Intent(MainActivity.this,HomeActivity.class);
								}else{
									String messgeTag = getIntent().getStringExtra("tag");
									intent = new Intent(MainActivity.this, HomeActivity.class);
									if (!TextUtils.isEmpty(messgeTag)) {
										intent.putExtra("tag", "tag");
									}
								}
								startActivity(intent);
							}else{//如果启动广告信息的广告图片不为空，跳转到启动广告信息页面
								AdvertisingInfoActivity.startAdvertisingInfoActivity(MainActivity.this,adInfo);
							}
							finish();
						}
					}, 500);*/
                break;
            case APP_INIT:
                // 第一次加载时候
                InitAPP();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case APP_INIT:
                controller.getServiceManager().getHomeService().appInit(isFirst, this);
                break;
            default:
                break;
        }
        if (returnObj != null) {
            UIUtils.getToastShort(this, returnObj.toString());
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    public void onInstallConversionDataLoaded(Map<String, String> map) {
        if (map.containsKey("af_dp")) {
            String deepLink = (String) map.get("af_dp");
            if (deepLink.contains("amp;"))
                deepLink = deepLink.replace("amp;", "");
            Intent intent = new Intent();
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            HashMap<String, String> mParams = new HashMap<String, String>();
            if (deepLink != null) {
                String url = (deepLink.split("\\?"))[1];
                String[] params = url.split("&");
                for (int i = 0; i < params.length; i++) {
                    String[] value = params[i].split("=");
                    if (value.length == 2) {
                        try {
                            mParams.put(value[0].trim(), URLDecoder.decode(value[1].trim(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if ("2".equals(mParams.get("actionCategory"))) {
                    intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                    try {
                        intent.putExtra("id", Integer.parseInt(mParams.get("content")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else if ("3".equals(mParams.get("actionCategory"))) {
                    intent.putExtra("moreUrl", mParams.get("content"));
                    intent.putExtra("title", mParams.get("name"));
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onInstallConversionFailure(String s) {

    }

    @Override
    public void onAppOpenAttribution(Map<String, String> map) {

    }

    @Override
    public void onAttributionFailure(String s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
