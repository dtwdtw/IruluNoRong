package com.wf.irulu.common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.wf.irulu.common.bean.AdInfo;
import com.wf.irulu.common.bean.PopupInfo;
import com.wf.irulu.common.bean.StartAdvertising;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.advertising.dialog.AdvertisingDialog;

/**
 * @描述: 弹出广告的联网请求接口的实现类
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.base
 * @类名:MyServiceListener
 * @作者: 左杰
 * @创建时间:2015/12/2 9:28
 */
public class AdvertisingServiceListener implements ServiceListener {
    private Context mContext;
    private AdvertisingDialog dialog;
    private AdvertisingServiceListener() {
    }

    private AdvertisingServiceListener(Context context,AdvertisingDialog dialog) {
        this.mContext = context;
        this.dialog = dialog;
    }

    private static AdvertisingServiceListener listener = null;

    public static AdvertisingServiceListener getInstance(Context context,AdvertisingDialog dialog) {
        if (listener == null)
            listener = new AdvertisingServiceListener(context,dialog);
        return listener;
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case START_ADVERTISING:
                StartAdvertising startAdvertising = (StartAdvertising) returnObj;
                final AdInfo adInfo = startAdvertising.getAdInfo();//启动广告信息
                PopupInfo popupInfo = startAdvertising.getPopupInfo();//弹窗广告信息
//                AdvertisingDialog dialog = new AdvertisingDialog(mContext, popupInfo);
//                dialog.setPopupInfo(popupInfo);
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//设定为系统级警告，关键
                if(null == dialog){
                    Bundle data = new Bundle();
                    data.putParcelable("popupInfo", popupInfo);
                    ((Activity)mContext).showDialog(ConstantsUtils.DIALOG_PROGRESS, data);
                }
                if(null != dialog && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {

    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
