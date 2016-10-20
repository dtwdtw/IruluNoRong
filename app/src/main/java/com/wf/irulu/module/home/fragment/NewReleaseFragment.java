package com.wf.irulu.module.home.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.HomeProduct;
import com.wf.irulu.common.bean.ProductInfo;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.HttpConstantUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.view.xrecyclerview.XRecyclerView;
import com.wf.irulu.framework.BaseContentFragment;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.home.adapter.HomeNewReleaseRecyclerAdapter;
import com.wf.irulu.common.bean.HomeBean;
import com.wf.irulu.common.bean.SlideBean;

import java.util.ArrayList;

/**
 * @描述: New Release(新发布)界面
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.content.fragment
 * @类名:NewReleaseFragment
 * @作者: 左杰
 * @创建时间:2015/10/28 17:59
 */
public class NewReleaseFragment extends BaseContentFragment implements ServiceListener, XRecyclerView.LoadingListener, View.OnClickListener {
    private final String TAG = getClass().getCanonicalName();
    private int mPage = 1;//页码
    private String mId;
    private ArrayList<ProductInfo> mProductList;
    private HomeNewReleaseRecyclerAdapter mNewReleaseAdapter;
    /**
     * 是否是下拉刷新
     */
    private boolean isPullDownToRefresh = false;
    private ArrayList<HomeProduct> mHomeProductList;
    private int position;
    /**
     * 轮播图布局
     */
    private View mCyclicView;
    private XRecyclerView mRecyclerView;
    private ImageView mBackToTopFab;

    @Override
    protected View init() {
        setContentView(R.layout.fragment_home_layout);
        initView();
        initData();
        return mMainView;
    }

    private void initView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview_layout);
//        mRecyclerView.setArrowImageView(R.mipmap.default_ptr_flip);
        mBackToTopFab = (ImageView) findViewById(R.id.back_to_top_fab);
        mRecyclerView.setLoadingListener(this);

        mBackToTopFab.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ILog.i(TAG, "mRecyclerView.computeVerticalScrollOffset()=" + mRecyclerView.computeVerticalScrollOffset());
                if (mRecyclerView.computeVerticalScrollOffset() > ConstantsUtils.DISPLAYH) {
                    mBackToTopFab.setVisibility(View.VISIBLE);
                } else {
                    mBackToTopFab.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void loadData(HomeBean homeBean) {
//        mHomeProductList = homeProductList; //dtw
        this.position = position;
    }

    private void initData() {
        if (mHomeProductList == null) {
            return;
        }
        if (null != mHomeProductList) {
            boolean tag = true;//让mRecyclerView只添加一次头布局
            HomeProduct homeProduct = mHomeProductList.get(position);
            String type = homeProduct.getType();
            if ("3".equals(type)) {
                mId = homeProduct.getId();
                ArrayList<SlideBean> slideDatas = homeProduct.getSlide();
                mProductList = homeProduct.getProductList();
                int slideSize = slideDatas.size();

                LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);

                mCyclicView = LayoutInflater.from(mActivity).inflate(R.layout.item_shuffling_figure, (ViewGroup) findViewById(android.R.id.content), false);
                RelativeLayout cyclicImage = (RelativeLayout) mCyclicView.findViewById(R.id.cyclic_image_layout);//轮播图布局
                if (slideSize > 0 && tag) {
                    cyclicImage.setVerticalGravity(View.VISIBLE);

                    mRecyclerView.addHeaderView(mCyclicView);
                    setShufflingFigure(mActivity, mCyclicView, new ArrayList<SlideBean>());//dtw
                    tag = false;
                } else
                    cyclicImage.setVisibility(View.GONE);
            }
            mNewReleaseAdapter = new HomeNewReleaseRecyclerAdapter(mActivity, mProductList);
            mRecyclerView.setAdapter(mNewReleaseAdapter);
        }
    }

    @Override
    public void onRefresh() {
        isPullDownToRefresh = true;
        /**
         * 联网请求第一页数据
         */
        controller.getServiceManager().getHomeService().getHomeDataMore(mId, String.valueOf(1), String.valueOf(HttpConstantUtils.PAGE_SIZE), true, this);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        /**
         * 联网请求第n页数据
         */
        controller.getServiceManager().getHomeService().getHomeDataMore(mId, String.valueOf(mPage), String.valueOf(HttpConstantUtils.PAGE_SIZE), false, this);
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
                    if (null != mCyclicView) {
                        setShufflingFigure(mActivity, mCyclicView, new ArrayList<SlideBean>());//dtw
                    }
                    mNewReleaseAdapter.setNotifyDataSetChanged(mProductList);

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
                        mNewReleaseAdapter.setNotifyDataSetChanged(mProductList);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        int page = Integer.parseInt((String) returnObj);
        if (page == 1) {
            mRecyclerView.refreshComplete();
        } else
            mRecyclerView.loadMoreComplete();
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        if (type == ServiceListener.TYPE_REFRESH_TIME_VAL) {
            mRecyclerView.refreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_top_fab:
                //回到顶部
                mRecyclerView.scrollToPosition(0);
//                mRecyclerView.smoothScrollToPosition(0);
                break;
        }
    }
}
