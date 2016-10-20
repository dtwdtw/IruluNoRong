package com.wf.irulu.module.aas.activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;

/**
 * Created by daniel on 2015/10/29.
 * 忘记密码页面
 */
public class ForgotPasswordActivity extends CommonTitleBaseActivity implements ServiceListener {

    private EditText forgot_email_et;
    private Button forgot_send_email_btn;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.forgot_password_title);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_forgot_password);
    }

    @Override
    public void initView() {
        forgot_email_et = (EditText) findViewById(R.id.forgot_password_email_et);
        forgot_send_email_btn = (Button) findViewById(R.id.forgot_password_send_btn);
        forgot_send_email_btn.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.forgot_password_send_btn) {
            String email = forgot_email_et.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                showToast(getString(R.string.empty_email_prompt));
                return;
            } else if (!StringUtils.isEmail(email)) {
                showToast(getString(R.string.email_style_wrong));
                return;
            } else {
                PageLoadDialog.showDialogForLoading(this, false, false);
                controller.getServiceManager().getAasService().forgetPassword(email, this);
            }
        }
    }

    /**
     * 联网成功
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        PageLoadDialog.hideDialogForLoading();
        switch (action) {
            case FIND_PASSWORD:
                    Toast toast = Toast.makeText(this, "Please go to your mailbox to change the password.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                break;
            default:
                break;
        }
    }

    /**
     * 联网失败
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
        }else{
            showToast((String)returnObj);
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
