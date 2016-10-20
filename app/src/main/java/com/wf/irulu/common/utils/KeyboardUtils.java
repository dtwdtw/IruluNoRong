package com.wf.irulu.common.utils;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * @描述: Keyboard utilities
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.common.utils
 * @类名:KeyboardUtils
 * @作者: 左杰
 * @创建时间:2015/11/11 16:59
 */
public class KeyboardUtils {
    /**
     * Hide soft input method manager
     *
     * @param view
     * @return view
     */
    public static View hideSoftInput(final View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    /**
     * <p>键盘隐藏/显示切换</p>
     */
    public static void hideSoftInputNotAlways(final View view) {
        InputMethodManager imm = (InputMethodManager) (view.getContext().getSystemService(INPUT_METHOD_SERVICE));
        imm.showSoftInput(view, HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftInput(final Context context) {
        InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
        final View currentFocusView = ((Activity)context).getCurrentFocus();
        if (currentFocusView != null) {
            final IBinder windowToken = currentFocusView.getWindowToken();
            if (inputMethodManager != null && windowToken != null) {
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    public static void toggleSoftInput(final Context context) {
        InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(0, HIDE_NOT_ALWAYS);
        }
    }

    public static void showSoftInput(final Context context, final View view) {
        InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            if (!inputMethodManager.isActive()) {
                inputMethodManager.toggleSoftInput(0, HIDE_NOT_ALWAYS);
            }
            inputMethodManager.showSoftInput(view, 0);
        }

    }

    public static boolean isActive(final Activity activity){
        return activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
    }
}

