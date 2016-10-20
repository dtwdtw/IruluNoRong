package com.wf.irulu.module.category.activity;

import android.view.View;
import android.widget.ImageView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.ProductCategoryBean;
import com.wf.irulu.common.bean.ProductCategoryInformationBean;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.IpListView;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.category.adapter.CategroyHomeAdapter;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/17.
 */
public class CategoryActivity extends CommonTitleBaseActivity implements ServiceListener {

    private IpListView category_lv;
    private ProductCategoryBean pcb;

    @Override
    protected String setWindowTitle() {
        return "Category";
    }

    @Override
    public void setContentView() {

        setContentView(R.layout.loading_multiple_waiting);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        controller.getServiceManager().getProductService().getCategoryHome(this);
    }

    @Override
    public void initDataView() {
        lines.setVisibility(View.GONE);
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
        category_lv = (IpListView) bindView(R.id.category_home_lv);
        ImageView iv = new ImageView(this);
        iv.setMinimumHeight(ConstantsUtils.DISPLAYH - ((ConstantsUtils.DISPLAYW - UIUtils.dip2px(20)) / 3 * 4) - UIUtils.dip2px(100));
        category_lv.addFooterView(iv);
    }

    @Override
    public void addData() {
        ArrayList<ProductCategoryInformationBean> list = pcb.getList();
        category_lv.setAdapter(new CategroyHomeAdapter(this, list));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        if (action == ActionTypes.PRODUCT_CATEGORY) {
            pcb = (ProductCategoryBean) returnObj;
            refreshDataView(R.layout.activity_category);

        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        displayNoDataView(R.layout.no_data_multiple_title_page);
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        showToast((String) returnObj);
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        if (action == ActionTypes.PRODUCT_CATEGORY) {
          pcb = (ProductCategoryBean) returnObj;
            refreshDataView(R.layout.activity_category);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
    }
}
