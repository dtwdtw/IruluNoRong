package com.wf.irulu.module.setting.activity;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;

/**
 * Created by daniel on 2015/11/2.
 * feedback页面
 */
public class FeedBackActivity extends CommonTitleBaseActivity implements ServiceListener {

    private EditText feedback_et;
    private AlertDialog dialog;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.your_feedback);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void initView() {
        feedback_et = (EditText) findViewById(R.id.feedback_et);
        Button feedback_btn = (Button) findViewById(R.id.feedback_btn);
        feedback_btn.setOnClickListener(this);
        commonTitleBack.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    /**
     * send 的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.feedback_btn) {
            String content = feedback_et.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                showToast(getString(R.string.empty_feedback));
                return;
            }
            PageLoadDialog.showDialogForLoading(this,true,false);
            controller.getServiceManager().getAasService().sendFeedback(content, this);
        } else if (view.getId() == R.id.commontitle_iv_back) {
            String feedback = feedback_et.getText().toString().trim();
            if (TextUtils.isEmpty(feedback)) {
                finish();
            } else {
                setEditDialog();
            }
        }
    }

    /**
     * send成功
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        PageLoadDialog.hideDialogForLoading();
        if (action == ActionTypes.FEEDBACK) {
            finish();
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        if (action == ActionTypes.FEEDBACK) {
                showToast((String) returnObj);
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    /**
     * 返回键的处理逻辑
     */
    @Override
    public void onBackPressed() {
        String feedback = feedback_et.getText().toString().trim();
        if (TextUtils.isEmpty(feedback)) {
            finish();
        } else {
            setEditDialog();
        }
    }

    /**
     * 内容不为空是显示的dialog
     */
    private void setEditDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_feedback, null);

        Button no = (Button) view.findViewById(R.id.feed_dialog_no_btn);
        Button yes = (Button) view.findViewById(R.id.feed_dialog_yes_btn);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ab.setView(view);
        dialog = ab.create();
        dialog.show();
    }
}
