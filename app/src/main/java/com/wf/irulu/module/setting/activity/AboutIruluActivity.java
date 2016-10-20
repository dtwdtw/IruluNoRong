package com.wf.irulu.module.setting.activity;

import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;

/**
 * Created by daniel on 2015/11/2.
 * irulu 说明页面
 */
public class AboutIruluActivity extends CommonTitleBaseActivity {
    @Override
    protected String setWindowTitle() {
        return getString(R.string.about_irulu);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_about_irulu);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
