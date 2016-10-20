package com.wf.irulu.module.order.activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.RefundDetailsDataBean;
import com.wf.irulu.common.bean.RefundDetailsListBean;
import com.wf.irulu.common.bean.RefundDetailsQuestionBean;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.NoScrollListView;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.order.adapter.RefundDetailsCommentsAdapter;
import com.wf.irulu.module.order.adapter.RefundDetailsPicAdapter;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/13.
 */
public class RefundDetailsActivity extends CommonTitleBaseActivity implements ServiceListener {

    private GridView refund_gv;
    private ArrayList<String> pics = new ArrayList<String>();
    private NoScrollListView nslv;
    private TextView order_id_tv;
    private TextView order_date_tv;
    private TextView status_tv;
    private TextView reason_tv;
    private TextView view_all_tv;
    private ImageView view_all_iv;
    private  Boolean flags = true;
    private RelativeLayout view_all_rl;
    private ArrayList<RefundDetailsListBean> list;


    @Override
    protected String setWindowTitle() {
        return getString(R.string.refund_details);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_refund_details);
    }

    @Override
    public void initView() {
        order_id_tv = (TextView) findViewById(R.id.refund_details_order_id_tv);
        order_date_tv = (TextView) findViewById(R.id.refund_details_order_date_tv);
        status_tv = (TextView) findViewById(R.id.refund_details_status_tv);
        reason_tv = (TextView) findViewById(R.id.refund_details_reason_tv);
        nslv = (NoScrollListView) findViewById(R.id.refund_details_lv);
        view_all_rl = (RelativeLayout) findViewById(R.id.refund_details_view_all_rl);
        view_all_tv = (TextView) findViewById(R.id.refund_details_view_all_tv);
        view_all_iv = (ImageView) findViewById(R.id.refund_details_view_all_iv);
        EditText issus_et = (EditText) findViewById(R.id.refund_details_issue_et);
        TextView upload_num_tv = (TextView) findViewById(R.id.refund_details_pic_number_tv);
        refund_gv = (GridView) findViewById(R.id.refund_details_gv);
        Button submit_btn = (Button) findViewById(R.id.refund_details_submit_btn);


        view_all_rl.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

    }

    @Override
    public void initData() {


        refund_gv.setAdapter(new RefundDetailsPicAdapter(this, pics));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refund_details_view_all_rl:
                if(flags) {//展开
                    view_all_tv.setText("Hidden");
                    RotateAnimation ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setDuration(500);
                    ra.setFillAfter(true);
                    view_all_iv.setAnimation(ra);
                    flags=false;

//                    nslv.setAdapter(new RefundDetailsCommentsAdapter(this,list,list.size()));
                }else{//闭合
                    view_all_tv.setText("View all");
                    RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setDuration(500);
                    ra.setFillAfter(true);
                    view_all_iv.setAnimation(ra);
                    flags=true;
//                    nslv.setAdapter(new RefundDetailsCommentsAdapter(this,list,2));
                }
                break;
            case R.id.refund_details_submit_btn:



                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case SERVICE_DETAILS:
                RefundDetailsDataBean rddb = (RefundDetailsDataBean) returnObj;
                RefundDetailsQuestionBean question = rddb.getQuestion();
                order_id_tv.setText(question.getOrderId());
                order_date_tv.setText(DateFormatUtils.getMMddyyyyHHmm(question.getTime() * 1000));
                status_tv.setText(question.getStatus());//状态 0：关闭1：等待客服回复、2：客服已回复、3：拒绝售后
                reason_tv.setText(question.getReason());
                list = rddb.getList();
                if (list == null || list.size() < 3) {
                    view_all_rl.setVisibility(View.GONE);
                }
                int n;
                if(list==null){
                    n = 0;
                }else if(list!=null && list.size()<3){
                    n = list.size();
                }else{
                    n = 2;
                }
                RefundDetailsCommentsAdapter  commentsAdapter = new RefundDetailsCommentsAdapter(this, list,n);
                nslv.setAdapter(commentsAdapter);
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if (errorCode == ErrorCodeUtils.ERROR_NO_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        switch (action) {
            case SERVICE_DETAILS:
                showToast((String) returnObj);
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
