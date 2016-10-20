package com.wf.irulu.module.product.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseFragmentActivity;
import com.wf.irulu.common.bean.DailyDealsInit;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.HorizontalViewPager;
import com.wf.irulu.common.view.astuetz.PagerSlidingTabStrip;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.adapter.DailyDealsPagerAdapter;

import java.util.zip.Inflater;


/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/4/12 上午10:49
 */
public class DailyDealsActivity extends CommonTitleBaseFragmentActivity implements ServiceListener {

    public static final String TAG = "DailyDealsActivity";
    private PagerSlidingTabStrip mStrip;
    private ViewPager horizontalViewPager;
    private View dataOKView;

    @Override
    protected String setWindowTitle() {
        return "Daily Deals";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.loading_multiple_waiting);
    }

    @Override
    public void initView() {
        dataOKView = LayoutInflater.from(this).inflate(R.layout.activity_daily_deals, null, false);
        mStrip = (PagerSlidingTabStrip) dataOKView.findViewById(R.id.daily_deals_tabs);
        dataOKView.findViewById(R.id.common_multiple_title_line).setVisibility(View.GONE);
        int normalColor = UIUtils.getColor(R.color.tab_text_normal);
        int selectedColor = UIUtils.getColor(R.color.tab_text_selected);
        mStrip.setTextColor(normalColor, selectedColor);
//        mStrip.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);//设置字体相关的属性
        horizontalViewPager = (ViewPager) findViewById(R.id.daily_deals_pager);
        mStrip.setTypeface(null, Typeface.NORMAL);//设置字体相关的属性
        horizontalViewPager = (ViewPager) dataOKView.findViewById(R.id.daily_deals_pager);
    }

    @Override
    public void initData() {
        controller.getServiceManager().getProductService().getDailDealsInit(1, 10, this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case DAILY_DEALS_INIT:
                dailyDealsInit(returnObj);
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case DAILY_DEALS_INIT:
                dailyDealsInitErr(returnObj, errorCode);
                break;


        }
        displayNoDataView(R.layout.no_data_multiple_title_page);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DailyDealsActivity.this,DailyDealsActivity.class));
                finish();
            }
        };
    }

    /**
     * 初始每日优惠数据失败
     * @param returnObj
     * @param errorCode
     */
    private void dailyDealsInitErr(Object returnObj, int errorCode) {
        switch (errorCode){
            case ErrorCodeUtils.NO_NET_RESPONSE:
                if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                    showToast(getString(R.string.server_network_failed));
                } else {
                    showToast(returnObj.toString());
                }

                break;
            case ErrorCodeUtils.ERROR_PARSER:
                showToast("数据解析错误!");
                break;
        }

    }

    public void displayNoDataView(int viewId) {
        setContentView(viewId);
        setView();
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    /**
     * 日购信息初始化成功
     *
     * @param returnObj
     */
    private void dailyDealsInit(Object returnObj) {

        if (returnObj == null) {
            ILog.d(TAG, "dailyDealsInit:returnObj==null");
            return;
        }

        refreshDataView(dataOKView);
        setView();

        Log.d(TAG, "dailyDealsInit() returned: " + returnObj.toString());
        DailyDealsInit dailyDealsInit = (DailyDealsInit) returnObj;
        ILog.d(TAG, dailyDealsInit.toString());

        horizontalViewPager.setAdapter(new DailyDealsPagerAdapter(getSupportFragmentManager(), dailyDealsInit));
        mStrip.setViewPager(horizontalViewPager);
    }
}
