package com.wf.irulu.common.utils;

import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.IruluApplication;

import java.util.HashMap;

/**
 * Created by XImoon on 15/11/18.
 */
public class CustomerAnalystic {

    public synchronized static void analystic(CustomerEvent event, AppEventsLogger mlogger,HashMap<String,String> map) {
//        if (SettingManager.DEBUG){
//            return;
//        }
        // 获取动作事件名称
        String action = String.valueOf(event);
        // 友盟
        MobclickAgent.onEvent(IruluApplication.getInstance(), action,map);
        // facebook
        if (mlogger != null) {
            mlogger.logEvent(action);
        }
        // ga
        IruluApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder().setCategory("Action").setAction(action).build());
    }

    public synchronized static void analystic(CustomerEvent event, AppEventsLogger mlogger) {
//        if (SettingManager.DEBUG){
//            return;
//        }
        // 获取动作事件名称
        String action = String.valueOf(event);
        // 友盟
        MobclickAgent.onEvent(IruluApplication.getInstance(), action, ConstantsUtils.mVersionAnalystics);
        // facebook
        if (mlogger != null) {
            mlogger.logEvent(action);
        }
        // ga
        IruluApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder().setCategory("Action").setAction(action).build());

        AppsFlyerLib.trackEvent(IruluApplication.getInstance(), action, null);
    }
}
