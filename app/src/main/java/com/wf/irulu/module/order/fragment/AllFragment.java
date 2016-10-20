package com.wf.irulu.module.order.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseFragment;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.bean.OrderInfo;
import com.wf.irulu.common.view.xrecyclerview.XRecyclerView;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.order.adapter.OrdersListAdapter;
import com.wf.irulu.module.order.dialog.LoadingDialog;
import com.wf.irulu.module.order.listener.IOrderslListener;

import java.util.ArrayList;

public class AllFragment extends BaseFragment implements IOrderslListener, ServiceListener, XRecyclerView.LoadingListener, View.OnClickListener {

    /**
     * 下拉刷新列表
     */
    private XRecyclerView mAllPtrlvOrders = null;
    /**
     * 当前所有订单
     */
    private ArrayList<OrderDetail> orders;
    /**
     * 订单适配器
     */
    private OrdersListAdapter adapter = null;
    /**
     * 联网获取总订单订单信息
     */
    private OrderInfo info = null;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 分页订单数
     */
    private int num = 5;
    /**
     * 所有订单对应的订单类型
     */
    private int type = 1;
    /**
     * 所有订单类的总数
     */
    private int total;
    /**
     * 控制刷新界面器（重新初始化界面需要）
     */
    private boolean isNeedRefresh = false;
    /**
     * 最后一次刷新时间
     */
    private long last_time = 0;
    /**
     * 进度加载框
     */
    private LoadingDialog mLoadingDialog;
    private View mEmptyView;

    Button go_shopping;

    boolean isInit = true;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    // 设置停止刷新
                    mAllPtrlvOrders.refreshComplete();
                    break;
            }
        }
    };

    public AllFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initView();
        return mMainView;
    }

    protected void initView() {
        // 如果是第一次加载（即Fragment没有destory过）
        if (isInit) {
            controller.registerIOrderList(this);
            setContentView(R.layout.loading_no_title_waitting);
            ((AnimationDrawable) mMainView.findViewById(R.id.loading_iv_anim).getBackground()).start();

            // 获取所有订单
            controller.getServiceManager().getShoppingService().getOrders(num, page, type, this);
            mLoadingDialog = new LoadingDialog(getActivity());
            isInit = false;
        } else {
            // 如果不是第一次加载（即Fragment被destory过）
            if (isNeedRefresh) {
                // 需要刷新
                viewOrderList(info.getAll().getOrderList(), page);
//                mAllPtrlvOrders.setEmptyView(mEmptyView);
                // 设置刷新标记为已刷新
                isNeedRefresh = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_shopping:
                Intent intent = new Intent(mActivity, HomeActivity.class);
                startActivity(intent);
                mActivity.finish();
                break;
        }
    }

    /**
     * 展示列表
     *
     * @param list  联网获取的订单
     * @param _page 联网获取的当前页
     */
    private void viewOrderList(ArrayList<OrderDetail> list, int _page) {
        if (total < 1) {
            setContentView(R.layout.view_order_empty);
            go_shopping = (Button) mMainView.findViewById(R.id.go_shopping);
            go_shopping.setOnClickListener(this);
            return;
        } else {
            if (go_shopping != null || mAllPtrlvOrders == null) {
                setContentView(R.layout.fragment_all);
                mAllPtrlvOrders = (XRecyclerView) mMainView.findViewById(R.id.all_ptrlv_orders);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mAllPtrlvOrders.setLayoutManager(linearLayoutManager);
                // 设置刷新模式监听
                mAllPtrlvOrders.setLoadingListener(this);
            }
            if (null == list) {
                if (_page > 1) {
                    page--;
                }
                // 停止刷新
                mAllPtrlvOrders.loadMoreComplete();
                return;
            }
            // 如果是刷新模式或者是第一次加载
            if (_page == 1) {
                // 当前获取的数据为需要展示的全部数据
                this.orders = list;
                // 重设数据
                orders = list;
                // 初始化数据适配器，设置回掉监听
                adapter = new OrdersListAdapter(orders, getActivity(), this);
                // 设置适配器
                mAllPtrlvOrders.setAdapter(adapter);
                if (adapter != null && go_shopping == null) {
                    // 刷新界面的情况下停止刷新
                    mAllPtrlvOrders.refreshComplete();
                }
            } else {
                // 如果数据有效
                if (list.size() > 0) {
                    // 如果数据重复
                    if (orders.containsAll(list)) {
                        return;
                    }
                    // 添加集合
                    orders.addAll(list);
                    // 刷新界面
                    adapter.setOrders(orders);
                    // 刷新界面
                    mAllPtrlvOrders.loadMoreComplete();
                } else {
                    // 如果获取的数据为空，重新加载
                    if (page > 1) {
                        page--;
                    }
                }
            }
            // 设置刷新模式
            if (list.size() < num) {
                // 只允许上拉刷新
                mAllPtrlvOrders.setLoadingMoreEnabled(false);

                mAllPtrlvOrders.noMoreLoading();
                mAllPtrlvOrders.removeFootView();
            } else {
                // 允许双向刷新加载
                mAllPtrlvOrders.setLoadingMoreEnabled(true);
            }
            go_shopping = null;
        }

        if (getActivity() != null && !getActivity().isFinishing() && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        // 移除当前的fragment
        if (getView() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            if (parent != null) {
                parent.removeView(getView());
            }
        }
        super.onDestroy();
    }

    @Override
    public void serviceSuccess(ServiceListener.ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case ORDER_LIST:
                int p = (Integer) bandObj;
                // 获取订单描述
                info = (OrderInfo) returnObj;
                // 获取总数
                total = info.getAll().getCount();
                if (getActivity() != null && !getActivity().isFinishing()) {
                    // 展示列表
                    viewOrderList(info.getAll().getOrderList(), p);
                } else {
                    // 设置游离模式下的刷新监控器(getActivity()获取为空)
                    isNeedRefresh = true;
                }
                break;
            case ORDER_DELETE:
                controller.postOrderListCallback();
                break;
            case ORDER_CANCEL:
                controller.postOrderListCallback();
                break;
            case ORDER_CONFIRM:
                controller.postOrderListCallback();
                break;
            default:
                break;
        }

    }

    @Override
    public void serviceFailure(ServiceListener.ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case ORDER_LIST:
                if (page > 1) {
                    page--;
                }
                break;
            case ORDER_DELETE:
                break;
            case ORDER_CANCEL:
                break;
            case ORDER_CONFIRM:
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ServiceListener.ActionTypes action, int type, Object returnObj) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            boolean isNeedRefreshing = (Boolean) returnObj;
            switch (action) {
                case ORDER_LIST:
                    if (mEmptyView == null) {
                        break;
                    }
//                    if (isNeedRefreshing && !mAllPtrlvOrders.isRefreshing()) {
//                        mLoadingDialog.show();
//                    } else {
//                        mLoadingDialog.dismiss();
//                    }
                    break;
                case ORDER_CANCEL:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    } else {
                        mLoadingDialog.dismiss();
                    }
                    break;
                case ORDER_DELETE:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    } else {
                        mLoadingDialog.dismiss();
                    }
                    break;
                case ORDER_CONFIRM:
                    if (isNeedRefreshing) {
                        mLoadingDialog.show();
                    } else {
                        mLoadingDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void refresh() {
        page = 1;
        controller.getServiceManager().getShoppingService().getOrders(num, page, type, this);
    }

    @Override
    public void onRefresh() {
        if (last_time != 0 && System.currentTimeMillis() - last_time < 1000 * 10) {
            handler.sendEmptyMessage(0);
            return;
        }
        last_time = System.currentTimeMillis();
        page = 1;
        controller.getServiceManager().getShoppingService().getOrders(num, page, type, this);
    }

    @Override
    public void onLoadMore() {
        page++;
        controller.getServiceManager().getShoppingService().getOrders(num, page, type, this);
    }

    //    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        if (last_time != 0 && System.currentTimeMillis() - last_time < 1000 * 10) {
//            handler.sendEmptyMessage(0);
//            return;
//        }
//        last_time = System.currentTimeMillis();
//        page = 1;
//        controller.getServiceManager().getShoppingService().getOrders(num, page, type, this);
//    }
//
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        page++;
//        controller.getServiceManager().getShoppingService().getOrders(num, page, type, this);
//    }
}
