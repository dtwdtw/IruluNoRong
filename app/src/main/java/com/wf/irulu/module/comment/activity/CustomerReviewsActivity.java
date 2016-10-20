package com.wf.irulu.module.comment.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CustomerReviews;
import com.wf.irulu.common.bean.Product;
import com.wf.irulu.common.bean.Star;
import com.wf.irulu.common.bean.StarInfo;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshListView;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.aas.activity.LoginActivity;
import com.wf.irulu.module.comment.adapter.CommentListAdapter;

import java.util.List;

public class CustomerReviewsActivity extends CommonTitleBaseActivity implements ServiceListener, PullToRefreshBase.OnRefreshListener2 {

    /**
     * 下拉刷新界面
     */
    private PullToRefreshListView mCustomerPtrlComment;
    /**
     * 商品详情
     */
    private Product mProduct;
    /**
     * 商品ID
     */
    private String mPId;
    /**
     * 翻页页码
     */
    private int page = 1;
    /**
     * 每页的条目数
     */
    private int num = 20;
    /**
     * 评论列表适配器
     */
    private CommentListAdapter mAdapter;
    /**
     * 评论总数
     */
    private int total;
    /**
     * 评论详情
     */
    private CustomerReviews mCustmerReviews;
    /**
     * 上一次刷新的时间
     */
    private long mLastTime = 0;

    /**
     * 评论总数
     */
    private TextView mReviewsTvTotal;
    /**
     * 平均评分等级
     */
    private TextView mReviewsTvRate;
    /**
     * 平均评分
     */
    private RatingBar mReviewsRbRate;
    /**
     * 5个评分等级的评论数
     */

    TextView title;

    RelativeLayout relativeLayout;
    View view;
    private TextView mReviewsTvFiveNum, mReviewsTvFourNum, mReviewsTvThreeNum, mReviewsTvTwoNum, mReviewsTvOneNum;
    private Button mReviewBtWrite;
    /**
     * 5个评分等级的百分比进度条
     */
    private ProgressBar mReviewsPbFivePercent, mReviewsPbFourPercent, mReviewsPbThreePercent, mReivewsPbTwoPercent, mReviewsPbOnePercent;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCustomerPtrlComment.onRefreshComplete();
        }
    };
    private final int REQUEST_CODE_WRITE_REVIEW = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_customer_revices);
        relativeLayout = (RelativeLayout) findViewById(R.id.customlayout);
        view = getLayoutInflater().inflate(R.layout.loading_simple_waiting, null);
        title = (TextView) view.findViewById(R.id.commontitle_tv_text);
        title.setText(setWindowTitle());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.setLayoutParams(layoutParams);
        relativeLayout.addView(view);
    }

    @Override
    protected void setView() {
        super.setView();
        if (mReviewBtWrite == null) {
            mReviewBtWrite = new Button(this, null);
            mReviewBtWrite.setId(R.id.reviews_bt_write);
            int px = getResources().getDimensionPixelSize(R.dimen.title_arrow_width_height);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(px, px);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mReviewBtWrite.setLayoutParams(lp);
            mReviewBtWrite.setBackgroundResource(R.mipmap.reviews_edit);

        }
        if (mReviewBtWrite.getParent() != null) {
            ViewGroup vViewGroup = (ViewGroup) mReviewBtWrite.getParent();
            vViewGroup.removeView(mReviewBtWrite);
        }
        ViewGroup vViewGroup= (ViewGroup) commonTitleBack.getParent();
        vViewGroup.addView(mReviewBtWrite);
    }

    @Override
    public void initView() {
        mCustomerPtrlComment = bindView(R.id.customer_ptrl_comment);
    }

    @Override
    public void initData() {
        // 获取商品id
        mProduct = getIntent().getParcelableExtra("product");
        mPId = mProduct.getId();
        // 获取评论总览视图
        View headerView = View.inflate(this, R.layout.view_reviews_title, null);
        mReviewsPbFivePercent = (ProgressBar) headerView.findViewById(R.id.reviews_pb_five_percent);
        mReviewsPbFourPercent = (ProgressBar) headerView.findViewById(R.id.reviews_pb_four_percent);
        mReviewsPbThreePercent = (ProgressBar) headerView.findViewById(R.id.reviews_pb_three_percent);
        mReivewsPbTwoPercent = (ProgressBar) headerView.findViewById(R.id.reviews_pb_two_percent);
        mReviewsPbOnePercent = (ProgressBar) headerView.findViewById(R.id.reviews_pb_one_percent);
        mReviewsTvFiveNum = (TextView) headerView.findViewById(R.id.reviews_tv_five_num);
        mReviewsTvFourNum = (TextView) headerView.findViewById(R.id.reviews_tv_four_num);
        mReviewsTvThreeNum = (TextView) headerView.findViewById(R.id.reviews_tv_three_num);
        mReviewsTvTwoNum = (TextView) headerView.findViewById(R.id.reviews_tv_two_num);
        mReviewsTvOneNum = (TextView) headerView.findViewById(R.id.reviews_tv_one_num);
        mReviewsTvTotal = (TextView) headerView.findViewById(R.id.reviews_tv_total);
        mReviewsTvRate = (TextView) headerView.findViewById(R.id.reviews_tv_rate);
        mReviewsRbRate = (RatingBar) headerView.findViewById(R.id.reviews_rb_rate);
        // 添加到下拉刷新列表视图中－
        mCustomerPtrlComment.getRefreshableView().addHeaderView(headerView);
        // 设置刷新模式为不允许操作
        mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.DISABLED);
        // 设置刷新加载回调监听
        mCustomerPtrlComment.setOnRefreshListener(this);
        // 获取评论详情
        controller.getServiceManager().getProductService().getCommentDetail(mPId, page, num, this);
//        // 设置下架产品不可评论
        if (mProduct.getStatu() != 1) {
            mReviewBtWrite.setEnabled(false);
        } else {
            mReviewBtWrite.setEnabled(true);
        }
    }

    @Override
    public void initDataView() {
        super.initDataView();
    }

    @Override
    protected String setWindowTitle() {
        return "Customer Reviews";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reviews_bt_write:
                if (controller.getCacheManager().getLoginUser().getUid() == -1 || controller.getCacheManager().getLoginUser().getUid() == 0) {
                    showToast(R.string.log_in_first);
                    LoginActivity.enterLoginActivity(this, "");
                    return;
                }
                MyReviewsActivity.startMyReviewsActivityOnResult(this, mProduct, "", REQUEST_CODE_WRITE_REVIEW);
                break;
            default:
                break;
        }
    }

    private void stop() {
        SystemClock.sleep(1000);
        mCustomerPtrlComment.onRefreshComplete();
    }

    /**
     * 跳转评论详情
     */
    public static void startCustomerRevicesActivity(Activity pActivity, Product pProduct) {
        Intent intent = new Intent(pActivity, CustomerReviewsActivity.class);
        intent.putExtra("product", pProduct);
        pActivity.startActivity(intent);
    }

    public void setTitle() {
        StarInfo starInfo = mCustmerReviews.getStarInfo();
        mReviewsTvTotal.setText(getString(R.string.num_ratings, String.valueOf(starInfo.getTotal())));
        mReviewsTvRate.setText(getRatingsRatio(starInfo.getAve()));
        mReviewsRbRate.setRating(Float.parseFloat(starInfo.getAve()));
        List<Star> stars = starInfo.getStarList();
        mReviewsTvFiveNum.setText(String.valueOf(stars.get(0).getStarCount()));
        mReviewsTvFourNum.setText(String.valueOf(stars.get(1).getStarCount()));
        mReviewsTvThreeNum.setText(String.valueOf(stars.get(2).getStarCount()));
        mReviewsTvTwoNum.setText(String.valueOf(stars.get(3).getStarCount()));
        mReviewsTvOneNum.setText(String.valueOf(stars.get(4).getStarCount()));
        total = mCustmerReviews.getStarInfo().getTotal();
        if (total != 0) {
            // 5星评论
            mReviewsPbFivePercent.setMax(total);
            mReviewsPbFivePercent.setProgress(stars.get(0).getStarCount());
            // 4星评论
            mReviewsPbFourPercent.setMax(total);
            mReviewsPbFourPercent.setProgress(stars.get(1).getStarCount());
            // 3星评论
            mReviewsPbThreePercent.setMax(total);
            mReviewsPbThreePercent.setProgress(stars.get(2).getStarCount());
            // 2星评论
            mReivewsPbTwoPercent.setMax(total);
            mReivewsPbTwoPercent.setProgress(stars.get(3).getStarCount());
            // 1星评论
            mReviewsPbOnePercent.setMax(total);
            mReviewsPbOnePercent.setProgress(stars.get(4).getStarCount());
        }

    }


    private SpannableString getRatingsRatio(String currentRatings) {

        SpannableString vSpannableString = new SpannableString(currentRatings + "/5.0");
        int length = vSpannableString.length();
        Resources vResources = getResources();
        vSpannableString.setSpan(new ForegroundColorSpan(vResources.getColor(R.color.product_title)), 0, length - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        vSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, length - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        vSpannableString.setSpan(new AbsoluteSizeSpan(vResources.getDimensionPixelSize(R.dimen.textsize_thirty)), 0, length - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        vSpannableString.setSpan(new ForegroundColorSpan(vResources.getColor(R.color.irulu_reply)), length - 4, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        vSpannableString.setSpan(new AbsoluteSizeSpan(vResources.getDimensionPixelSize(R.dimen.textsize_eleven)), length - 4, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return vSpannableString;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 1;
        // 设置超过20秒后允许刷新
        if (SystemClock.currentThreadTimeMillis() - mLastTime >= 1000 * 20) {
            mLastTime = SystemClock.currentThreadTimeMillis();
            controller.getServiceManager().getProductService().getCommentDetail(mPId, page, num, this);
        } else {
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page++;
        controller.getServiceManager().getProductService().getCommentDetail(mPId, page, num, this);
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case COMMENT_LIST:
                // 根据页码判断时上拉加载还是下拉刷新
                if (page == 1) {
                    if (view.getParent() != null) {
                        relativeLayout.removeView(view);
                        displayNoDataView(R.layout.activity_customer_revices);
//                        title=(TextView)findViewById(R.id.commontitle_tv_text);
//                        title.setText(setWindowTitle());
                        setView();
                        initView();
                        initData();
                    }
                    mCustmerReviews = (CustomerReviews) returnObj;
                    // 获取评论综述
                    total = mCustmerReviews.getStarInfo().getTotal();
                    // 判断适配器是否为空从而得到是第一次加载还是刷新模式
                    if (mAdapter == null) {
                        // 第一次加载
                        mAdapter = new CommentListAdapter(mCustmerReviews.getCommentList(), this);
                        mCustomerPtrlComment.setAdapter(mAdapter);
                        mReviewBtWrite.setOnClickListener(this);    //写评论
                    } else {
                        // 刷新
                        mAdapter.refresh(mCustmerReviews.getCommentList());
                        // 停止下拉刷新
                        mCustomerPtrlComment.onRefreshComplete();
                    }
                    // 设置标题
                    setTitle();
                } else {
                    CustomerReviews reviews = (CustomerReviews) returnObj;
                    // 当当前获取的列表不为空的时候
                    if (reviews != null || reviews.getCommentList() != null || reviews.getCommentList().size() != 0) {
                        // 当当前评论列表不包含获取到的评论内容时
                        if (!mCustmerReviews.getCommentList().contains(reviews.getCommentList())) {
                            // 添加评论内容
                            mCustmerReviews.getCommentList().addAll(reviews.getCommentList());
                            // 刷新列表
                            mAdapter.refresh(mCustmerReviews.getCommentList());
                        }
                    }
                    // 停止上拉加载
                    mCustomerPtrlComment.onRefreshComplete();
                }
                int size = mCustmerReviews.getCommentList().size();
                // 当总评论数大于当前获取的评论数的时候，设置刷新模式为上拉加载＋下拉刷新
                if (total > size) {
                    mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    // 否则只允许下拉刷新
                    mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case COMMENT_LIST:
                if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {

                    if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                        showToast(getString(R.string.server_network_failed));
                    } else {
                        showToast(returnObj.toString());
                    }
                    displayNoDataView(R.layout.no_data_simple_title_page);
                    findViewById(R.id.no_data_bt_again).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setContentView();
                            initView();
                            initData();
                        }
                    });

                }
                // 当页码为1的时候
                if (page == 1) {
                    // 设置刷新模式只允许刷新
                    mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    int size = mCustmerReviews.getCommentList().size();
                    // while reviews'size is larger than the current reviews that customer get
                    // 当总评论数大于当前获取的评论数的时候，设置刷新模式为上拉加载＋下拉刷新
                    if (page > 1) {
                        page--;
                    }
                    if (total > size) {
                        mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        // 否则只允许下拉刷新
                        mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }
                break;
            default:
                break;
        }
        mCustomerPtrlComment.setMode(PullToRefreshBase.Mode.BOTH);
        if (mCustomerPtrlComment.isRefreshing()) {
            mCustomerPtrlComment.onRefreshComplete();
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_REVIEW) {
            if (resultCode == RESULT_OK) {
                page = 1;
                controller.getServiceManager().getProductService().getCommentDetail(mPId, page, num, this);
            }
        }
    }
}
