package com.wf.irulu.module.product.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.wf.irulu.R;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.product.listener.ShareListener;

/**
 * Created by XImoon on 15/11/3.
 */
public abstract class ProductShareDialog extends AlertDialog implements RadioGroup.OnCheckedChangeListener{

    private Context mContext;
    private ShareListener mListener;

    public ProductShareDialog(Context context) {
        super(context,R.style.Theme_Hold_Dialog_Base);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mContext.getString(R.string.share_to));
        setContentView(R.layout.dialog_share_product);
        RadioGroup shareRgPlatform = (RadioGroup) findViewById(R.id.share_rg_platform);
        shareRgPlatform.setOnCheckedChangeListener(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 获得窗体
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ConstantsUtils.DISPLAYW / 3 * 2;
        window.setAttributes(params);
//        window.setWindowAnimations(R.style.DialogPopupAnimation);
    }
}
