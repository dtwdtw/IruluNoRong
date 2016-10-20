package com.wf.irulu.module.product.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CheckoutBean;
import com.wf.irulu.common.bean.GiftInfo;
import com.wf.irulu.common.bean.MaybeLike;
import com.wf.irulu.common.bean.Product;
import com.wf.irulu.common.bean.ProductDetail;
import com.wf.irulu.common.bean.ShareInfo;
import com.wf.irulu.common.bean.ShippingAddr;
import com.wf.irulu.common.bean.ShippingAddrBean;
import com.wf.irulu.common.bean.Standard;
import com.wf.irulu.common.bean.Type;
import com.wf.irulu.common.bean.TypeName;
import com.wf.irulu.common.bean.WishList;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.TwitterUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.CoveredTitleScrollView;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.IsAddWishListListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.aas.activity.LoginActivity;
import com.wf.irulu.module.comment.activity.CustomerReviewsActivity;
import com.wf.irulu.module.comment.activity.MyReviewsActivity;
import com.wf.irulu.module.comment.adapter.CommentListAdapter;
import com.wf.irulu.module.payment.activity.OrderConfirmationActivity;
import com.wf.irulu.module.product.adapter.CustomerLoveAdapter;
import com.wf.irulu.module.product.dialog.ParamsDialog;
import com.wf.irulu.module.product.dialog.ProductShareDialog;
import com.wf.irulu.module.product.listener.ParamsChoosedListener;
import com.wf.irulu.module.product.other.WrapHeightGridLayoutManager;
import com.wf.irulu.module.product.service.CustomLoveDecoration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class ProductDetailActivity extends CommonTitleBaseActivity implements ServiceListener, ParamsChoosedListener, SliderAdapter.OnSliderClickListener, IsAddWishListListener {

    /**
     * 商品id
     */
    private String mId;
    /**
     * 可滑动区域
     */
    private CoveredTitleScrollView mProductCtsvContent;
    /**
     * 图片展示区域
     */
    private SliderLayout mProductVpDetailImage;
    /**
     * 分享
     */
    private ImageView mProductIvShareFriend;
    /**
     * 添加到wish按钮
     */
    private CheckBox mProductCbAddWish;
    /**
     * 商品名称
     */
    private TextView mProductTvTitleName;
    /**
     * 评论等级
     */
    private RatingBar mProductRbReviewsRank;
    /**
     * 评论人数
     */
    private TextView mReviewsNumTv;
    /**
     * 商品原价
     */
    private TextView mProductTvPriceOld;
    /**
     * 商品折扣价
     */
    private TextView mProductTvPriceNew;
    /**
     * 折扣价标签
     */
    private TextView mProductTvPriceOff;

    /**
     * 礼品信息
     */
    private RelativeLayout mProductRlGiftMessage;
    /**
     * 礼品
     */
    private LinearLayout mProductLlGiftDetail;
    /**
     * 查看更多礼品
     */
    private CheckBox mProductCbGiftMore;
    /**
     * 配送费
     */
    private TextView mProductTvShippingFee;
    /**
     * 配送国家
     */
    private TextView mProductTvShippingCountry;
    /**
     * 参数选择查看更多
     */
    private RelativeLayout mProductRlParamsMore;
    /**
     * 评论信息查看更多
     */
    private RelativeLayout mProductRlReviewMore;
    /**
     * 评论标题
     */
    private TextView mReviewsTileTv;


    /**
     * 详情描述查看更多
     */
    private RelativeLayout mProductRlDescriptionMore;
    /**
     * 商品详情描述
     */
    private LinearLayout mProductLlDescription;

    /**
     * 用户评论
     */
    private ListView mReviewsLv;
    /**
     * 用户评论为空时显示
     */
    private TextView mListViewEmptyTv;
    /**
     * 用户推荐
     */
    private RecyclerView mProductRvCustomerLove;
    /**
     * 添加到购物车
     */
    private Button mProductBtAddCart;
    /**
     * 立即购买
     */
    private Button mProductBtBuyNow;
    /**
     * 下架提示
     */
    private TextView mProductTvSold;
    /**
     * 商品信息
     */
    private ProductDetail mDetail;
    /**
     * 用户推荐适配器
     */
    private CustomerLoveAdapter mCustomerLoveAdapter;
    /**
     * 参数弹窗
     */
    private ParamsDialog mParamsDialog;
    /**
     * 分享弹窗
     */
    private ProductShareDialog mProductShareDialog;
    /**
     * facebook 回调
     */
    private CallbackManager mCallbackManager;
    /**
     * facebook分享弹窗
     */
    private ShareDialog mShareDialog;
    /**
     * sku 类型对应的位置
     */
    private int mTypeId = -1;
    /**
     * 已选参数map表（key:参数名称，value:参数值）
     */
    private HashMap<String, String> mParamsMap;
    /**
     * 参数类型名称列表
     */
    private ArrayList<String> mTypeName;
    /**
     * 地址信息
     */
    private ShippingAddrBean mShippingAddrBean;

    private int[] location = new int[2];
    private int[] textLocation = new int[2];

    //设置初始化加载布局
    @Override
    public void setContentView() {
        setContentView(R.layout.loading_multiple_waiting);
    }

    @Override
    protected String setWindowTitle() {
        return getString(R.string.product_detail_title);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mId = getIntent().getStringExtra("pId");
        controller.registIsAddWishListListener(this);
        controller.getServiceManager().getProductService().getProductDetail(mId, this);
        // 统计进入详情
        MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_enter_shopping_cart), ConstantsUtils.mVersionAnalystics);
    }

    @Override
    public void initDataView() {
        mProductCtsvContent = (CoveredTitleScrollView) bindView(R.id.product_ctsv_content);
        mProductVpDetailImage = (SliderLayout) bindView(R.id.product_vp_detail_image);
        // 顶部图片展示设置以屏幕宽度为长度的正方形区域
        mProductVpDetailImage.setLayoutParams(new RelativeLayout.LayoutParams(ConstantsUtils.DISPLAYW, ConstantsUtils.DISPLAYW));

        // 添加整体滑动区域滚动事件的回调监听
        mProductCtsvContent.setOnScrolledListener(new CoveredTitleScrollView.onScrolledListener() {
            @Override
            public void scrolled(int pT, int pScrollY) {
                // 当滑动距离小于或者等于顶部图片的高度的时候，设置顶部图片的回滚或者固定（即ScrollView滚动了多远的距离，ViewPager 回退多远的距离，固定在顶部位置），距离根据滑动坐标的绝对值判断
                if ((pScrollY >= 0 && pScrollY <= ConstantsUtils.DISPLAYW) || (pScrollY <= 0 && -pScrollY <= ConstantsUtils.DISPLAYW)) {
                    // 设置可滑动控件ViewPager滚动到指定位置，横坐标为当前滑动区域的坐标，纵坐标为反滑动距离
                    mProductVpDetailImage.setScrollY(-pScrollY);
                }
            }
        });
        mProductCbAddWish = bindView(R.id.product_cb_add_wish);
        mProductIvShareFriend = bindView(R.id.product_iv_share_friend);
        mProductIvShareFriend.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mProductIvShareFriend.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, ConstantsUtils.DISPLAYW - mProductIvShareFriend.getMeasuredHeight() / 2, layoutParams.rightMargin, layoutParams.bottomMargin);
                mProductIvShareFriend.setLayoutParams(layoutParams);
                mProductCtsvContent.scrollTo(0, 0);
            }
        });

        mProductTvTitleName = bindView(R.id.product_tv_title_name);

        mProductTvPriceOld = bindView(R.id.product_tv_price_old);
        mProductTvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        mProductTvPriceNew = bindView(R.id.product_tv_price_new);
        mProductTvPriceOff = bindView(R.id.product_tv_price_off);

        mReviewsNumTv = bindView(R.id.tv_rating);
        mProductRbReviewsRank = bindView(R.id.rb_rating);

        mProductRlGiftMessage = bindView(R.id.product_rl_gift_message);
        mProductLlGiftDetail = bindView(R.id.product_ll_gift_detail);
        mProductCbGiftMore = bindView(R.id.product_cb_gift_more);

        mProductRlParamsMore = bindView(R.id.product_rl_params_more);
        mProductTvShippingFee = bindView(R.id.product_tv_shipping_fee);
        mProductTvShippingCountry = bindView(R.id.product_tv_shipping_country);

        mProductRlReviewMore = bindView(R.id.product_rl_review_more);
        mReviewsTileTv = bindView(R.id.product_tv_reviews);
        mReviewsLv = bindView(R.id.lv_reviews);
        mListViewEmptyTv = bindView(R.id.tv_empty);


        mProductRlDescriptionMore = bindView(R.id.product_rl_description_more);
        mProductLlDescription = bindView(R.id.product_ll_description);
        mProductRvCustomerLove = bindView(R.id.product_rv_customer_love);

        // 设置用户推荐的宽高
        // 设置用户推荐的数据填充模式
        WrapHeightGridLayoutManager vGridLayoutManager = new WrapHeightGridLayoutManager(this, 2);
        mProductRvCustomerLove.setLayoutManager(vGridLayoutManager);
        mProductRvCustomerLove.setNestedScrollingEnabled(false);
        int left = UIUtils.dip2px(8);
        int right = UIUtils.dip2px(4);
        mProductRvCustomerLove.addItemDecoration(new CustomLoveDecoration(left, right, left));

        mProductBtAddCart = bindView(R.id.product_bt_add_cart);
        mProductBtBuyNow = bindView(R.id.product_bt_buy_now);
        mProductTvSold = bindView(R.id.product_tv_sold);

        findViewById(R.id.commontitle_tv_cartnum).post(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.commontitle_tv_cartnum).getLocationInWindow(textLocation);
            }
        });

        mReviewsLv.setEmptyView(mListViewEmptyTv);

    }

    /**
     * 填充页面数据
     */
    @Override
    public void addData() {
        setImagePageShow();
        mProductTvTitleName.setText(mDetail.getName());
        setPriceShow(mDetail.getPrice(), mDetail.getDiscountPrice());
        setPriceTag();
        setGift();
        setShippingMessage();
        setComment();
        setDescriptionDetail();
        setCustomerLove();
        setParamsDialog();
        initShare();
        mProductVpDetailImage.post(new Runnable() {
            @Override
            public void run() {
                // 开启自动轮播
                mProductVpDetailImage.startAutoCycle();
            }
        });
        initClick();
    }

    /**
     * 设置图片底部数量显示指示器 （RadioGroup 内 RadiosButton 的个数 + ViewPager 滑动时 RadioGroup 选中子项）
     */
    private void setImagePageShow() {
        int size = mDetail.getImage() == null ? 0 : mDetail.getImage().size();
        // 设置页卡指示器
        mProductVpDetailImage.setConfigs(IruluApplication.getInstance().getPicasso(), BaseSliderView.ScaleType.CenterCrop, R.mipmap.notify_image_xiaotu, R.mipmap.notify_image_xiaotu, true, this);
        List<String> urls = new ArrayList<String>();

        // 动态循环添加子项
        for (int i = 0; i < size; i++) {
            urls.add(StringUtils.getFullImageUrlFormat(mDetail.getImage().get(i)));
        }

        mProductVpDetailImage.setUrls(urls);
        mProductVpDetailImage.completed();
        // 设置页卡切换时间
        mProductVpDetailImage.setDuration(6000);
        // 设置页卡切换动画
        mProductVpDetailImage.setPresetTransformer(SliderLayout.Transformer.Default);
        // 轮播说明文本框动画效果
        mProductVpDetailImage.setCustomAnimation(new DescriptionAnimation());
        // 设置页卡指示器
        mProductVpDetailImage.setCustomIndicator((PagerIndicator) findViewById(R.id.product_rg_image_mark));
        if (size > 0) {
            mProductVpDetailImage.setCurrentPosition(0);
        }
    }

    /**
     * 设置价格显示方式（折扣价是否需要隐藏）
     *
     * @param oldPrice 原价
     * @param newPrice 折扣价
     */
    private void setPriceShow(float oldPrice, float newPrice) {
        // 当折扣价小于原价的时候，显示原价
        if (newPrice < oldPrice) {
            mProductTvPriceOld.setText(StringUtils.getPriceByFormat(oldPrice));
            mProductTvPriceOld.setVisibility(View.VISIBLE);
        } else {
            mProductTvPriceOld.setVisibility(View.GONE);
        }
        mProductTvPriceNew.setText(StringUtils.getPriceByFormat(newPrice));
    }

    /**
     * 设置价格标签
     */
    private void setPriceTag() {
        // 折扣价标签设置
        if (TextUtils.isEmpty(mDetail.getPercentTag())) {
            mProductTvPriceOff.setVisibility(View.GONE);
        } else {
            if (mDetail.getPercentTag().equals("0%")) {
                mProductTvPriceOld.setVisibility(View.GONE);
                mProductTvPriceOff.setVisibility(View.GONE);
            } else {
                mProductTvPriceOff.setVisibility(View.VISIBLE);
                mProductTvPriceOff.setText(" " + mDetail.getPercentTag() + " OFF ");
            }
        }
    }

    /**
     * 设置礼品显示
     */
    private void setGift() {
        // 存在礼品赠送
        if (mDetail.getGiftInfo() != null && mDetail.getGiftInfo().size() > 0) {
            // 清空子视图
            mProductLlGiftDetail.removeAllViews();
            mProductRlGiftMessage.setVisibility(View.VISIBLE);
            int size = mDetail.getGiftInfo().size();
            View view = null;
            TextView giftTvName;
            TextView giftTvNum;
            // 动态添加礼品显示
            for (int i = 0; i < size; i++) {
                //第i个条目的礼品信息
                final GiftInfo info = mDetail.getGiftInfo().get(i);
                view = getLayoutInflater().inflate(R.layout.view_product_gift_detail, null);
                giftTvName = (TextView) view.findViewById(R.id.gift_tv_name);
                giftTvNum = (TextView) view.findViewById(R.id.gift_tv_num);
                giftTvName.setText(info.getName());
                giftTvNum.setText("x " + String.valueOf(info.getNum()));
                // 设置礼品的点击事件，确保当前礼品和当前商品不一致跳转（singTask模式下的重新启动）
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startOnNewIntentProductDetailActivity(String.valueOf(info.getProductId()));// singTask模式的重启
                    }
                });
                if (i > 0) {
                    view.setVisibility(View.GONE);
                }
                mProductLlGiftDetail.addView(view);
            }
            // 设置礼物信息显示一条
            mProductCbGiftMore.setChecked(false);
            // 当礼品数为1的时候隐藏查看更多礼品信息
            if (size <= 1) {
                mProductCbGiftMore.setVisibility(View.GONE);
            } else {
                mProductCbGiftMore.setVisibility(View.VISIBLE);
                mProductCbGiftMore.setOnClickListener(this);
            }
        } else {
            // 没有礼品
            mProductRlGiftMessage.setVisibility(View.GONE);
        }
    }

    /**
     * 设置配送信息
     */
    private void setShippingMessage() {
        /** 设置配送费*/
        // 获取配送国家列表长度
        int size = mDetail.getShippingInfo() == null ? 0 : mDetail.getShippingInfo().size();
        String priceSymbol = CacheUtils.getString(IruluApplication.getInstance(), "priceSymbol", "$") + " ";
        String fee = "Free";
        if (size > 0) {
            float price = 0;
            try {
                price = Float.parseFloat(mDetail.getShippingInfo().get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            fee = price > 0 ? priceSymbol + mDetail.getShippingInfo().get(0) : fee;
        } else {
            //获得格式化价格
            fee = mDetail.getDeliveryFee() > 0 ? StringUtils.getPriceByFormat(mDetail.getDeliveryFee()) : fee;
            return;
        }
        // 获得多样化文本
        SpannableString develiveryFee = new SpannableString(fee + " shipping");
        //设置蓝色价格
        develiveryFee.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_line_image)), 0, fee.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mProductTvShippingFee.setText(develiveryFee);
        /** 设置配送国家*/
        String country = "";
        // 多样化文本初始化
        mProductTvShippingCountry.setText("");
        // 动态添加国家列表
        SpannableString develiveryCountry = null;
        country = mDetail.getShippingInfo().get(1);
        // 获得多样化文本信息
        develiveryCountry = new SpannableString("To " + country);
        // 设置 To 文本标签颜色
        develiveryCountry.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.price_product_gray)), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // 设置最后一行不换行
        mProductTvShippingCountry.append(develiveryCountry);

    }

    /**
     * 设置评论信息
     */
    public void setComment() {
        // 设置评论星级
        mProductRbReviewsRank.setRating(mDetail.getStar());
        // 设置评论数
        mReviewsNumTv.setText("(" + String.valueOf(mDetail.getCommentNum()) + ")");
        mReviewsTileTv.setText(getResources().getString(R.string.customer_reviews, String.valueOf(mDetail.getCommentNum())));
        CommentListAdapter vCommentListAdapter = new CommentListAdapter(mDetail.getCommentList(), this);
        mReviewsLv.setAdapter(vCommentListAdapter);
    }

    /**
     * 设置详细描述参数
     */
    public void setDescriptionDetail() {
        // 设置参数选项是否显示
        if (mDetail.getStandard() != null && mDetail.getStandard().size() > 0) {
            // 清空子视图
            mProductLlDescription.removeAllViews();
            mProductLlDescription.setVisibility(View.VISIBLE);
            int size = mDetail.getStandard().size();
            View view = null;
            TextView mDescriptionTvName = null;
            TextView mDescriptionTvContent = null;
            Standard standard = null;
            // 动态添加规格参数
            for (int i = 0; i < size; i++) {
                // 获得参数对象
                standard = mDetail.getStandard().get(i);
                view = getLayoutInflater().inflate(R.layout.view_product_description_detail, null);
                mDescriptionTvName = (TextView) view.findViewById(R.id.description_tv_name);
                mDescriptionTvContent = (TextView) view.findViewById(R.id.description_tv_content);
                // 参数名称
                mDescriptionTvName.setText(standard.getName());
                // 参数值
                mDescriptionTvContent.setText(standard.getContent());
                mProductLlDescription.addView(view);
            }
            view = null;
            standard = null;
        } else {
            // 没有规格参数
            mProductLlDescription.setVisibility(View.GONE);
        }
    }

    /**
     * 设置用户推荐的数据
     */
    private void setCustomerLove() {
        mCustomerLoveAdapter = new CustomerLoveAdapter(this, mDetail.getMaybeLike());
        mProductRvCustomerLove.setAdapter(mCustomerLoveAdapter);

    }

    /**
     * 设置参数弹窗，初始化参数选择条件信息
     */
    private void setParamsDialog() {
        mParamsMap = new HashMap<String, String>();
        // 获得参数列表
        ArrayList<TypeName> typeNames = mDetail.getTypeName();
        mTypeName = new ArrayList<String>();
        int size = typeNames == null ? 0 : typeNames.size();
        // 初始化已选参数队列，未选择全部置空，初始化全部参数队列
        for (int i = 0; i < size; i++) {
            TypeName type = typeNames.get(i);
            mParamsMap.put(type.getName(), null);
            mTypeName.add(type.getName());
        }
        mParamsDialog = new ParamsDialog(this);
        mParamsDialog.setListener(this, mDetail, mTypeName, this);
    }

    /**
     * 初始化分享
     */
    private void initShare() {
        if (mDetail.isWishList() == 1) {
            mProductCbAddWish.setChecked(true);
        } else {
            mProductCbAddWish.setChecked(false);
        }
        if (mDetail.getStatus() == 1) {
            mProductCbAddWish.setClickable(true);
            mProductCbAddWish.setOnClickListener(this);
        } else {
            mProductCbAddWish.setClickable(false);
        }
        mProductShareDialog = new ProductShareDialog(this) {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mProductShareDialog.dismiss();
                // radiogroup一共有三个子控件,中间的是分割线
                if (checkedId == group.getChildAt(0).getId()) {
                    ((RadioButton) group.getChildAt(0)).setChecked(false);
                    shareFacebook();
                } else if (checkedId == group.getChildAt(2).getId()) {
                    ((RadioButton) group.getChildAt(2)).setChecked(false);
                    shareTwitter();
                }
            }
        };
        initFacebook();
        initTwitter();
    }

    /**
     * 初始化Facebook分享
     */
    private void initFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    /**
     * 初始化Twitter
     */
    private void initTwitter() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterUtils.TWITTER_API_KEY, TwitterUtils.TWITTER_API_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
    }

    /**
     * 初始化点击事件
     */
    public void initClick() {
        // 下架商品操作
        if (mDetail.getStatus() == 1) {
            mProductBtAddCart.setEnabled(true);
            mProductBtBuyNow.setEnabled(true);
            mProductBtAddCart.setOnClickListener(this);
            mProductBtBuyNow.setOnClickListener(this);
            mProductTvSold.setVisibility(View.GONE);
        } else {
            mProductBtAddCart.setEnabled(false);
            mProductBtBuyNow.setEnabled(false);
            mProductTvSold.setVisibility(View.VISIBLE);
        }
        // 详细参数选择
        ImageView ivParamsMore = bindView(R.id.product_iv_params_more);
        if (mDetail.getTypeName() != null && mDetail.getTypeName().size() > 0) {
            mProductRlParamsMore.setClickable(true);
            mProductRlParamsMore.setOnClickListener(this);
            ivParamsMore.setVisibility(View.VISIBLE);
            ivParamsMore.setOnClickListener(this);
        } else {
            mProductRlParamsMore.setClickable(false);
            ivParamsMore.setVisibility(View.INVISIBLE);
        }
        // 更多评论
        ImageView ivReviewsMore = bindView(R.id.product_iv_reviews_more);
        mProductRlReviewMore.setClickable(true);
        mProductRlReviewMore.setOnClickListener(this);
        ivReviewsMore.setOnClickListener(this);
        // 详细描述
        bindView(R.id.product_iv_descriptions_more).setOnClickListener(this);
        mProductRlDescriptionMore.setOnClickListener(this);
        if (mDetail.getStatus() == 1) {
            mProductIvShareFriend.setClickable(true);
            mProductIvShareFriend.setOnClickListener(this);
        } else {
            mProductIvShareFriend.setClickable(false);
        }

        //定位点击位置
        mProductBtAddCart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mProductBtAddCart.getLocationInWindow(location);
                    location = new int[]{location[0] + (int) event.getX(), location[1] + (int) event.getY()};
                }
                return false;
            }
        });
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case PRO_DETA:
                if (returnObj != null && returnObj instanceof ProductDetail) {
                    mDetail = (ProductDetail) returnObj;
                    if (mDetail != null) {
                        refreshDataView(R.layout.activity_product_detail);
                    }
                }
                break;
            case ADD_WISH:
                WishList wishList = (WishList) returnObj;
                ArrayList<WishList> wishArrayList = wishList.getList();
                int wishListSize = wishArrayList.size();
                CacheUtils.setLong(this, "wishListNum", wishListSize);
                controller.postWishListCountCallback(wishListSize);
                mProductCbAddWish.setChecked(true);
                mDetail.setIsWishList(1);
                mProductCbAddWish.setClickable(true);
                controller.postIsAddWishListCallback(mId, "1");
                showToast(R.string.added_wish_list);
                break;
            case DEL_WISH:
                wishList = (WishList) returnObj;
                wishArrayList = wishList.getList();
                wishListSize = wishArrayList.size();
                CacheUtils.setLong(this, "wishListNum", wishListSize);
                controller.postWishListCountCallback(wishListSize);
                mProductCbAddWish.setChecked(false);
                mDetail.setIsWishList(0);
                mProductCbAddWish.setClickable(true);
                //更新首页的添加WishList图标的状态
                controller.postIsAddWishListCallback(mId, "0");//"0"代表删除，"1"代表添加
                showToast(R.string.delete_wish_list);
                break;
            case ADD_CART:
                mProductBtAddCart.setClickable(true);
                mParamsDialog.setOptionsClickable(true, ParamsDialog.ACTION_ADD_CART);
                Integer count = (Integer) returnObj;
                controller.postShoppongCartCountCallback(count);
                CacheUtils.setLong(this, "cartNum", count);
                showToast(R.string.added_to_cart);
                break;
            case GET_ADDRESS:
                ShippingAddr shippingAddr = (ShippingAddr) returnObj;
                List<ShippingAddrBean> list = shippingAddr.getList();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    int isDefault = list.get(i).getIsDefault();
                    if (isDefault == 1) {//如果有默认地址,就设置地址
                        mShippingAddrBean = list.get(i);
                        break;
                    } else {//否则把第一个设置为地址
                        mShippingAddrBean = list.get(0);
                    }
                }
                String addressId = "";
                if (mShippingAddrBean != null && !TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    addressId = mShippingAddrBean.getId();
                }
                int skuId = mDetail.getType().get(mTypeId).getId();//Sku ID
                int quantity = mParamsDialog.getQuantity();//购买数量
                // 请求服务器立即购买
                controller.getServiceManager().getOrderService().buyNow(mId, String.valueOf(skuId), String.valueOf(quantity), "", addressId, "1", this);
                break;
            case PRODUCT_BUYNOW:
                mProductBtBuyNow.setClickable(true);
                mParamsDialog.setOptionsClickable(true, ParamsDialog.ACTION_BUY_NOW);
                PageLoadDialog.hideDialogForLoading();
                // 跳转订单确认页面
                CheckoutBean checkoutBean = (CheckoutBean) returnObj;
                Intent intent = new Intent(this, OrderConfirmationActivity.class);
                intent.putExtra("checkoutBean", checkoutBean);
                intent.putExtra("shippingAddrBean", mShippingAddrBean);
                intent.putExtra("TAG", getClass().getCanonicalName());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case PRO_DETA:
                if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                    showToast(getString(R.string.server_network_failed));
                } else {
                    showToast(returnObj.toString());
                }
                displayNoDataView(R.layout.no_data_multiple_title_page);

                break;
            case ADD_WISH:
                mProductCbAddWish.setChecked(false);
                mProductCbAddWish.setClickable(true);
                if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                    showToast(R.string.item_already_existed);
                } else {
                    showToast(returnObj.toString());
                }
                break;
            case DEL_WISH:
                mProductCbAddWish.setChecked(true);
                mProductCbAddWish.setClickable(true);
                if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                    showToast(R.string.server_network_failed);
                } else {
                    showToast(returnObj.toString());
                }
                break;
            case ADD_CART:

                mParamsDialog.setOptionsClickable(true, ParamsDialog.ACTION_ADD_CART);
                mProductBtAddCart.setClickable(true);
                if (returnObj != null && !TextUtils.isEmpty((String) returnObj)) {
                    showToast(returnObj.toString());
                } else {
                    showToast(getString(R.string.server_network_failed));
                }
                break;
            case GET_ADDRESS:
                mProductBtBuyNow.setClickable(true);
                PageLoadDialog.hideDialogForLoading();
                mParamsDialog.setOptionsClickable(true, ParamsDialog.ACTION_BUY_NOW);
                if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                    showToast(getString(R.string.server_network_failed));
                } else {
                    showToast(returnObj.toString());
                }
                break;
            case PRODUCT_BUYNOW:
                mProductBtBuyNow.setClickable(true);
                mParamsDialog.setOptionsClickable(true, ParamsDialog.ACTION_BUY_NOW);
                PageLoadDialog.hideDialogForLoading();
                if (returnObj == null || TextUtils.isEmpty((String) returnObj)) {
                    showToast(getString(R.string.server_network_failed));
                } else {
                    showToast(returnObj.toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 分享
            case R.id.product_iv_share_friend:
                CustomerAnalystic.analystic(CustomerEvent.num_of_product_detail_share, mLoger);
                mProductShareDialog.show();
                break;
            // 添加到wish
            case R.id.product_cb_add_wish:
                CustomerAnalystic.analystic(CustomerEvent.num_of_product_detail_add_to_wish, mLoger);
                addToWishList();
                break;
            // 选择参数
            case R.id.product_rl_params_more:
            case R.id.product_iv_params_more:
                mParamsDialog.show(ParamsDialog.ACTION_VARIETY);
                break;
            // 查看评论信息
            case R.id.product_rl_review_more:
            case R.id.product_iv_reviews_more:
                CustomerAnalystic.analystic(CustomerEvent.num_of_product_detail_reviews, mLoger);
                Product product = new Product();
                product.setId(mId);
                product.setName(mDetail.getName());
                product.setPrice(mDetail.getDiscountPrice());
                if (mDetail.getImage() != null && mDetail.getImage().size() > 0) {
                    product.setImage(mDetail.getImage().get(0));
                }
                product.setStatu(mDetail.getStatus());
                if (mDetail.getCommentNum() > 0) {
                    CustomerReviewsActivity.startCustomerRevicesActivity(this, product);
                } else {
                    if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
                        LoginActivity.enterLoginActivity(this, this.getClass().getCanonicalName());
                    } else {
                        MyReviewsActivity.startMyReviewsActivity(this, product, "");
                    }
                }
                break;
            // 查看详细描述,跳转详情页面
            case R.id.product_rl_description_more:
            case R.id.product_iv_descriptions_more:
                CustomerAnalystic.analystic(CustomerEvent.num_of_product_detail_description, mLoger);
                ProductDescriptionActivity.startProductDescriptionActivity(this, mDetail.getDescription());
                break;
            // 数量加
            case R.id.params_iv_add:
                mParamsDialog.setQuantity(true);
                break;
            // 数量减
            case R.id.params_iv_dec:
                mParamsDialog.setQuantity(false);
                break;
            // 添加到购物车
            case R.id.product_bt_add_cart:
                buyOrAddProduct(ParamsDialog.ACTION_ADD_CART);
                break;
            // 立即购买
            case R.id.product_bt_buy_now:
                buyOrAddProduct(ParamsDialog.ACTION_BUY_NOW);
                break;
            case R.id.params_continue:
                buyOrAddProduct(mParamsDialog.getAction());
                break;
            // 查看更多礼品
            case R.id.product_cb_gift_more:
                if (mProductCbGiftMore.isChecked()) {
                    for (int i = 1; i < mDetail.getGiftInfo().size(); i++) {
                        mProductLlGiftDetail.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                } else {
                    for (int i = 1; i < mDetail.getGiftInfo().size(); i++) {
                        mProductLlGiftDetail.getChildAt(i).setVisibility(View.GONE);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void buyOrAddProduct(int action) {
        switch (action) {
            case ParamsDialog.ACTION_ADD_CART: // 添加到购物车
                CustomerAnalystic.analystic(CustomerEvent.num_of_product_detail_add_2_cart, mLoger);
                addToCart();
                break;
            case ParamsDialog.ACTION_BUY_NOW:
                // 立即购买
                CustomerAnalystic.analystic(CustomerEvent.num_of_product_detail_buy_now, mLoger);
                buyNow();
                break;

        }

    }


    /**
     * 添加到心愿单
     */
    private void addToWishList() {
        if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
            mProductCbAddWish.setChecked(false);
            showToast(R.string.log_in_first);
            LoginActivity.enterLoginActivity(this, "");
            return;
        }
        mProductCbAddWish.setClickable(false);
        if (mDetail.isWishList() == 1) {
            mProductCbAddWish.setChecked(true);
            controller.getServiceManager().getShoppingService().deleteToWishList("", mId, this);
        } else {
            mProductCbAddWish.setChecked(false);
            controller.getServiceManager().getShoppingService().addToWishList(mId, this);
        }
    }

    /**
     * 添加到购物车
     */
    public void addToCart() {
        if (mParamsDialog.getQuantity() > 0 && mTypeId != -1) {
            mProductBtAddCart.setClickable(false);
            mParamsDialog.setOptionsClickable(false, ParamsDialog.ACTION_ADD_CART);
            controller.getServiceManager().getShoppingService().addToCart(mId, String.valueOf(mDetail.getType().get(mTypeId).getId()), mParamsDialog.getQuantity(), this);

            if (mParamsDialog != null && mParamsDialog.isShowing()) {
                mParamsDialog.dismiss();
            }
            flaytocart();
        } else {
            if (mParamsDialog.isShowing()) {
                showToast(R.string.please_choose_size_color);
            } else {
                mParamsDialog.show(ParamsDialog.ACTION_ADD_CART);
            }
        }
    }

    /**
     * 飞入购物车动画
     */
    private void flaytocart() {
        final ImageView imageView = new ImageView(ProductDetailActivity.this);

        String url = StringUtils.getFullImageUrlFormat(mDetail.getImage().get(0));
        if (mDetail.getType().get(mTypeId).getImage() != null && mDetail.getType().get(mTypeId).getImage().size() > 0) {
            url = StringUtils.getThumbnailImageUrlFormat(mDetail.getType().get(mTypeId).getImage().get(0), UIUtils.getSixthWidth());
        }
        IruluApplication.getInstance().getPicasso().load(url).placeholder(R.mipmap.nav_gwc_icon_yuan).error(R.mipmap.nav_gwc_icon_yuan).into(imageView);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.cartfly);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30, 30);
        layoutParams.leftMargin = location[0] - 12;
        layoutParams.topMargin = location[1] - 12 - 50;
        imageView.setLayoutParams(layoutParams);
        relativeLayout.addView(imageView);

        final Animation animation = new TranslateAnimation(0, textLocation[0] - location[0] + 12, 0, textLocation[1] - location[1] + 12 + 30);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.post(new Runnable() {
            @Override
            public void run() {

                imageView.startAnimation(animation);
            }
        });
    }

    /**
     * 立即购买
     */
    public void buyNow() {
        if (controller.getCacheManager().getLoginUser().getUid() == 0 || controller.getCacheManager().getLoginUser().getUid() == -1) {
            showToast(R.string.log_in_first);
            LoginActivity.enterLoginActivity(this, this.getClass().getCanonicalName());
        } else {
            // 对立即购买做安全判断
            if (mTypeId != -1 && mParamsDialog.getQuantity() > 0) {
                mProductBtBuyNow.setClickable(false);
                mParamsDialog.setOptionsClickable(false, ParamsDialog.ACTION_BUY_NOW);
                // 当地址不为空的时候,立即购买
                if (mShippingAddrBean != null && !TextUtils.isEmpty(mShippingAddrBean.getId())) {
                    int skuId = mDetail.getType().get(mTypeId).getId();//Sku ID
                    int quantity = mParamsDialog.getQuantity();//购买数量
                    // 请求服务器立即购买
                    controller.getServiceManager().getOrderService().buyNow(mId, String.valueOf(skuId), String.valueOf(quantity), "", mShippingAddrBean.getId(), "1", this);
                } else {
                    // 请求获取地址
                    controller.getServiceManager().getAasService().getAllAddress(this);
                }
                PageLoadDialog.showDialogForLoading(this, true, false);
            } else {
                if (mParamsDialog.isShowing()) {
                    showToast(R.string.please_choose_size_color);
                } else {
                    mParamsDialog.show(ParamsDialog.ACTION_BUY_NOW);
                }
            }
        }
    }

    /**
     * Facebook 分享
     */
    private void shareFacebook() {
        ShareInfo shareInfo = mDetail.getShareInfo();
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(shareInfo.getTitle())
                    .setContentDescription(shareInfo.getContent())
                    .setContentUrl(Uri.parse(shareInfo.getLink()))
                    .setImageUrl(Uri.parse(shareInfo.getIcon()))
                    .build();

            mShareDialog.show(this, linkContent);
        }
    }

    /**
     * Twitter 分享
     */
    private void shareTwitter() {
        ShareInfo shareInfo = mDetail.getShareInfo();
        try {
            URL url = new URL(shareInfo.getLink());
            TweetComposer.Builder builder = new TweetComposer.Builder(this)
                    .url(url)
                    .text(shareInfo.getContent())
                    .image(Uri.parse(shareInfo.getIcon()));
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeParams(String pKey, String pValue) {
        float newPrice = mDetail.getDiscountPrice();
        int stock = mDetail.getStock();
        String url = null;
        if (mDetail.getImage() != null && mDetail.getImage().size() > 0) {
            url = mDetail.getImage().get(0);
        }
        // 更新用户已选参数
        mParamsMap.put(pKey, pValue);
        // 参数列表选满，即用户可以操作商品的购买
        ArrayList<Type> types = mDetail.getType();
        // 获取sku的类型的长度
        int size = types.size();
        // 获取参数类型的个数
        int length = mDetail.getTypeName().size();
        // 如果参数未选择完（即已选参数列表中包含空参数值）
        if (mParamsMap.containsValue("") || mParamsMap.containsValue(null)) {
            // 当本次改变前的状态为已完全选择对应的SKU状态时，本次改变重置标题为默认标题
            if (mTypeId != -1) {
                mParamsDialog.setTitle(newPrice, stock, url);
                setProductOptions(stock);
            }
            // 否则重置参数位置为－1
            mTypeId = -1;
        } else {
            // 循环比对每一个sku是否匹配当前已选择的参数值
            for (int i = 0; i < size; i++) {
                // 获取当前的sku值
                Type type = types.get(i);
                // 设置是否匹配标志位
                boolean isChoosed = true;
                // 循环比对当前sku的单个条目项（即对应参数类型）是否匹配对应已选参数当前的条目项
                for (int j = 0; j < length; j++) {
                    // 获取sku参数详情列表
                    ArrayList<String> params = type.getType();
                    // 如果存在一个不匹配的信息则匹配失败，跳出当前匹配循环
                    if (!params.get(j).equals(mParamsMap.get(mTypeName.get(j)))) {
                        isChoosed = false;
                        break;
                    }
                }
                // 判断匹配是否成功
                if (isChoosed) {
                    // 成功，则当前sku的position已选择sku的position
                    mTypeId = i;
                    newPrice = type.getDiscountPrice();
                    stock = type.getStock();
                    if (type.getImage() != null && type.getImage().size() > 0) {
                        url = mDetail.getImage().get(0);
                    }
                    break;
                } else {
                    // 不成功，则重置sku的position为－1
                    mTypeId = -1;
                }
            }
            // 匹配不成功，找原因、漏洞
            if (mTypeId == -1) {
                newPrice = mDetail.getDiscountPrice();
                stock = mDetail.getStock();
                if (mDetail.getImage() != null && mDetail.getImage().size() > 0) {
                    url = mDetail.getImage().get(0);
                }
                ILog.e("ProductDetailActivity", " ----> Please pay attention,typeId is not equals the sku which you choose ----> ");
            } else {
                if (types.get(mTypeId).getImage() != null && types.get(mTypeId).getImage().size() > 0) {
                    url = types.get(mTypeId).getImage().get(0);
                }
            }
            mParamsDialog.setTitle(newPrice, stock, url);
            setProductOptions(stock);
        }
        mParamsDialog.setChoosed(mParamsMap, pKey);

    }

    /**
     * 设置是否可买
     *
     * @param stock
     */
    public void setProductOptions(int stock) {
        // 注意判断下架字段的wish同样不可操作
        if (stock > 0 && mDetail.getStatus() == 1) {
            mProductBtAddCart.setEnabled(true);
            mProductBtBuyNow.setEnabled(true);
        } else {
            mProductBtAddCart.setEnabled(false);
            mProductBtBuyNow.setEnabled(false);
        }
    }


    /**
     * 跳转 商品详情页面
     *
     * @param context
     * @param pId     参数(商品id)
     */
    public static void startProductDetailActivity(Context context, String pId) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("pId", pId);
        context.startActivity(intent);
    }

    /**
     * 重启模式
     *
     * @param pId 商品id
     */
    public void startOnNewIntentProductDetailActivity(String pId) {
        // 如果是同一个产品，则回滚到页面顶部
        if (pId.equals(mId)) {
            mProductCtsvContent.scrollTo(0, 0);
            return;
        }
        // 否则刷新页面
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("pId", pId);
        onNewIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 加载界面，刷新数据，重新设置标题栏
        mId = intent.getStringExtra("pId");
        mTypeId = -1;
        if (mParamsDialog != null && mParamsDialog.isShowing()) {
            mParamsDialog.cancel();
        }
        setContentView();
        setView();
        controller.getServiceManager().getProductService().getProductDetail(mId, this);
        // 统计进入详情
        MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_enter_product_detail), ConstantsUtils.mVersionAnalystics);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSliderClick(final int position) {
        ShowPhotosActivity.startShowPhotosActivity(this, mDetail.getImage(), "comment", position);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProductVpDetailImage != null) {
            mProductVpDetailImage.stopAutoCycle();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.unRegistIsAddWishListListener(this);
        if (mParamsDialog != null) {
            mParamsDialog.dismiss();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mProductVpDetailImage != null) {
            mProductVpDetailImage.startAutoCycle();
        }
    }

    @Override
    public void isAddWishList(String productId, String addWishList) {
        if (mDetail == null || mDetail.getMaybeLike() == null) {
            return;
        }
        ArrayList<MaybeLike> vMaybeLikes = mDetail.getMaybeLike();
        for (int i = 0; i < vMaybeLikes.size(); i++) {
            MaybeLike vMaybeLike = vMaybeLikes.get(i);
            if (vMaybeLike.getId() == Integer.valueOf(productId)) {
                vMaybeLike.setAddWishList(Integer.valueOf(addWishList));
                mProductRvCustomerLove.getAdapter().notifyDataSetChanged();
                break;
            }
        }

    }
}