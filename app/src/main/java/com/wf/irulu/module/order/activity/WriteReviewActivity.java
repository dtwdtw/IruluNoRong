package com.wf.irulu.module.order.activity;

import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.OrderDetailProduct;

/**
 * Created by Sellen on 15/11/25.
 */
public class WriteReviewActivity extends CommonTitleBaseActivity{

    OrderDetailProduct productInfo;

    @Override
    protected String setWindowTitle() {
        return this.getString(R.string.write_a_review);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_write_review);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        productInfo = (OrderDetailProduct) getIntent().getParcelableExtra("product");
    }

    @Override
    public void onClick(View v) {

    }
}
