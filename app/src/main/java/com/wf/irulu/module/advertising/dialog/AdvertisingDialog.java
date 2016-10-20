package com.wf.irulu.module.advertising.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.PopupInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.home.activity.ShufflingFigureActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @描述: 弹窗广告信息
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.advertising.dialog
 * @类名:AdvertisingDialog
 * @作者: 左杰
 * @创建时间:2015/11/25 14:46
 */
public class AdvertisingDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private PopupInfo popupInfo;
    private ImageView mAdvertisingImage;
    private int width = ConstantsUtils.DISPLAYW * 4 / 5;
    private int height = ConstantsUtils.DISPLAYH * 400 / 667;

    public AdvertisingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public AdvertisingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public AdvertisingDialog(Context context, PopupInfo popupInfo) {
        super(context, R.style.Theme_Hold_Dialog_Base);
        setCanceledOnTouchOutside(false);//点击灰色区域不可取消
        this.mContext = context;
        this.popupInfo = popupInfo;
    }

    public void setPopupInfo(PopupInfo popupInfo) {
        this.popupInfo = popupInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_advertising);
        findView();
        initData();
    }


    public void findView() {
        mAdvertisingImage = (ImageView) findViewById(R.id.advertising_iv);
        LinearLayout advertisingLayout = (LinearLayout) findViewById(R.id.advertising_layout);
        ImageView advertisingDel = (ImageView) findViewById(R.id.advertising_del);

        mAdvertisingImage.setOnClickListener(this);
        advertisingDel.setOnClickListener(this);
        //设置控件的宽高
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(width, height);
        advertisingLayout.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件
    }

    private void initData() {
        if (null != popupInfo) {
            String image = popupInfo.getImage();
            String str = "?imageView2/0/w/" + width + "/h/" + height + "/format/jpg/interlace/1/q/75";
            IruluApplication.getInstance().getPicasso().load(image + str).into(mAdvertisingImage);
        }
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advertising_iv:
                Intent slideIntent = new Intent();
                String type = popupInfo.getType();
                String content = popupInfo.getContent();
                if (type.equals("1")) {
                    slideIntent.setClass(mContext, CategoryActivity.class);
                } else if (type.equals("2")) {
                    slideIntent.setClass(mContext, ProductDetailActivity.class);
                    slideIntent.putExtra("pId", content);
                } else if (type.equals("3")) {
                    slideIntent.setClass(mContext, ShufflingFigureActivity.class);
                    slideIntent.putExtra("url", content);
                    slideIntent.putExtra("title", popupInfo.getTitle());
                } else if (type.equals("4")) {
                    try {
                        content = URLDecoder.decode(content, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    slideIntent.setAction(Intent.ACTION_VIEW);
                    slideIntent.setData(Uri.parse(content));
                }

                mContext.startActivity(slideIntent);
                break;
            case R.id.advertising_del:
                cancel();
                break;
            default:
                break;
        }
        dismiss();
    }
}
