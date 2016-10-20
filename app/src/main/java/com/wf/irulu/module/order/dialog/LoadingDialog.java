package com.wf.irulu.module.order.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wf.irulu.R;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.order.dialog
 * @类名:LoadingDialog
 * @作者: Yuki
 * @创建时间:2015/12/11
 */
public class LoadingDialog extends Dialog{


    public LoadingDialog(Context context) {
        super(context,R.style.loading_dialog_style);
        initView(context);

    }

    public void initView(Context context){
        setCancelable(true);//但是这样就不能响应返回键的事件了
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void show() {
        if (this != null && !isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (this != null && isShowing()) {
            super.dismiss();
        }
    }
}
