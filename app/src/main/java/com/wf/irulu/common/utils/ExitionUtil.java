package com.wf.irulu.common.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by imaster on 16/1/22.
 */
public class ExitionUtil {

    private boolean isRecoedAble;

    private Handler handler;

    private static ExitionUtil instance;

    public static ExitionUtil getInstance() {
        if (null == instance) {
            instance = new ExitionUtil();
        }

        return instance;
    }

    private ExitionUtil() {
        isRecoedAble = false;

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case -1:
                        isRecoedAble = false;
                        break;
                }
            }
        };
    }

    public void setEnable() {
        isRecoedAble = true;

        handler.sendEmptyMessageDelayed(-1, 500);
    }

    public boolean isRecoedAble() {
        return isRecoedAble;
    }
}
