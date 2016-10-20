package com.wf.irulu.module.contact.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonAdapter;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.base.ViewHolder;
import com.wf.irulu.common.bean.ContactUsTag;
import com.wf.irulu.common.bean.CustomerSupportTagList;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;

import java.util.ArrayList;

public class ContactUsActivity extends CommonTitleBaseActivity implements ServiceListener {

    private Dialog dialog;
    private TextView customer_txt;
    private String reason_id;//原因id
    private EditText customer_edtxt;
    private EditText customer_email;
    private ArrayList<CustomerSupportTagList> mCustomerSupportTagList;

    @Override
    protected String setWindowTitle() {
        return "Contact Us";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public void initView() {
        PageLoadDialog.showDialogForLoading(this, true, false);
        RelativeLayout customer_rl = (RelativeLayout) findViewById(R.id.customer_rl);
        customer_txt = (TextView) findViewById(R.id.customer_txt);
        customer_edtxt = (EditText) findViewById(R.id.customer_edtxt);
        customer_email = (EditText) findViewById(R.id.customer_email);
        customer_rl.setOnClickListener(this);
    }

    @Override
    public void initData() {
        controller.getServiceManager().getAasService().getSupportReason(this);
        String email = CacheUtils.getString(this, "email");
        customer_email.setText(email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_rl:
                View inflate = View.inflate(this, R.layout.dialog_choose_layout, null);
                TextView choose_common_title = (TextView) inflate.findViewById(R.id.choose_common_title);//对话框标题控件
                ImageView cancel_choose = (ImageView) inflate.findViewById(R.id.cancel_choose);
                ListView choose_common_lv = (ListView) inflate.findViewById(R.id.choose_common_lv);
                choose_common_title.setText(customer_txt.getText().toString().trim());//设置对话框标题
//                choose_common_lv.setDividerHeight(0);
                //使用万能的ListView GridView 适配器
                if (mCustomerSupportTagList != null) {
//			        choose_common_lv.setAdapter(new ListAdapter());
                    choose_common_lv.setAdapter(new CustomerSupportAdapter(this, mCustomerSupportTagList, R.layout.item_choose_reviews_view));
//                    dialog = new AlertDialog.Builder(this,R.style.Theme_Hold_Dialog_Base).setView(inflate).create();
                    dialog = new Dialog(this, R.style.Theme_Hold_Dialog_Base);
                    dialog.setContentView(inflate);
                    dialog.show();
                }

                cancel_choose.setOnClickListener(this);
                break;
            case R.id.cancel_choose:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 点击发送按钮
     *
     * @param v
     */
    public void send(View v) {
        String customerEdtxt = customer_edtxt.getText().toString().trim();
        String customerEmail = customer_email.getText().toString().trim();
        if (reason_id == null) {
            showToast("Order problem cannot be empty!");
            return;
        }
        if (TextUtils.isEmpty(customerEdtxt)) {
            showToast("Your question cannot be empty!");
            return;
        }
        if (TextUtils.isEmpty(customerEmail)) {
            showToast("Email cannot be empty!");
            return;
        }
        if (!StringUtils.isEmail(customerEmail)) {
            showToast(R.string.email_style_wrong);
            return;
        }

        PageLoadDialog.showDialogForLoading(this, true, false);
        controller.getServiceManager().getAasService().addSupportInfo(reason_id, customerEdtxt, customerEmail, this);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case SUPPORT_REASON:
                ContactUsTag contactUsTag = (ContactUsTag) returnObj;
                mCustomerSupportTagList = contactUsTag.getList();
                PageLoadDialog.hideDialogForLoading();
                break;
            case ADD_SUPPORT_INFO:
                showToast("Send a success.");
                Intent intent = new Intent(this, ContactUsSuccessActivity.class);
                startActivity(intent);
                PageLoadDialog.hideDialogForLoading();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case SUPPORT_REASON:
                PageLoadDialog.hideDialogForLoading();
                if (null != returnObj) {
                    showToast(returnObj.toString());
                }
                break;
            case ADD_SUPPORT_INFO:
                showToast("Send failure!");
                PageLoadDialog.hideDialogForLoading();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    /**
     * 使用万能的ListView GridView 适配器
     */
    public class CustomerSupportAdapter extends CommonAdapter<CustomerSupportTagList> {
        private ArrayList<CustomerSupportTagList> datas;

        public CustomerSupportAdapter(Context context, ArrayList<CustomerSupportTagList> datas, int itemLayoutResId) {
            super(context, datas, itemLayoutResId);
            this.datas = datas;
        }

        @Override
        public void convert(ViewHolder viewHolder, final int position, final CustomerSupportTagList item) {
            //设置item_choose_reviews_view布局的RelativeLayout的宽高
//			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 70);
//			viewHolder.setLayoutParams(R.id.item_rl,lp);

            if (datas.size() - 1 == position) {
                viewHolder.setVisibility(R.id.item_horizontal_line, View.GONE);//隐藏最后一个item的线
            } else
                viewHolder.setVisibility(R.id.item_horizontal_line, View.VISIBLE);
            //设置评语
            viewHolder.setText(R.id.item_content, item.getQuestion());
            //设置选中的状态(打勾还是不打勾)
            viewHolder.setBackgroundResource(R.id.leftnemu_selected, R.mipmap.leftnemu_normal);
            //设置item(RelativeLayout)的点击事件
            viewHolder.setOnClickListener(R.id.item_rl, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getId().equals(reason_id)) {
                        reason_id = null;
                    } else {
                        reason_id = item.getId();
                    }
                    reason_id = item.getId();
                    notifyDataSetChanged();
                    dialog.dismiss();
                    customer_txt.setText(item.getQuestion());
                }
            });

            if (item.getId().equals(reason_id)) {
                viewHolder.setBackgroundResource(R.id.leftnemu_selected, R.mipmap.leftnemu_selected);
            } else {
                viewHolder.setBackgroundResource(R.id.leftnemu_selected, R.mipmap.leftnemu_normal);
            }
        }
    }
}
