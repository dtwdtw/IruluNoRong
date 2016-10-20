package com.wf.irulu.module.homenew1_3_0.DiscoverFragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wf.irulu.R;
import com.wf.irulu.common.bean.HomeProduct;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.HttpConstantUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.view.HorizontialListView;
import com.wf.irulu.common.view.xrecyclerview.XRecyclerView;
import com.wf.irulu.framework.BaseContentFragment;
import com.wf.irulu.logic.listener.IsAddWishListListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.homenew1_3_0.EventActivity.EventActivity;
import com.wf.irulu.common.bean.HomeBean;
import com.wf.irulu.common.bean.SlideBean;
import com.wf.irulu.module.newArrivals.NewArrivalsActivity;
import com.wf.irulu.module.product.activity.DailyDealsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by dtw on 16/4/13.
 */
public class DiscoverFragment1_3_0 extends BaseContentFragment implements ServiceListener, IsAddWishListListener, XRecyclerView.LoadingListener, View.OnClickListener, DiscoverDataListener {
    private final String TAG = getClass().getCanonicalName();
    private String mFirstId;
    private int mPage = 1;//页码
    private NewArrivalsAdapter mFirstAdapter;
    private HomeBean.DiscoverBean discoverBean;
    private List<ProductInfo> firstProductListBeen;
    private HorizontialListView horizontialListView;
    private List<ProductInfo> mProductList;

    private String msecondId;
    private RecommendAdapter msecondAdapter;
    private List<ProductInfo> secondProductListBeen;
    private ImageView eventdealsImageView;
    private ImageView dailydealsImageView;

    private LinearLayout dailydealslayout;
    /**
     * 是否是下拉刷新
     */
    private boolean isPullDownToRefresh = false;
    /**
     * 轮播图布局
     */
//    private View mCyclicView;
    private XRecyclerView mRecyclerView;
    private ImageView mBackToTopFab;
    private View mMenuLayout;

    private RecyclerView headRecocleView;

    private TextView dealytitle;
    //    private TextView dealytimehh;
//    private TextView dealytimehl;
//    private TextView dealytimemh;
//    private TextView dealytimeml;
//    private TextView dealytimesh;
//    private TextView dealytimesl;
    private TextView dealypricenow;
    private TextView dealypriceold;
    private TextView dealytime;
    private RelativeLayout cyclicImage;
    private CountDownTimer countDownTimer;
    private DiscoverPresenter discoverPresenter;
    FrameLayout eventdealsLine;

    private Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discoverPresenter = DiscoverPresenter.getInstence();
        discoverPresenter.setDiscoverDataListener(this);
    }

    public DiscoverFragment1_3_0() {
    }

    @Override
    protected View init() {
        setContentView(R.layout.fragment_home_layout);
        initView();
        initData();
        return mMainView;
    }

    /**
     * 获取初始化数据
     *
     * @param homeBean 数据
     */
    @Override
    public void loadData(HomeBean homeBean) {
        discoverBean = homeBean.getDiscover();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mMenuLayout = LayoutInflater.from(mActivity).inflate(R.layout.discoverfragment1_3_0, null);
        eventdealsLine = (FrameLayout) mMenuLayout.findViewById(R.id.eventdealsline);
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview_layout);
        mBackToTopFab = (ImageView) findViewById(R.id.back_to_top_fab);
        eventdealsImageView = (ImageView) mMenuLayout.findViewById(R.id.eventdeals);
        dailydealsImageView = (ImageView) mMenuLayout.findViewById(R.id.dailydealsimg);
        dailydealslayout = (LinearLayout) mMenuLayout.findViewById(R.id.dailydealslayout);
        eventdealsImageView.setOnClickListener(this);
        dailydealslayout.setOnClickListener(this);

        dealytitle = (TextView) mMenuLayout.findViewById(R.id.dailydealstitle);
//        dealytimehh = (TextView) findViewById(R.id.timehh);
//        dealytimehl = (TextView) findViewById(R.id.timehl);
//        dealytimemh = (TextView) findViewById(R.id.timemh);
//        dealytimeml = (TextView) findViewById(R.id.timeml);
//        dealytimesh = (TextView) findViewById(R.id.timesh);
//        dealytimesl = (TextView) findViewById(R.id.timesl);
        dealypricenow = (TextView) mMenuLayout.findViewById(R.id.pricenow);
        dealypriceold = (TextView) mMenuLayout.findViewById(R.id.priceold);
        dealypriceold.getPaint().setAntiAlias(true);
        dealytime = (TextView) mMenuLayout.findViewById(R.id.dealytime);

//        mRecyclerView.setArrowImageView(R.mipmap.default_ptr_flip);
        mRecyclerView.setLoadingListener(this);
        controller.registIsAddWishListListener(this);
        mBackToTopFab.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ILog.i(TAG, "mRecyclerView.computeVerticalScrollOffset()=" + mRecyclerView.computeVerticalScrollOffset());
                Log.e("hellolove", mRecyclerView.computeVerticalScrollOffset() + "");
                if (mRecyclerView.computeVerticalScrollOffset() > ConstantsUtils.DISPLAYH) {
                    mBackToTopFab.setVisibility(View.VISIBLE);
                } else {
                    mBackToTopFab.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {


        if (discoverBean == null) {
            return;
        }
        if (null != discoverBean) {
            boolean tag = true;//让mRecyclerView只添加一次头布局
            List<SlideBean> slideDatas = discoverBean.getSlide();
            int slideSize = slideDatas.size();
            cyclicImage = (RelativeLayout) mMenuLayout.findViewById(R.id.cyclic_image_layout);//轮播图布局
            headRecocleView = (RecyclerView) mMenuLayout.findViewById(R.id.headrecycleview);
            if (slideSize > 0 && tag) {
                cyclicImage.setVerticalGravity(View.VISIBLE);
                setShufflingFigure(mActivity, mMenuLayout, slideDatas);
                tag = false;
            } else
                cyclicImage.setVisibility(View.GONE);

            TextView precent = (TextView) mMenuLayout.findViewById(R.id.precent);
            precent.setText(discoverBean.getDaily_deals().getProduct().getPercent() + "%\nOFF");

            HomeBean.DiscoverBean.NewArrivalsBean newArrivalsBean = discoverBean.getNew_arrivals();
            HomeBean.DiscoverBean.RecommendationBean recommendationBean = discoverBean.getRecommendation();
            String fristtype = newArrivalsBean.getType();
            String secondtype = recommendationBean.getType();

            horizontialListView = (HorizontialListView) mMenuLayout.findViewById(R.id.menulayout);
            horizontialListView.setAdapter(new CataMenuListAdapter(mActivity, discoverBean.getCatalog()));

            int eventwidth = mActivity.getResources().getDisplayMetrics().widthPixels;
            int eventheight = eventwidth * 30 / 75;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) eventdealsImageView.getLayoutParams();
            layoutParams.width = eventwidth;
            layoutParams.height = eventheight;

            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) dailydealsImageView.getLayoutParams();
            layoutParams1.width = eventwidth * 30 / 75;
            layoutParams1.height = eventwidth * 30 / 75;

            if (discoverBean.getEvents_deals() != null) {
                eventdealsImageView.setLayoutParams(layoutParams);
                Picasso.with(mActivity).load(discoverBean.getEvents_deals().getEventInfo().getImage()).into(eventdealsImageView);
            } else {
                eventdealsImageView.setVisibility(View.GONE);
                eventdealsLine.setVisibility(View.GONE);
            }
            if (discoverBean.getDaily_deals() != null&& !discoverBean.getDaily_deals().getProduct().getImage().isEmpty()) {
                dailydealsImageView.setLayoutParams(layoutParams1);
                Picasso.with(mActivity).load(discoverBean.getDaily_deals().getProduct().getImage()).into(dailydealsImageView);
                dealypricenow.setText(StringUtils.getPriceByFormat(Double.valueOf(discoverBean.getDaily_deals().getProduct().getDiscountPrice())));
                dealypriceold.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                dealypriceold.setText(StringUtils.getPriceByFormat(Double.valueOf(discoverBean.getDaily_deals().getProduct().getPrice())));
            } else {
                dailydealslayout.setVisibility(View.GONE);
            }


//            String timetext= DateFormatUtils.DateFormat(discoverBean.getDaily_deals().getLimit_time()*1000,"HH:mm:ss");
//            timetext=timetext.replaceAll(".{1}(?!$)", "$0 ");
//            Log.e("hellolove",discoverBean.getDaily_deals().getLimit_time()+"");
//            dealytime.setText(TimeUtils.getSPTime(mActivity,timetext));
//            if(countDownTimer==null) {
            countDownTimer = new CountDownTimer(discoverBean.getDaily_deals().getLimit_time() * 1000 - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-0:00"));
                    Date date = new Date(millisUntilFinished);
                    String timetext = simpleDateFormat.format(date);
                    timetext = timetext.replaceAll(".{1}(?!$)", "$0 ");
                    dealytime.setText(TimeUtils.getSPTime(mActivity, timetext));
                }

                @Override
                public void onFinish() {

                }
            }.start();
//            }

            if ("2".equals(fristtype)) {
                headRecocleView.setVisibility(View.VISIBLE);
                mFirstId = recommendationBean.getId();
                firstProductListBeen = newArrivalsBean.getProductList();
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
                headRecocleView.setLayoutManager(gridLayoutManager);
//                mCyclicView = LayoutInflater.from(mActivity).inflate(R.layout.item_shuffling_figure, (ViewGroup) findViewById(android.R.id.content), false);
                mFirstAdapter = new NewArrivalsAdapter(mActivity, firstProductListBeen, controller);
                headRecocleView.setAdapter(mFirstAdapter);
                mMenuLayout.findViewById(R.id.firstline).setVisibility(View.VISIBLE);

                TextView firsttitle = (TextView) mMenuLayout.findViewById(R.id.firsttitle);
                firsttitle.setText(newArrivalsBean.getTitle());

                mMenuLayout.findViewById(R.id.firstline).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, NewArrivalsActivity.class);
                        intent.putExtra("id", discoverBean.getNew_arrivals().getId());
                        startActivity(intent);
                    }
                });
            }
            mRecyclerView.clearHeaderView();
            mRecyclerView.addHeaderView(mMenuLayout);


            if ("1".equals(secondtype)) {
                TextView secondtitle = (TextView) mMenuLayout.findViewById(R.id.secondtitle);
                secondtitle.setText(recommendationBean.getTitle());
                mMenuLayout.findViewById(R.id.secondline).setVisibility(View.VISIBLE);

                msecondId = recommendationBean.getId();
                secondProductListBeen = recommendationBean.getProductList();
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
                mRecyclerView.setLayoutManager(gridLayoutManager);

//                mCyclicView = LayoutInflater.from(mActivity).inflate(R.layout.item_shuffling_figure, (ViewGroup) findViewById(android.R.id.content), false);
                msecondAdapter = new RecommendAdapter(mActivity, secondProductListBeen, controller);
                mRecyclerView.setAdapter(msecondAdapter);
                mProductList = secondProductListBeen;
            }
        }
    }

    @Override
    public void onRefresh() {
        isPullDownToRefresh = true;
        /**
         * 联网请求第一页数据
         */
//        controller.getServiceManager().getHomeService().getHomeDataMore("1", String.valueOf(1), String.valueOf(HttpConstantUtils.PAGE_SIZE), true, this);
        discoverPresenter.pullData();
    }


    @Override
    public void onLoadMore() {
        mPage++;
        /**
         * 联网请求加载数据
         */
        controller.getServiceManager().getHomeService().getHomeDataMore("1", String.valueOf(mPage), String.valueOf(HttpConstantUtils.PAGE_SIZE), false, this);

        ILog.e("hellolove page", mPage + "");
        ILog.e("hellolove size", HttpConstantUtils.PAGE_SIZE + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        controller.unRegistIsAddWishListListener(this);
        countDownTimer.cancel();
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case HOME_MORE:
                int page = Integer.parseInt((String) bandObj);
                HomeProduct mHomeProduct = (HomeProduct) returnObj;
                ArrayList<ProductInfo> productList = mHomeProduct.getProductList();
                if (page == 1) {//下拉刷新
                    mRecyclerView.refreshComplete();
                    mProductList = productList;
                    ArrayList<SlideBean> slideDatas = mHomeProduct.getSlide();
                    if (null != cyclicImage) {
                        setShufflingFigure(mActivity, cyclicImage, slideDatas);
                    }
                    msecondAdapter.setNotifyDataSetChanged(mProductList);
                    isPullDownToRefresh = false;
                    mPage = 1;
                } else {//上拉加载
                    mRecyclerView.loadMoreComplete();
                    if (productList.size() == 0) {
                        mRecyclerView.noMoreLoading();
                        return;
                    }
                    if (!mProductList.containsAll(productList)) {
                        mProductList.addAll(productList);
                        msecondAdapter.setNotifyDataSetChanged(mProductList);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if (returnObj != null) {
            int page = Integer.parseInt((String) returnObj);
            if (page == 1) {
                mRecyclerView.refreshComplete();
            } else
                mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        if (type == ServiceListener.TYPE_REFRESH_TIME_VAL) {
            mRecyclerView.refreshComplete();
        }
    }

    @Override
    public void isAddWishList(String productId, String addWishList) {
//        if (mProductList != null) {
//            int size = mProductList.size();
//            for (int i = 0; i < size; i++) {
//                ProductInfo productInfo = mProductList.get(i);
//                String productIdStr = productInfo.getProductId();
//                if (productId.equals(productIdStr)) {
//                    productInfo.setAddWishList(addWishList);
//                    mAdapter.setNotifyDataSetChanged(mProductList);
//                }
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_top_fab:
                //回到顶部
//                mRecyclerView.scrollToPosition(0);
                mRecyclerView.smoothScrollToPosition(0);
                break;
            case R.id.dailydealslayout:
//                ProductDetailActivity.startProductDetailActivity(mActivity, discoverBean.getDaily_deals().getProduct().getId());
                Intent i = new Intent(mActivity, DailyDealsActivity.class);
                startActivity(i);
                break;
            case R.id.eventdeals:
//                ProductDetailActivity.startProductDetailActivity(mActivity,discoverBean.getEvents_deals().getEventInfo().);
//                Bundle bundle=new Bundle();
                Intent intent = new Intent(mActivity, EventActivity.class);
                intent.putExtra("url", discoverBean.getEvents_deals().getEventInfo().getContent());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDiscoverBean(HomeBean.DiscoverBean discoverBean) {
        this.discoverBean = discoverBean;
        handler.post(new Runnable() {
            @Override
            public void run() {
                isPullDownToRefresh = false;
                mPage = 1;
                mRecyclerView.refreshComplete();
                initData();

            }
        });
    }
}