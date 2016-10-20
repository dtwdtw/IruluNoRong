package com.wf.irulu.module.newArrivals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.HomeProduct;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.xrecyclerview.XRecyclerView;
import com.wf.irulu.logic.listener.IsAddWishListListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.service.ArrivalsDecoration;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/18.
 */
public class NewArrivalsActivity extends CommonTitleBaseActivity implements ServiceListener, IsAddWishListListener, XRecyclerView.LoadingListener {
    private static final int REFRESH = 2;
    private static final int LOAD = 3;
    private static final int NORMAL = 1;
    private int currentAction = NORMAL;

    private XRecyclerView mXRecyclerView;
    private NewArrivalsAdapter mNewArrivalsAdapter;
    private ArrayList<ProductInfo> mProductInfos = new ArrayList<ProductInfo>();
    private boolean isLoadData = false;
    private int mPage = 1;
    private int mId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getIntExtra("id", 3);
        }
    }

    @Override
    protected String setWindowTitle() {

        return getString(R.string.new_arrivals);


    }


    @Override
    public void setContentView() {

        setContentView(R.layout.loading_multiple_waiting);

    }

    @Override
    protected void setView() {
        super.setView();
        mXRecyclerView = (XRecyclerView) findViewById(R.id.xv_new_arrivals);

        if (mXRecyclerView != null) {

            mXRecyclerView.setLoadingListener(this);

            GridLayoutManager vGridLayoutManager = new GridLayoutManager(this, 2);

            mXRecyclerView.setLayoutManager(vGridLayoutManager);
            int left = UIUtils.dip2px(8);
            int right = UIUtils.dip2px(4);
            mXRecyclerView.addItemDecoration(new ArrivalsDecoration(left, right, left));
            mProductInfos.clear();
            mNewArrivalsAdapter = new NewArrivalsAdapter(this, mProductInfos);
            mXRecyclerView.setAdapter(mNewArrivalsAdapter);
        }

    }

    @Override
    public void initView() {


    }


    @Override
    public void initData() {
        controller.registIsAddWishListListener(this);
        controller.getServiceManager().getProductService().getNewArrivals(mPage + "", mId + "", this);

    }

    @Override
    public void initDataView() {


    }


    @Override
    public void addData() {
    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {

        switch (action) {
            case NEW_ARRICALS:
                isLoadData = false;
                if (currentAction == NORMAL) {
                    refreshDataView(R.layout.activity_new_arrivals);
                }
                HomeProduct vHomeProduct = (HomeProduct) returnObj;
                boolean vIsEmpty = vHomeProduct.getProductList() == null || vHomeProduct.getProductList().size() == 0;
                boolean isRepeat = false;
                if (currentAction == REFRESH) {

                    mXRecyclerView.refreshComplete();
                } else if (currentAction == LOAD) {
                    if (vIsEmpty) {
                        mXRecyclerView.noMoreLoading();
                    } else {
                        mXRecyclerView.loadMoreComplete();
                    }
                }

                if (!vIsEmpty) {
                    if (currentAction == REFRESH) {
                        mPage = 1;
                        mProductInfos.clear();
                    } else if (currentAction == LOAD) {
                        mPage++;
                        if (mProductInfos.containsAll(vHomeProduct.getProductList())) {
                            isRepeat = true;
                        }
                    }
                    if (!isRepeat) {
                        mProductInfos.addAll(vHomeProduct.getProductList());
                    }

                    mNewArrivalsAdapter.notifyDataSetChanged();
                }

        }


    }


    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        isLoadData = false;

        switch (action) {
            case NEW_ARRICALS:
                if (currentAction == REFRESH) {
                    mXRecyclerView.refreshComplete();
                } else if (currentAction == LOAD) {
                    mXRecyclerView.loadMoreComplete();
                } else {
                    displayNoDataView(R.layout.no_data_multiple_title_page);
                }
                break;
            default:
                break;
        }
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
    public void isAddWishList(String productId, String addWishList) {
        if (mProductInfos == null || mProductInfos.size() == 0) {
            return;
        }

        ProductInfo vProductInfo;
        boolean isChange = false;
        for (int i = 0; i < mProductInfos.size(); i++) {
            vProductInfo = mProductInfos.get(i);
            if (vProductInfo != null && vProductInfo.getProductId().equals(productId)) {
                vProductInfo.setAddWishList(addWishList);
                isChange = true;
                break;
            }
        }

        if (isChange) {
            mNewArrivalsAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onRefresh() {
        if (isLoadData) {
            return;
        }
        currentAction = REFRESH;
        isLoadData = true;
        controller.getServiceManager().getProductService().getNewArrivals(1 + "", mId + "", this);
    }

    @Override
    public void onLoadMore() {
        //与返回一一对应使之不乱序
        if (isLoadData) {
            return;
        }
        isLoadData = true;
        currentAction = LOAD;
        controller.getServiceManager().getProductService().getNewArrivals(mPage + "", mId + "", this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.unRegistIsAddWishListListener(this);
    }
}
