package com.wf.irulu.module.setting.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.MD5Util;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;

/**
 * Created by daniel on 2015/11/2.
 * 更改密码页面
 */
public class ChangePasswordActivity extends CommonTitleBaseActivity implements ServiceListener {

    private EditText current_pwd_et;
    private EditText new_pwd_et;
    private EditText enter_again_et;
    private TextView email_tv;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.change_password);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_change_password);
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        email_tv = (TextView) findViewById(R.id.change_password_email_tv);
        current_pwd_et = (EditText) findViewById(R.id.change_password_current_password);
        new_pwd_et = (EditText) findViewById(R.id.change_password_new_password);
        enter_again_et = (EditText) findViewById(R.id.change_password_enter_again);
        Button submit = (Button) findViewById(R.id.change_password_submit);
        submit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        String email = CacheUtils.getString(this, "email");
        email_tv.setText("Login Email:" + email);
    }

    /**
     * 提交的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_password_submit) {
            String oldpwd = current_pwd_et.getText().toString().trim();
            String newpwd = new_pwd_et.getText().toString().trim();
            String repassword = enter_again_et.getText().toString().trim();
            if (TextUtils.isEmpty(oldpwd)) {
                showToast(R.string.enter_current_password);
            } else if (oldpwd.length() < 6) {
                showToast(R.string.password_mimimum_length);
            } else if (TextUtils.isEmpty(newpwd)) {
                showToast(R.string.enter_new_password);
            } else if (newpwd.length() < 6) {
                showToast(R.string.password_mimimum_length);
            } else if (!newpwd.equals(repassword)) {
                showToast(R.string.password_not_same);
            } else {
                PageLoadDialog.showDialogForLoading(this, true, false);
                controller.getServiceManager().getAasService().changePassword(MD5Util.md5(oldpwd + ":alllandnet"), MD5Util.md5(newpwd + ":alllandnet"), this);
            }

        }
    }

    /**
     * 提交成功后 (访问网络)的操作
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        PageLoadDialog.hideDialogForLoading();
        if (action == ActionTypes.CHANGE_PASSWORD) {
            showToast(R.string.reset_password_succed);
            finish();
        }
    }

    /**
     * 提交失败(访问网络失败)的操作
     *
     * @param action    当前操作
     * @param returnObj 返回对象
     * @param errorCode
     */
    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        if (action == ActionTypes.CHANGE_PASSWORD) {
            showToast((String) returnObj);
        }

    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
