package com.wf.irulu.module.category.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CategoryDataBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.category.adapter.SmartDeviceAdapter;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategorySmartDeviceActivity extends CommonTitleBaseActivity implements ServiceListener {
    private Intent intent;
    private ListView sd_lv;
    private CategoryDataBean cdb;

    @Override
    protected String setWindowTitle() {
        intent = getIntent();
        String name = intent.getStringExtra("name");
        return name;
    }

    @Override
    public void setContentView() {

        setContentView(R.layout.loading_multiple_waiting);
//        setContentView(R.layout.activity_category_smart_device);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        controller.getServiceManager().getProductService().getCategoryList(intent.getIntExtra("id", -1), 1, 20, null, 0, 4, this);
    }

    @Override
    public void initDataView() {
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
        sd_lv = (ListView) bindView(R.id.category_smart_device_lv);
    }

    @Override
    public void addData() {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {

        if (action == ActionTypes.PRODUCT_CATEGORY_LIST) {
            if (returnObj != null) {
                cdb = (CategoryDataBean) returnObj;
                refreshDataView(R.layout.activity_category_smart_device);
                sd_lv.setAdapter(new SmartDeviceAdapter(this, cdb));
            }
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        displayNoDataView(R.layout.no_data_multiple_title_page);
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        ArrayList<String> msgs = (ArrayList<String>) returnObj;
        if (msgs != null && msgs.size() > 2) {

            showToast(msgs.get(2));
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
    }
}
