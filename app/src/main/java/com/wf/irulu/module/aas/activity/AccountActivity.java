package com.wf.irulu.module.aas.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.MD5Util;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.listener.ServiceListener;

/**
 * Created by daniel on 2015/10/29.
 * 注册页面
 */
public class AccountActivity extends CommonTitleBaseActivity implements ServiceListener {

    private EditText firstname_et;
    private EditText lastname_et;
    private EditText email_et;
    private EditText password_et;
    private EditText repassword_et;
    private Button account_btn;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.creat_account);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_account);
    }

    @Override
    public void initView() {
        firstname_et = (EditText) findViewById(R.id.account_firstname_et);
        lastname_et = (EditText) findViewById(R.id.account_lastname_et);
        email_et = (EditText) findViewById(R.id.account_email_et);
        password_et = (EditText) findViewById(R.id.account_password_et);
        repassword_et = (EditText) findViewById(R.id.account_repassword_et);
        account_btn = (Button) findViewById(R.id.account_btn);
        account_btn.setOnClickListener(this);
        commonTitleBack.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_btn:
                String firstname = firstname_et.getText().toString().trim();
                String lastname = lastname_et.getText().toString().trim();
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();
                String repassword = repassword_et.getText().toString().trim();

                if (TextUtils.isEmpty(firstname)) {
                    showToast(getString(R.string.empty_firstname));
                    return;
                } else if (TextUtils.isEmpty(lastname)) {
                    showToast(getString(R.string.empty_lastname));
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    showToast(getString(R.string.empty_email_prompt));
                    return;
                } else if (!StringUtils.isEmail(email)) {
                    showToast(getString(R.string.email_style_wrong));
                    return;
                } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
                    showToast(getString(R.string.empty_password));
                    return;
                } else if (password.length() < 6) {
                    showToast(getString(R.string.password_mimimum_length));
                    return;
                } else if (!password.equals(repassword)) {
                    showToast(getString(R.string.password_not_same));
                    return;
                } else {
                    PageLoadDialog.showDialogForLoading(this, true, false);
                    String _encode_password = MD5Util.md5(password + ":alllandnet");
                    controller.getServiceManager().getAasService().register(firstname, lastname, email, _encode_password, "1", this);
                }
                break;
            case R.id.commontitle_iv_back:
                if ("RefrenceActivity".equals(getIntent().getStringExtra("flag"))) {
                    Intent i = new Intent(this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    finish();
                    overridePendingTransition(0, R.anim.activity_close);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        if (action == ActionTypes.REGISTER) {
            PageLoadDialog.hideDialogForLoading();
                Intent mactivity = new Intent(this, HomeActivity.class);
                startActivity(mactivity);
                LoginUser user = (LoginUser) returnObj;
                String rongCloudToken = user.getRongCloudToken();//融云token
                CacheUtils.setLong(this, "uid", user.getUid());
                // email
                CacheUtils.setString(this, "email", user.getEmail());
                // lastname
                CacheUtils.setString(this, "lastname", user.getLastname());
                // firstname
                CacheUtils.setString(this, "firstname", user.getFirstname());
                // nickname
                CacheUtils.setString(this, "nickname", user.getNickname());
                // registerDate
                CacheUtils.setLong(this, "registerDate", user.getRegisterDate());
                // froms
                CacheUtils.setLong(this, "froms", user.getFroms());
                // headjpg
                CacheUtils.setString(this, "headjpg", user.getHeadjpg());
                // sex
                CacheUtils.setString(this, "sex", user.getSex());
                CacheUtils.setString(this, "token", user.getToken());
                // 保存登陆用户信息
                CacheUtils.setLong(this, "user_type", 1);
                // rongCloudToke
                CacheUtils.setString(this, "rong_cloud_token", rongCloudToken);
                // birthday
                CacheUtils.setString(this, "birthday", user.getBirthday());
                CacheUtils.setString(this, "returnemail", user.getEmail());
                showToast(R.string.regist_succed);
                finish();
                overridePendingTransition(0, R.anim.activity_close);
            }

    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(getString(R.string.no_network));
        }else{
            showToast((String)returnObj);
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    public void onBackPressed() {
        if ("RefrenceActivity".equals(getIntent().getStringExtra("flag"))) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        } else {
            finish();
            overridePendingTransition(0, R.anim.activity_close);
        }
    }
}
