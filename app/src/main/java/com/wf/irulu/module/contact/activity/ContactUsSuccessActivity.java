package com.wf.irulu.module.contact.activity;

import android.content.Intent;
import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.framework.HomeActivity;

public class ContactUsSuccessActivity extends CommonTitleBaseActivity {

    @Override
    protected String setWindowTitle() {
        return "Contact Us";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_contact_us_success);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    public void returnHome(View v){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
