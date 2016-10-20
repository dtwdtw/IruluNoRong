package com.wf.irulu.module.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.wf.irulu.logic.listener.IsAddWishListListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.home.adapter.HomeSpecialOffersRecyclerAdapter;
import com.wf.irulu.common.bean.HomeBean;
import com.wf.irulu.common.bean.SlideBean;

import java.util.ArrayList;

/**
 * @描述: Special Offers(特别优惠)页面
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.content.fragment
 * @类名:SpecialOffersFragment
 * @作者: 左杰
 * @创建时间:2015/10/24 14:31
 */
public class SpecialOffersFragment extends BaseContentFragment implements ServiceListener, IsAddWishListListener, XRecyclerView.LoadingListener, View.OnClickListener {
    private final String TAG = getClass().getCanonicalName();
    private String mId;
    private int mPage = 1;//页码
    private HomeSpecialOffersRecyclerAdapter mAdapter;
    private ArrayList<ProductInfo> mProductList;
    private ArrayList<HomeProduct> mHomeProductList;
    private int position;
    /**
     * 是否是下拉刷新
     */
    private boolean isPullDownToRefresh = false;
    /**
     * 轮播图布局
     */
    private View mCyclicView;
    private XRecyclerView mRecyclerView;
    private ImageView mBackToTopFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SpecialOffersFragment() {
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
//        mHomeProductList = homeProductList;
        this.position = position;//dtw
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview_layout);
        mBackToTopFab = (ImageView) findViewById(R.id.back_to_top_fab);
//        mRecyclerView.setArrowImageView(R.mipmap.default_ptr_flip);
        mRecyclerView.setLoadingListener(this);
        controller.registIsAddWishListListener(this);
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

    /**
     * 初始化数据
     */
    private void initData() {
        if (mHomeProductList == null) {
            return;
        }
        if (null != mHomeProductList) {
            boolean tag = true;//让mRecyclerView只添加一次头布局
            HomeProduct homeProduct = mHomeProductList.get(position);
            String type = homeProduct.getType();
            if ("1".equals(type)) {
                mId = homeProduct.getId();
                ArrayList<SlideBean> slideDatas = homeProduct.getSlide();
                mProductList = homeProduct.getProductList();
                int slideSize = slideDatas.size();

                if (mAdapter == null) {
                    //让其一进来自动加载数据
                    mRecyclerView.showRefreshHeader();
                    //联网请求加载数据
                    controller.getServiceManager().getHomeService().getHomeDataMore(mId, String.valueOf(1), String.valueOf(HttpConstantUtils.PAGE_SIZE), true, this);
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
                mRecyclerView.setLayoutManager(gridLayoutManager);

                mCyclicView = LayoutInflater.from(mActivity).inflate(R.layout.item_shuffling_figure, (ViewGroup) findViewById(android.R.id.content), false);
                RelativeLayout cyclicImage = (RelativeLayout) mCyclicView.findViewById(R.id.cyclic_image_layout);//轮播图布局
                if (slideSize > 0 && tag) {
                    cyclicImage.setVerticalGravity(View.VISIBLE);

                    mRecyclerView.addHeaderView(mCyclicView);
                    setShufflingFigure(mActivity, mCyclicView, new ArrayList<SlideBean>());
                    tag = false;
                } else
                    cyclicImage.setVisibility(View.GONE);
            }
            mAdapter = new HomeSpecialOffersRecyclerAdapter(mActivity, mProductList, controller);
            mRecyclerView.setAdapter(mAdapter);
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
         * 联网请求加载数据
         */
        controller.getServiceManager().getHomeService().getHomeDataMore(mId, String.valueOf(mPage), String.valueOf(HttpConstantUtils.PAGE_SIZE), false, this);

        ILog.e("hellolove page", mPage + "");
        ILog.e("hellolove size",HttpConstantUtils.PAGE_SIZE+"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        controller.unRegistIsAddWishListListener(this);
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
                        setShufflingFigure(mActivity, mCyclicView, new ArrayList<SlideBean>());///dtw
                    }
                    mAdapter.setNotifyDataSetChanged(mProductList);
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
                        mAdapter.setNotifyDataSetChanged(mProductList);
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
        if (mProductList != null) {
            int size = mProductList.size();
            for (int i = 0; i < size; i++) {
                ProductInfo productInfo = mProductList.get(i);
                String productIdStr = productInfo.getProductId();
                if (productId.equals(productIdStr)) {
                    productInfo.setAddWishList(addWishList);
                    mAdapter.setNotifyDataSetChanged(mProductList);
                    break;
                }
            }
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
