package com.wf.irulu.module.user.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.StateInfo;
import com.wf.irulu.common.view.AssortView;
import com.wf.irulu.module.user.adapter.StateAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StateActivity extends CommonTitleBaseActivity {
    private ArrayList<StateInfo.ListEntity> datas = new ArrayList<StateInfo.ListEntity>();
    private ListView mStateLv;
    private AssortView mStateAssortView;
    private StateAdapter mStateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_state);

    }

    @Override
    public void initView() {
        mStateLv = (ListView) findViewById(R.id.state_lv);
        mStateAssortView = (AssortView) findViewById(R.id.country_av);
        initListener();

    }


    private void initListener() {
        mStateAssortView.setOnTouchAssortListener(new AssortView.OnTouchAssortListener() {

            View layoutView = View.inflate(StateActivity.this, R.layout.popupwindow_country_alert, null);
            TextView text = (TextView) layoutView.findViewById(R.id.popupwindow_country_tv);
            PopupWindow popupWindow;

            public void onTouchAssortListener(String str) {
                int index = -1;
                for (int i = 0; i < datas.size(); i++) {
                    if (str.equalsIgnoreCase(datas.get(i).getName().substring(0, 1))) {
                        if (i != 0) {
                            if (!datas.get(i).getName().substring(0, 1).equals(datas.get(i - 1).getName().substring(0, 1))) {
                                index = i;
                            }
                        } else {
                            index = i;
                        }
                    }
                }
                if (index != -1) {
                    mStateLv.setSelection(index);
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

    @Override
    public void initData() {
        ArrayList<StateInfo.ListEntity> temp = getIntent().getParcelableArrayListExtra("stateList");
        datas.clear();
        Collections.sort(temp, new Comparator<StateInfo.ListEntity>() {
            @Override
            public int compare(StateInfo.ListEntity t1, StateInfo.ListEntity t2) {
                return t1.getName().compareToIgnoreCase(t2.getName());
            }
        });
        datas.addAll(temp);
        mStateAdapter = new StateAdapter(this, temp);
        mStateLv.setAdapter(mStateAdapter);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected String setWindowTitle() {
        return "Select State";
    }


}
