package com.wf.irulu.module.product.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseFragment;
import com.wf.irulu.common.bean.DailyDealsInit;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.view.CustomItemDecoration;
import com.wf.irulu.common.view.xrecyclerview.XRecyclerView;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.adapter.DailyDealsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Author: zzh
 * Contact: zzh5659@qq.com
 * CreateDate: 16/4/12 上午11:12
 */


public class DallyDealsFragment extends BaseFragment implements ServiceListener {

    //
    public static final String TITLE = "Title";


    public static final String TAG = "DallyDealsFragment";
    public static final String LIMIT_TIME = "limit_time";
    public static final String PRODUCT_LIST = "ProductList";

    //数据数量
    private final int num = 6;
    private String[] titles = {"Deals Today", "Deals Tomorrow"};

    private RecyclerView rv;
    //根据此变量的值请求不同的数据(today,tomorrow)
    private String title;

    //
    private int type = -1;

    private List<DailyDealsInit.ProductListBean> productList;
    private DailyDealsAdapter adapter;
    private int page = 1;
    private long limitTime;
    private boolean isActive;
    private TextView limitTimeTV;
    private MyCountDownTimer myCountDownTimer;
    private TextView limitTimeTitleTv;

    public static Fragment newInstance(String title, List<DailyDealsInit.ProductListBean> productList, long limit_time) {
        DallyDealsFragment dallyDealsFragment = new DallyDealsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putParcelableArrayList(PRODUCT_LIST, (ArrayList<? extends Parcelable>) productList);
        bundle.putLong(LIMIT_TIME, limit_time);
        dallyDealsFragment.setArguments(bundle);
        return dallyDealsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        title = arguments.getString(TITLE);
        productList = arguments.getParcelableArrayList(PRODUCT_LIST);
        limitTime = arguments.getLong(LIMIT_TIME);

        if (title.equals(titles[0])) {
            type = 0;
        } else if (title.equals(titles[1])) {
            type = 1;
        }
        isActive = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setContentView(R.layout.fragment_daily_deals);

        initView();
        initData();
        return mMainView;

    }

    private void initView() {
        if (productList != null) { //有数据则显示
            limitTimeTV = (TextView) findViewById(R.id.dally_deals_limit_time);
            limitTimeTitleTv = (TextView) findViewById(R.id.dally_deals_limit_time_title);

            if (type==0) {
                limitTimeTitleTv.setText("Deals ends in : ");
            }else if(type==1){
                limitTimeTitleTv.setText("Deals starts in : ");
            }
            //倒计时
            countDown();
            rv = (RecyclerView) findViewById(R.id.dally_deals_rv);
            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//            rv.setPullRefreshEnabled(false);
            adapter = new DailyDealsAdapter(getContext(), productList,type,controller);

            rv.setAdapter(adapter);


            //TODO:上拉刷新,下拉加载,因服务器接口问题,后续完成
            //上拉刷新
//            rv.setLoadingListener(new XRecyclerView.LoadingListener() {
//                @Override
//                public void onRefresh() {
//                    //加载第一页数据
//                    if (type == -1) return;
//                    page = 1;
//                    controller.getServiceManager().getProductService().getDailDeals(page, num, type, DallyDealsFragment.this);
//                }
//
//                @Override
//                public void onLoadMore() {
//
//                    //加载下一页数据
//                    if (type == -1) return;
//                    controller.getServiceManager().getProductService().getDailDeals(page, num, type, DallyDealsFragment.this);
//                }
//            });

        } else { //没数据则显示无数据页面
            ILog.d(TAG, "initView:没有数据");
        }


    }

    /**
     * 倒计时
     */
    private void countDown() {


        myCountDownTimer = new MyCountDownTimer(limitTime * 1000- System.currentTimeMillis(), 1000);
        myCountDownTimer.start();

    }

    class MyCountDownTimer extends CountDownTimer{

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Log.d(TAG, "onTick() returned: 本地时间" + DateFormatUtils.DateFormat(System.currentTimeMillis(), "HH:mm:ss"));

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-0:00"));
            Date date=new Date(millisUntilFinished);
            String timetext = simpleDateFormat.format(date);

            limitTimeTV.setText(timetext);
            //

        }

        @Override
        public void onFinish() {

        }
    }

    private void initData() {


    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case DAILY_DEALS:
                onGetDaiyDealsDataSucess(bandObj, returnObj);
                break;
        }

    }

    /**
     * 获取特价产品成功
     *
     * @param bandObj
     * @param returnObj
     */
    private void onGetDaiyDealsDataSucess(Object bandObj, Object returnObj) {
//        if (page == 1) {
//            rv.refreshComplete();
//        } else if (page > 1) {
//            rv.loadMoreComplete();
//        }


        page++;

    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case DAILY_DEALS:
                onGetDaiyDealsDataFailure(returnObj, errorCode);
                break;
        }
    }

    /**
     * //获取特价产品失败
     *
     * @param returnObj
     * @param errorCode
     */
    private void onGetDaiyDealsDataFailure(Object returnObj, int errorCode) {
        Toast.makeText(getContext(), "获取数据失败!ErrCode:" + errorCode, Toast.LENGTH_SHORT).show();
//        rv.refreshComplete();

    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActive=false;
        myCountDownTimer.cancel();

    }
}
