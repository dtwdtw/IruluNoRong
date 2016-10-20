package com.wf.irulu.module.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.bean.ProductSearch;
import com.wf.irulu.common.bean.SearchResult;
import com.wf.irulu.common.bean.Sort;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.view.ClearEditText;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshListView;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.logic.db.RecentSearchDbHelper;
import com.wf.irulu.logic.listener.NoInternetConnListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.adapter.ProductSearchAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/*搜索结果返回界面*/
public class SearchResultActivity extends BaseActivity implements ServiceListener, PullToRefreshBase.OnRefreshListener2, NoInternetConnListener {

    /**
     * 搜索关键词
     */
    private String mKeyWord;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 每页条数
     */
    private int num = 20;
    /**
     * 关键词
     */
    private ClearEditText mResultCetKeyword;
    /**
     * 最佳匹配
     */
    private TextView mResultTvMatch;
    /**
     * 价格排序
     */
    private TextView mResultTvPrice;
    /**
     * 购买量排序
     */
    private TextView mResultTvOrder;
    /**
     * 价格排序
     */
    private LinearLayout mResultLlOrder;
    /**
     * 购买量排序
     */
    private LinearLayout mResultLlPrice;
    /**
     * 动画特效
     */
    private AnimationDrawable animationDrawable;
    /**
     * 动画控件
     */
    private ImageView commonLoadingIvAnim;
    /**
     * 最佳匹配列表
     */
    private ArrayList<ProductSearch> mResultProducts;
    /**
     * 展示关键词
     */
    private ArrayList<String> mWordsShow;
    /**
     * 搜索结果适配器
     */
    private ProductSearchAdapter mResultAdapter;
    /**
     * 排序字符
     */
    private String mSortWord;
    /**
     * 排序方式
     */
    private int mSort;
    /**
     * 产品列表
     */
    private PullToRefreshListView mResultPtrlvProducts;
    /**
     * 关键词对应表
     */
    private HashMap<String, String> mSearchKeyWordMap;


    private boolean mIsNotConnect = false;

    private boolean firstIN = true;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search_result);
    }

    @Override
    public void initView() {
        commonLoadingIvAnim = bindView(R.id.loading_iv_anim);
        animationDrawable = (AnimationDrawable) commonLoadingIvAnim.getBackground();
        animationDrawable.start();
        mResultTvMatch = bindView(R.id.result_tv_match);
        mResultTvPrice = bindView(R.id.result_tv_price);
        mResultTvOrder = bindView(R.id.result_tv_order);
        mResultLlOrder = bindView(R.id.result_ll_price);
        mResultLlPrice = bindView(R.id.result_ll_order);
        mResultCetKeyword = bindView(R.id.result_cet_keyword);
        bindView(R.id.result_iv_back).setOnClickListener(this);
        mResultPtrlvProducts = bindView(R.id.result_ptrlv_products);
        mResultPtrlvProducts.getRefreshableView().addHeaderView(getLayoutInflater().inflate(R.layout.view_split_gray, null));
        mResultPtrlvProducts.setMode(PullToRefreshBase.Mode.DISABLED);
        mResultPtrlvProducts.setOnRefreshListener(this);
        mResultCetKeyword.setClearIconVisible(true);
        mResultCetKeyword.setClearIconClickable(false);
        mResultCetKeyword.setClickable(true);
        mResultCetKeyword.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mSearchKeyWordMap = new HashMap<String, String>();
        mKeyWord = getIntent().getStringExtra("keyword");
        RecentSearchDbHelper dbHelper = new RecentSearchDbHelper(controller);
        dbHelper.insertKeyWord(mKeyWord);
        mResultCetKeyword.setText(mKeyWord);
        mResultCetKeyword.setFocusable(false);
        mResultCetKeyword.setFocusableInTouchMode(false);
        controller.getServiceManager().getProductService().getSearchResult(mKeyWord, "default", 0, page, num, this, true);
        mResultPtrlvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pId = ((ProductSearch) mResultAdapter.getItem(position - 2)).getId();
                ProductDetailActivity.startProductDetailActivity(SearchResultActivity.this, String.valueOf(pId));
            }
        });
    }

    @Override
    protected void setView() {
        super.setView();


//        // 返回键
//        if (commonTitleBack != null) {
//            commonTitleBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isFinish()) {
//                        finish();
//                    }
//                }
//            });
//        }
    }

    public void displayNoDataView(int viewId) {
        setContentView(viewId);
        setView();
    }

    @Override
    public void onClick(View v) {
        String sortWord = null;
        switch (v.getId()) {
            case R.id.result_tv_match:
                page = 1;
                // 最佳匹配
                sortWord = mSearchKeyWordMap.get(mWordsShow.get(0));
                if (mSortWord.equals(sortWord)) {
                    // 如果当前在本组排序方式中
                    return;
                }
                mSortWord = sortWord;
                // 设置文本颜色
                // 当前文本颜色选中
                mResultTvMatch.setTextColor(getResources().getColor(R.color.blue_line_image));
                // 非当前文本颜色置灰
                mResultTvPrice.setTextColor(getResources().getColor(R.color.order_detail_content));
                mResultTvOrder.setTextColor(getResources().getColor(R.color.order_detail_content));
                // 设置指示图片
                mResultTvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_03), null);
                mResultTvOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_bottom), null);
                mSort = 0;
                controller.getServiceManager().getProductService().getSearchResult(mKeyWord, mSortWord, mSort, page, num, this, false);
                break;
            case R.id.result_ll_price:
                page = 1;
                // 价格
                sortWord = mSearchKeyWordMap.get(mWordsShow.get(1));


                // 设置文本颜色
                // 当前文本颜色选中
                mResultTvPrice.setTextColor(getResources().getColor(R.color.blue_line_image));
                // 非当前文本颜色置灰
                mResultTvMatch.setTextColor(getResources().getColor(R.color.order_detail_content));
                mResultTvOrder.setTextColor(getResources().getColor(R.color.order_detail_content));
                mSortWord = sortWord;
                // 排序方式
                if (mSort == 0) {
                    mSort = -1;
                } else {
                    mSort = -mSort;
                }
                // 排序指示器图片设置
                if (mSort == -1) {
                    mResultTvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_01), null);
                } else {
                    mResultTvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_02), null);
                }
                mResultTvOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_bottom), null);
                controller.getServiceManager().getProductService().getSearchResult(mKeyWord, mSortWord, mSort, page, num, this, false);
                break;
            case R.id.result_ll_order:
                page = 1;
                // 销量
                sortWord = mSearchKeyWordMap.get(mWordsShow.get(2));
                if (mSortWord.equals(sortWord)) {
                    return;
                }
                mSortWord = sortWord;
                // 设置文本颜色
                // 当前文本颜色选中
                mResultTvOrder.setTextColor(getResources().getColor(R.color.blue_line_image));
                // 非当前文本颜色置灰
                mResultTvPrice.setTextColor(getResources().getColor(R.color.order_detail_content));
                mResultTvMatch.setTextColor(getResources().getColor(R.color.order_detail_content));
                // 排序指示器图片设置
                mResultTvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_03), null);
                mResultTvOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.search_arrow_bottom_selected), null);
                // 排序方式
                mSort = 0;
                controller.getServiceManager().getProductService().getSearchResult(mKeyWord, mSortWord, mSort, page, num, this, false);
                break;
            case R.id.result_iv_back:
                finish();
                break;
            case R.id.result_cet_keyword:
                SearchActivity.startSearchActivity(this, mKeyWord);
                finish();
            default:
                break;
        }

    }

    public static void startSearchResultActivity(Activity activity, String keyWord) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra("keyword", keyWord);
        activity.startActivity(intent);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case PRODUCT_SEARCH:
                SearchResult result = (SearchResult) returnObj;
                if (result == null) {
                    mResultPtrlvProducts.onRefreshComplete();
                    mResultPtrlvProducts.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    return;
                }
                mWordsShow = new ArrayList<String>();
                int size = result.getSort().size();
                for (int i = 0; i < size; i++) {
                    Sort sort = result.getSort().get(i);
                    mWordsShow.add(sort.getName());
                    mSearchKeyWordMap.put(sort.getName(), sort.getWord());
                }
                if (mResultAdapter == null) {
                    mResultProducts = result.getList();
                    // 停止动效
                    animationDrawable.stop();
                    commonLoadingIvAnim.setVisibility(View.GONE);
                    // 设置空布局
                    View view = bindView(R.id.result_v_empty);
                    mResultPtrlvProducts.setEmptyView(view);

//                    mResultPtrlvProducts.setMode(PullToRefreshBase.Mode.DISABLED);
                    mResultAdapter = new ProductSearchAdapter(this, mResultProducts);
                    mResultPtrlvProducts.setAdapter(mResultAdapter);
                    mSortWord = mSearchKeyWordMap.get(mWordsShow.get(0));
//                    mResultTvMatch.setText(mWordsShow.get(0));
//                    mResultTvMatch.setText(mWordsShow.get(1));
//                    mResultTvMatch.setText(mWordsShow.get(2));
                    mResultTvMatch.setTextColor(getResources().getColor(R.color.blue_line_image));
                    mResultTvMatch.setOnClickListener(this);
                    mResultLlPrice.setOnClickListener(this);
                    mResultLlOrder.setOnClickListener(this);
                } else {

                    if (mResultAdapter.getCount() > 0) {
                        if (page == 1) {
                            mResultProducts = result.getList();
                            mResultAdapter.refresh(mResultProducts);
//                        mResultPtrlvProducts.getRefreshableView().setSelection(0);
                        } else {
                            if (!mResultProducts.containsAll(result.getList()) && result.getList() != null && result.getList().size() > 0) {
                                mResultProducts.addAll(result.getList());
                                mResultAdapter.refresh(mResultProducts);
                            }
                        }
                    }

                    if (mResultProducts == null || mResultProducts.size() < page * num) {
                        mResultPtrlvProducts.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        mResultPtrlvProducts.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                }
                mResultPtrlvProducts.onRefreshComplete();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {


            case PRODUCT_SEARCH:
                if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SearchResultActivity.this, SearchActivity.class));
                            SearchResultActivity.this.finish();
                        }
                    };
                    if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                        showToast(getString(R.string.server_network_failed));
                    } else {
                        showToast(returnObj.toString());
                    }
                    displayNoDataView(R.layout.searchnonetlayout);
                    findViewById(R.id.nonettitle).setOnClickListener(onClickListener);
                    findViewById(R.id.result_cet_keyword).setOnClickListener(onClickListener);
                    findViewById(R.id.result_tv_match).setOnClickListener(onClickListener);
                    findViewById(R.id.result_ll_price).setOnClickListener(onClickListener);
                    findViewById(R.id.result_ll_order).setOnClickListener(onClickListener);
                    findViewById(R.id.no_data_bt_again).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setContentView(R.layout.activity_search_result);
                            initView();
                            initData();
                        }
                    });

                }

                if (page > 1) {
                    page--;
                }
                mResultPtrlvProducts.onRefreshComplete();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        switch (action) {
            case PRODUCT_SEARCH:
                if (type == ServiceListener.TYPE_REFRESH_TIME_VAL) {
                    mResultPtrlvProducts.onRefreshComplete();
                    return;
                }
                if (!firstIN) {
                    boolean isRequesting = (Boolean) returnObj;
                    if (isRequesting) {
                        PageLoadDialog.showDialogForLoading(this, true, false);
                    } else {
                        PageLoadDialog.hideDialogForLoading();
                    }
                }
                firstIN = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 1;
        controller.getServiceManager().getProductService().getSearchResult(mKeyWord, mSortWord, mSort, page, num, this, true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page++;
        controller.getServiceManager().getProductService().getSearchResult(mKeyWord, mSortWord, mSort, page, num, this, false);
    }

    @Override
    public void noInternetConn(boolean pIsNotConnect) {
//        this.mIsNotConnect = pIsNotConnect;
//        if (mNoInternetLayout != null) {
//            if (pIsNotConnect) {
//                mNoInternetLayout.setVisibility(View.VISIBLE);//显示无网络连接状态
//            } else {
//                mNoInternetLayout.setVisibility(View.GONE);//不显示无网络连接状态
//            }
//        }
    }

//    private boolean mIsNotConnect = false;
//    @Override
//    public void noInternetConn(boolean pIsNotConnect) {
//
//        this.mIsNotConnect = pIsNotConnect;
//        if (mNoInternetLayout != null) {
//            if (pIsNotConnect) {
//                mNoInternetLayout.setVisibility(View.VISIBLE);//显示无网络连接状态
//            } else {
//                mNoInternetLayout.setVisibility(View.GONE);//不显示无网络连接状态
//            }
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获按下返回键事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "SearchResultActivity");
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

        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "SearchResultActivity");
    }
}
