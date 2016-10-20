package com.wf.irulu.module.user.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CountryInfor;
import com.wf.irulu.common.bean.CountryInforBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.AssortView;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.user.adapter.CountryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by daniel on 2015/11/11.
 */
public class CountryActivity extends CommonTitleBaseActivity implements ServiceListener {
    private ArrayList<CountryInforBean> datas = new ArrayList<CountryInforBean>();
    private ListView country_lv;
    private AssortView country_av;
    private CountryAdapter adapter;

    @Override
    protected String setWindowTitle() {
        return "Select Country";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_country);
    }

    @Override
    public void initView() {
        country_lv = (ListView) findViewById(R.id.country_lv);
        country_av = (AssortView) findViewById(R.id.country_av);
    }

    @Override
    public void initData() {
        PageLoadDialog.showDialogForLoading(this, true, false);
        controller.getServiceManager().getAasService().getCountryInformation(this);

    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 点击字母的回调   1 字母按键的会调  2 显示中间的视图
     */
    private void assortCallBack() {
        country_av.setOnTouchAssortListener(new AssortView.OnTouchAssortListener() {

            View layoutView = View.inflate(CountryActivity.this, R.layout.popupwindow_country_alert, null);
            TextView text = (TextView) layoutView.findViewById(R.id.popupwindow_country_tv);
            PopupWindow popupWindow;

            public void onTouchAssortListener(String str) {
                int index = -1;
                for (int i = 0; i < datas.size(); i++) {
                    if (str.equalsIgnoreCase(datas.get(i).getFirstLetter())) {
                        if (i != 0) {
                            if (!datas.get(i).getFirstLetter().equals(datas.get(i - 1).getFirstLetter())) {
                                index = i;
                            }
                        } else {
                            index = i;
                        }
                    }
                }
                if (index != -1) {
                    country_lv.setSelection(index);
                }
                if (popupWindow != null) {
                    text.setText(str);
                } else {
                    popupWindow = new PopupWindow(layoutView,
                            80, 80,
                            false);
                    popupWindow.showAtLocation(getWindow().getDecorView(),
                            Gravity.CENTER, 0, 0);
                }
                text.setText(str);
            }

            public void onTouchAssortUP() {
                if (popupWindow != null)
                    popupWindow.dismiss();
                popupWindow = null;
            }
        });
    }

    /**
     * 成功获取国家信息 添加数据 并按照字母排序
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        PageLoadDialog.hideDialogForLoading();
        switch (action) {
            case COUNTRY_INFORMATION:
                CountryInfor ci = (CountryInfor) returnObj;
                int total = ci.getTotal();
                if (total > 0) {
                    for (int i = 0; i < total; i++) {
                        datas.add(ci.getList().get(i));
                    }
                    Collections.sort(datas, new Comparator<CountryInforBean>() {
                        @Override
                        public int compare(CountryInforBean t1, CountryInforBean t2) {
                            return t1.getCountryName().compareToIgnoreCase(t2.getCountryName());
                        }
                    });
                        adapter = new CountryAdapter(this, datas, getIntent().getStringExtra("cname"));
                        country_lv.setAdapter(adapter);
                    assortCallBack();
                }

                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        showToast((String) returnObj);
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        PageLoadDialog.hideDialogForLoading();
        switch (action) {
            case COUNTRY_INFORMATION:
                CountryInfor ci = (CountryInfor) returnObj;
                int total = ci.getTotal();
                if (total > 0) {
                    for (int i = 0; i < total; i++) {
                        datas.add(ci.getList().get(i));
                    }
                    Collections.sort(datas, new Comparator<CountryInforBean>() {
                        @Override
                        public int compare(CountryInforBean t1, CountryInforBean t2) {
                            return t1.getCountryName().compareToIgnoreCase(t2.getCountryName());
                        }
                    });
                    adapter = new CountryAdapter(this, datas, getIntent().getStringExtra("cname"));
                    country_lv.setAdapter(adapter);
                    assortCallBack();
                }
                break;
        }
    }
}
