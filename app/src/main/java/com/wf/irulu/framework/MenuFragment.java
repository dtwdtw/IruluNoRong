package com.wf.irulu.framework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseFragment;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.view.RoundImageView;
import com.wf.irulu.component.slidingmenu.SlidingMenu;
import com.wf.irulu.logic.listener.ShoppingCartCountListener;
import com.wf.irulu.logic.listener.TotalUnreadCountListener;
import com.wf.irulu.logic.listener.UnpaidOrderCountListener;
import com.wf.irulu.logic.listener.WishListCountListener;
import com.wf.irulu.module.aas.activity.LoginActivity;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.contact.activity.ContactUsActivity;
import com.wf.irulu.module.message.activity.MessageActivity;
import com.wf.irulu.module.mycoupon.activity.MyCouponActivity;
import com.wf.irulu.module.order.activity.OrdersActivity;
import com.wf.irulu.module.product.activity.WishActivity;
import com.wf.irulu.module.setting.activity.SettingActivity;
import com.wf.irulu.module.shoppingcart.activity.ShoppingCartEditActivity;
import com.wf.irulu.module.user.activity.ProfileActivity;

import java.io.File;

/**
 * @描述: 菜单栏页
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.framework
 * @类名:MenuFragment
 * @作者: 左杰
 * @创建时间:2015/10/24 13:32
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener, TotalUnreadCountListener, ShoppingCartCountListener, WishListCountListener, UnpaidOrderCountListener {

    private final String TAG = getClass().getCanonicalName();
    private RoundImageView headimage;
    private TextView nickname_tv;
    private TextView massage_unread_txt;
    private TextView shoppingcart_unread_txt;
    /**
     * WishList数量
     */
    private TextView mWishlistUnreadTxt;
    /**
     * 未付款订单数量
     */
    private TextView mMyorderUnreadTxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.library_slidingmenu_menu_layout);
        initView();
        return mMainView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        RelativeLayout head_layout = (RelativeLayout) findViewById(R.id.head_layout);
        RelativeLayout myorder_layout = (RelativeLayout) findViewById(R.id.myorder_layout);
        RelativeLayout mycoupon_layout = (RelativeLayout) findViewById(R.id.mycoupon_layout);
        RelativeLayout wishlist_layout = (RelativeLayout) findViewById(R.id.wishlist_layout);
        RelativeLayout shoppingcart_layout = (RelativeLayout) findViewById(R.id.shoppingcart_layout);
        RelativeLayout massage_layout = (RelativeLayout) findViewById(R.id.massage_layout);
        RelativeLayout customer_layout = (RelativeLayout) findViewById(R.id.customer_layout);
        RelativeLayout Friend_layout = (RelativeLayout) findViewById(R.id.friend_layout);
        RelativeLayout setting_layout = (RelativeLayout) findViewById(R.id.setting_layout);
        massage_unread_txt = (TextView) findViewById(R.id.massage_unread_txt);// 侧栏消息个数
        shoppingcart_unread_txt = (TextView) findViewById(R.id.shoppingcart_unread_txt);// 侧栏购物车个数
        mMyorderUnreadTxt = (TextView) findViewById(R.id.myorder_unread_txt);//未付款订单数量
        mWishlistUnreadTxt = (TextView) findViewById(R.id.wishlist_unread_txt);
        RelativeLayout category_layout = (RelativeLayout) findViewById(R.id.category_layout);

        headimage = (RoundImageView) findViewById(R.id.head_img);

        nickname_tv = (TextView) findViewById(R.id.name_txt);

        // 注册购物车数量广播
        // IntentFilter filter = new IntentFilter("com.wf.irulu.common.cart");
        // receiver = new CartAmountReceiver();
        // mActivity.registerReceiver(receiver, filter);

        // 注册融云获取所有未读消息数广播
        // IntentFilter rong_filter = new
        // IntentFilter("com.wf.irulu.rong.TotalUnreadCount");
        // RongTotalUnreadCountReceiver rong_receiver = new
        // RongTotalUnreadCountReceiver();
        // mActivity.registerReceiver(rong_receiver, rong_filter);

        // 初始化未读总数
//        if (RongIMClient.getInstance() != null) {
//            int totalUnreadCount = RongIMClient.getInstance().getTotalUnreadCount();// 未读消息总数
//            controller.postTotalUnreadCountCallback(totalUnreadCount);
//        }

        head_layout.setOnClickListener(this);
        category_layout.setOnClickListener(this);
        myorder_layout.setOnClickListener(this);
        mycoupon_layout.setOnClickListener(this);
        wishlist_layout.setOnClickListener(this);
        shoppingcart_layout.setOnClickListener(this);
        massage_layout.setOnClickListener(this);
        customer_layout.setOnClickListener(this);
        Friend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_invite_friends, ((HomeActivity) getActivity()).mLogger);
                if (!getActivity().isFinishing() && getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).shareFriend();
                }
            }
        });
        setting_layout.setOnClickListener(this);

        //注册购物车数量的监听器
        controller.registShoppongCartCountListenert(this);
        //注册融云消息数量的监听器
        controller.registTotalUnreadCountListener(this);
        //注册WishList数量的监听器
        controller.registWishListCountListener(this);
        //注册未付款订单数量的监听器
        controller.registUnpaidOrderCountListener(this);

        // 初始化购物车数量的显示
        long cartNum = CacheUtils.getLong(mActivity, "cartNum");
        controller.postShoppongCartCountCallback((int) cartNum);

        // 初始化WishList数量的显示
        long wishListNum = CacheUtils.getLong(mActivity, "wishListNum");
        controller.postWishListCountCallback((int) wishListNum);

        // 初始化未付款订单的数量的显示
        long unpaidOrderNum = CacheUtils.getLong(mActivity, "unpaidOrderNum");
        controller.postUnpaidOrderCountCallback((int) unpaidOrderNum);

    }

    /**
     * 收起菜单
     */
    public void closeMenu() {
        // 菜单需要收起
        HomeActivity mainActivity = (HomeActivity) mActivity;
        SlidingMenu menu = mainActivity.getSlidingMenu();
        menu.toggle();// 里面有一个参数，默认为true表示带动画,false表示不带动画
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        controller.mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeMenu();
            }
        }, 500);
        switch (v.getId()) {
            case R.id.head_layout:// 头像的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_profile, ((HomeActivity) getActivity()).mLogger);
                if (judgeToken()) {
                    LoginActivity.enterLoginActivity(mActivity, "profile");
                } else {
                    Intent profile = new Intent(mActivity, ProfileActivity.class);
                    startActivity(profile);
                }
                break;
            case R.id.category_layout:// 分类的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_shop_by_category, ((HomeActivity) getActivity()).mLogger);
                Intent category = new Intent(mActivity, CategoryActivity.class);
                startActivity(category);
                break;
            case R.id.myorder_layout:// 我的订单的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_my_order, ((HomeActivity) getActivity()).mLogger);
                if (judgeToken()) {
                    LoginActivity.enterLoginActivity(mActivity, "order");
                } else {
                    Intent intentOrder = new Intent(getContext(), OrdersActivity.class);
                    startActivity(intentOrder);
                }
                break;
            case R.id.mycoupon_layout:// 我的优惠券的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_my_coupon, ((HomeActivity) getActivity()).mLogger);
                if (judgeToken()) {
                    LoginActivity.enterLoginActivity(mActivity, "coupon");
                } else {
                    Intent intentCoupon = new Intent(getContext(), MyCouponActivity.class);
                    startActivity(intentCoupon);
                }
                break;
            case R.id.wishlist_layout:// 我的收藏 的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_wish_list, ((HomeActivity) getActivity()).mLogger);
                if (judgeToken()) {
                    LoginActivity.enterLoginActivity(mActivity, "wishlist");

                } else {
                    WishActivity.startWishActivity(mActivity);
                }
                break;
            case R.id.shoppingcart_layout:// 购物车的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_shopping_cart, ((HomeActivity) getActivity()).mLogger);
                Intent intentShoppingCart = new Intent(mActivity, ShoppingCartEditActivity.class);
                startActivity(intentShoppingCart);
                break;
            case R.id.massage_layout:// 消息通知的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_message, ((HomeActivity) getActivity()).mLogger);
                if (judgeToken()) {
                    LoginActivity.enterLoginActivity(mActivity, "massage");

                } else {
                    Intent intentMessage = new Intent(mActivity, MessageActivity.class);
                    startActivity(intentMessage);
                }
                break;
            case R.id.customer_layout:// 客户支持的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_contact_us, ((HomeActivity) getActivity()).mLogger);
                Intent intentContactUs = new Intent(mActivity, ContactUsActivity.class);
                startActivity(intentContactUs);
                break;
            case R.id.setting_layout:// 设置的点击事件
                CustomerAnalystic.analystic(CustomerEvent.num_of_menu_settings, ((HomeActivity) getActivity()).mLogger);
                Intent setting = new Intent(mActivity, SettingActivity.class);
                if (judgeToken()) {//未登录 或者token过期了
                    setting.putExtra("flags", false);
                } else {//正常登录
                    setting.putExtra("flags", true);
                }
                startActivity(setting);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        controller.unRegistShoppongCartCounListenert(this);
        controller.unRegistTotalUnreadCountListener(this);
        controller.unRegistWishListCountListener(this);
        controller.unRegistUnpaidOrderCountListener(this);
        super.onDestroy();
    }

    /**
     * 设置购物车数量的显示
     */
    private void setCartCount() {
        long cartNum = CacheUtils.getLong(mActivity, "cartNum");
        ILog.d(TAG, "购物车数量" + cartNum);
        if (cartNum == 0) {
            shoppingcart_unread_txt.setVisibility(View.GONE);
        } else if (cartNum > 99) {
            shoppingcart_unread_txt.setVisibility(View.VISIBLE);
            shoppingcart_unread_txt.setText("99+");
        } else {
            shoppingcart_unread_txt.setVisibility(View.VISIBLE);
            shoppingcart_unread_txt.setText(cartNum + "");
        }
    }

    @Override
    public void totalUnreadCount(int totalUnreadCount) {
        if (totalUnreadCount > 0) {
            massage_unread_txt.setVisibility(View.VISIBLE);
            massage_unread_txt.setText(String.valueOf(totalUnreadCount));
        } else if (totalUnreadCount > 99) {
            massage_unread_txt.setVisibility(View.VISIBLE);
            massage_unread_txt.setText("99+");
        } else {
            massage_unread_txt.setVisibility(View.GONE);
        }
    }

    @Override
    public void shoppongCartCount(int count) {
        if (count == 0) {
            shoppingcart_unread_txt.setVisibility(View.GONE);
        } else if (count > 99) {
            shoppingcart_unread_txt.setVisibility(View.VISIBLE);
            shoppingcart_unread_txt.setText("99+");
        } else {
            shoppingcart_unread_txt.setVisibility(View.VISIBLE);
            shoppingcart_unread_txt.setText(String.valueOf(count));
        }

    }

    /**
     * 初始化头像 和昵称
     */
    private void initHeadPic() {
        String headurl = CacheUtils.getString(mActivity, "head_url");
        String nheadurl = CacheUtils.getString(mActivity, "headjpg");
        if (CacheUtils.getLong(mActivity, "froms") == 5) {
            IruluApplication.getInstance().getPicasso().load(nheadurl).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(headimage);
        } else {
            if (!TextUtils.isEmpty(headurl)) {
                IruluApplication.getInstance().getPicasso().load(new File(headurl)).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(headimage);

            } else {
                if (!TextUtils.isEmpty(nheadurl)) {
                    String netHeadUrl = nheadurl + "?imageView2/4/w/" + ConstantsUtils.DISPLAYW + "/h/" + ConstantsUtils.DISPLAYW + "/format/jpg/interlace/1";
                    IruluApplication.getInstance().getPicasso().load(netHeadUrl).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(headimage);
                } else {
                    headimage.setImageResource(R.mipmap.headpic);
                }
            }
        }

        String email = CacheUtils.getString(mActivity, "email");
        String nickname = CacheUtils.getString(mActivity, "nickname");
        if (TextUtils.isEmpty(nickname)) {
            if (TextUtils.isEmpty(email)) {
                nickname_tv.setText("Sign in");
            } else {
                if (email.split("@").length < 1) {
                } else {
                    nickname_tv.setText(email.split("@")[0]);
                }
            }
        } else {
            nickname_tv.setText(nickname);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initHeadPic();
    }

    /**
     * 判断token 是否存在 是否过期
     *
     * @return true  过期或不存在   false 未过期
     */
    private boolean judgeToken() {
        String token = CacheUtils.getString(mActivity, "token", null);
        if (TextUtils.isEmpty(token)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void wishListCount(int count) {
        /*if (count == 0) {
            mWishlistUnreadTxt.setVisibility(View.GONE);
        } else if (count > 99) {
            mWishlistUnreadTxt.setVisibility(View.VISIBLE);
            mWishlistUnreadTxt.setText("99+");
        } else {
            mWishlistUnreadTxt.setVisibility(View.VISIBLE);
            mWishlistUnreadTxt.setText(String.valueOf(count));
        }*/
    }

    @Override
    public void UnpaidOrderCount(int count) {
        if (count == 0) {
            mMyorderUnreadTxt.setVisibility(View.GONE);
        } else if (count > 99) {
            mMyorderUnreadTxt.setVisibility(View.VISIBLE);
            mMyorderUnreadTxt.setText("99+");
        } else {
            mMyorderUnreadTxt.setVisibility(View.VISIBLE);
            mMyorderUnreadTxt.setText(String.valueOf(count));
        }
    }
}
