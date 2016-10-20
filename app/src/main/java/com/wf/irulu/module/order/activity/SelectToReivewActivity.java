package com.wf.irulu.module.order.activity;

import android.view.View;
import android.widget.ListView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.OrderDetailProduct;
import com.wf.irulu.module.order.adapter.SelectToReviewAdapter;

import java.util.ArrayList;

/**
 * Created by Sellen on 15/11/23.
 * If more than one product need to write a review, we show the products list then we can select anyone to write a review.
 */
public class SelectToReivewActivity extends CommonTitleBaseActivity{

    ArrayList<OrderDetailProduct> products;

    ListView list;

    SelectToReviewAdapter adapter;

    @Override
    protected String setWindowTitle() {
        return this.getString(R.string.write_a_review);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select_to_review);
    }

    @Override
    public void initView() {
        list = (ListView) this.findViewById(R.id.list);
    }

    @Override
    public void initData() {
        products = getIntent().getParcelableArrayListExtra("products");

        adapter = new SelectToReviewAdapter(SelectToReivewActivity.this, products);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
