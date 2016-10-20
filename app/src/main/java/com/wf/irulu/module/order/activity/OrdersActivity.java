package com.wf.irulu.module.order.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseFragmentActivity;
import com.wf.irulu.common.bean.OrderInfo;
import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.view.NoScrollViewPager;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.order.adapter.OrdersPageAdapter;
import com.wf.irulu.module.order.fragment.AllFragment;
import com.wf.irulu.module.order.fragment.ShippedFragment;
import com.wf.irulu.module.order.fragment.UnpaidFragment;
import com.wf.irulu.module.order.listener.IOrderslListener;

import java.util.ArrayList;

public class OrdersActivity extends CommonTitleBaseFragmentActivity implements ServiceListener, RadioGroup.OnCheckedChangeListener,IOrderslListener {

    /** 不可滑动的viewpager*/
    private NoScrollViewPager mOrdersNsvpPage;
    /** fragment列表适配器*/
    private OrdersPageAdapter mPageAdapter;
    /** fragment列表*/
    private ArrayList<Fragment> mFragments;
    /** fragment管理器*/
    private FragmentManager mFragmentManager;
    /** 选择器*/
    private RadioGroup mOrdersRgTabs;
    private final int NUM = 1;
    private final int PAGE = 1;
    private final int TYPE = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_orders);
    }

    @Override
    public void initView() {
        controller.registerIOrderList(this);
        mOrdersNsvpPage = bindView(R.id.orders_nsvp_page);
        mOrdersRgTabs = bindView(R.id.orders_rg_tabs);
    }

    @Override
    public void initData() {
        // 设置fragment和title的适配器
        getOrders();
        mPageAdapter = new OrdersPageAdapter(mFragmentManager,mFragments);
        mOrdersNsvpPage.setAdapter(mPageAdapter);
        mOrdersRgTabs.setOnCheckedChangeListener(this);
        mOrdersRgTabs.check(R.id.orders_rb_all);
        controller.getServiceManager().getShoppingService().getOrders(NUM,PAGE,TYPE,this);
    }

    /**
     * 初始化数据或者重设数据（依据fragment是否赋值，若不为空则证明已经初始化数据，不为空则证明刷新数据），请求联网获得全部数据
     */
    private void getOrders() {
        mFragments = new ArrayList<Fragment>();
        AllFragment allFragment = new AllFragment();
        UnpaidFragment unpaidFragment = new UnpaidFragment();
        ShippedFragment shippedFragment = new ShippedFragment();
        mFragments.add(allFragment);
        mFragments.add(unpaidFragment);
        mFragments.add(shippedFragment);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected String setWindowTitle() {
        return getString(R.string.my_orders);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.orders_rb_all:
                mOrdersNsvpPage.setCurrentItem(0);
                break;
            case R.id.orders_rb_unpaid:
                CustomerAnalystic.analystic(CustomerEvent.num_of_my_order_unpaid_orders, mLoger);
                mOrdersNsvpPage.setCurrentItem(1);
                break;
            case R.id.orders_rb_shipped:
                CustomerAnalystic.analystic(CustomerEvent.num_of_my_order_open_orders, mLoger);
                mOrdersNsvpPage.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action){
            case ORDER_LIST:
                OrderInfo info = (OrderInfo) returnObj;
                int total = info.getAll().getCount();
                if (total <= 99){
                    ((RadioButton)mOrdersRgTabs.getChildAt(0)).setText(getString(R.string.all_orders) + "("  + info.getAll().getCount() + ")");
                }else{
                    ((RadioButton)mOrdersRgTabs.getChildAt(0)).setText(getString(R.string.all_orders) + "(99+)");
                }
                total = info.getNotpay().getCount();
                if (total <= 99){
                    ((RadioButton)mOrdersRgTabs.getChildAt(1)).setText(getString(R.string.unpaid) + "(" + info.getNotpay().getCount() + ")");
                }else{
                    ((RadioButton)mOrdersRgTabs.getChildAt(1)).setText(getString(R.string.unpaid) + "(99+)");
                }
                total = info.getNotconfirm().getCount();
                if (total <= 99){
                    ((RadioButton)mOrdersRgTabs.getChildAt(2)).setText(getString(R.string.shipped) + "("  + info.getNotconfirm().getCount() + ")");
                }else{
                    ((RadioButton)mOrdersRgTabs.getChildAt(2)).setText(getString(R.string.shipped) + "(99+)");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {

    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    public void refresh() {
        controller.getServiceManager().getShoppingService().getOrders(NUM,PAGE,TYPE,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.unRegisterIOrderList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获按下返回键事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "OrdersActivity");
                ExitionUtil.getInstance().setEnable();
            } /** else {
             仍然将之前的页面设置为Exit Page
             } **/
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "OrdersActivity");
    }
}
