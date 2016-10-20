package com.wf.irulu.module.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.TrackingInfor;
import com.wf.irulu.common.bean.TrackingInforBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.order.adapter.TrackingAdapter;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/12. 物流详情页面
 */
public class TrackingDetailsActivity extends CommonTitleBaseActivity implements ServiceListener {

    private TextView express_tv;
    private TextView tracking_id;
    private ListView tracking_lv;
    private TrackingInforBean tib;

    @Override
    protected String setWindowTitle() {
        return "Tracking Details";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.loading_simple_waiting);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String flag = getIntent().getStringExtra("flag");
        controller.getServiceManager().getOrderService().getLogisticsInfor(flag, this);
    }

    @Override
    public void initDataView() {
        ImageView tracking_details_pic_iv = (ImageView) findViewById(R.id.tracking_details_product_pic_iv);
        express_tv = (TextView) findViewById(R.id.tracking_details_express_name_tv);
        tracking_id = (TextView) findViewById(R.id.tracking_details_id_tv);
        tracking_lv = (ListView) findViewById(R.id.tracking_details_lv);
    }

    @Override
    public void addData() {
        if(tib!=null) {
            express_tv.setText(tib.getLogisticsCompany());
            tracking_id.setText(tib.getLogisticsId());
            ArrayList<TrackingInfor> items = tib.getItems();
            tracking_lv.setAdapter(new TrackingAdapter(items, this));
        }
    }

    @Override
    public void onClick(View view) {

    }

    public static void startTrackingDetailsActivity(Activity activity, String orderid) {
        Intent i = new Intent(activity, TrackingDetailsActivity.class);
        i.putExtra("flag", orderid);
        activity.startActivity(i);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
            if(action==ActionTypes.LOGISTICS_TRACKING){
                tib = (TrackingInforBean) returnObj;
                refreshDataView(R.layout.activity_tracking_details);
            }

    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        displayNoDataView(R.layout.no_data_simple_title_page);
            if(errorCode== ErrorCodeUtils.NO_NET_RESPONSE){
                showToast(R.string.no_network);
                return;
            }
        showToast((String)returnObj);
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
}
