package com.wf.irulu.common.base;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.receiver.NetworkChangedReceive;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.CommonUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.listener.NoInternetConnListener;
import com.wf.irulu.logic.listener.ShoppingCartCountListener;
import com.wf.irulu.module.product.activity.SearchActivity;
import com.wf.irulu.module.shoppingcart.activity.ShoppingCartEditActivity;

/**
 * @描述: 携带标题的FragmentActivity
 * @项目名: iRULU
 * @包名: com.wf.irulu.common.base
 * @类名: CommonTitleBaseFragmentActivity
 * @作者: Yuki
 * @创建时间: 2015.10.21 11:19
 * */
public abstract class CommonTitleBaseFragmentActivity extends BaseFragmentActivity implements ShoppingCartCountListener, NoInternetConnListener {

    /** 购物车上面的数量*/
    public TextView mCommonTitleCartnum = null;
    /** 无网络连接状态布局*/
    public RelativeLayout mNoInternetLayout = null;

    /**
     * 设置公用标题栏
     */
    @Override
    protected void setView() {
        super.setView();
        TextView commonTitleText = (TextView) findViewById(R.id.commontitle_tv_text);//标题
        ImageView commonTitleBack = (ImageView) findViewById(R.id.commontitle_iv_back);
        Button commonTitleCart = (Button) findViewById(R.id.commontitle_bt_cart);//购物车
        mCommonTitleCartnum = (TextView) findViewById(R.id.commontitle_tv_cartnum);
        Button commonTitleSearch = (Button) findViewById(R.id.commontitle_bt_search);//搜索
        mNoInternetLayout = (RelativeLayout) findViewById(R.id.no_internet_rl);// 无网络连接状态布局
        ImageView commonLoadingIvAnim = (ImageView) findViewById(R.id.loading_iv_anim);//加载动画
        LinearLayout cancelNoInternet = (LinearLayout) findViewById(R.id.no_internet_ll);//无网络连接状态差按钮
        commonTitleText.setText(setWindowTitle());

        if (commonLoadingIvAnim != null){
            AnimationDrawable animationDrawable = (AnimationDrawable) commonLoadingIvAnim.getBackground();
            animationDrawable.start();
        }
        // 搜索
        if (commonTitleSearch != null) {
            commonTitleSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(CommonTitleBaseFragmentActivity.this, SearchActivity.class);
                startActivity(intent);
                }
            });
        }
        // 购物车
        if (commonTitleCart != null) {
            //初始化购物车数量的显示
            if (mCommonTitleCartnum != null) {
                long cartNum = CacheUtils.getLong(this, "cartNum");
                shoppongCartCount((int) cartNum);
                //注册购物车数量的监听器
                controller.registShoppongCartCountListenert(this);
            }
            commonTitleCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(CommonTitleBaseFragmentActivity.this, ShoppingCartEditActivity.class);
                startActivity(intent);
                }
            });
        }
        // 网络连接
        if (cancelNoInternet != null) {
            //注册无网络连接状态的监听器
            controller.registNoInternetConnListener(this);
            //网络检测广播注册
            NetworkChangedReceive.beginListenNetworkChange();
            cancelNoInternet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.postNoInternetConnCallback(false);
                }
            });
        }
        // 返回键
        if (commonTitleBack != null) {
            commonTitleBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置标题
     * @return 标题
     */
    protected abstract String setWindowTitle();

    @Override
    public void shoppongCartCount(int pCount) {
        if (mCommonTitleCartnum != null) {
            if (pCount == 0) {
                mCommonTitleCartnum.setVisibility(View.GONE);
            } else if (pCount > 99) {
                mCommonTitleCartnum.setVisibility(View.VISIBLE);
                mCommonTitleCartnum.setText("99+");
            } else {
                mCommonTitleCartnum.setVisibility(View.VISIBLE);
                mCommonTitleCartnum.setText(pCount + "");
            }
        }
    }

    @Override
    public void noInternetConn(boolean pIsNotConnect) {
        if (mNoInternetLayout != null) {
            if (pIsNotConnect) {
                ILog.e("NetworkChangedReceive", "Fragment无网络连接-显示");
                mNoInternetLayout.setVisibility(View.VISIBLE);//显示无网络连接状态
            } else {
                ILog.e("NetworkChangedReceive", "Fragment无网络连接-隐藏");
                mNoInternetLayout.setVisibility(View.GONE);//不显示无网络连接状态
            }
        }
    }

    public <T extends View > T bindView(int pId){
        T view = (T) findViewById(pId);
        return view;
    }

    /**
     * 刷新数据界面
     * @param viewId
     */
    public void refreshDataView(View viewId){
        setContentView(viewId);
        setView();
        ILog.d("TB","mNoInternetLayout===>"+mNoInternetLayout);
        ILog.d("TB","refreshDataView===>"+ CommonUtil.isNetworkAvailable());
        if (CommonUtil.isNetworkAvailable()) {

            mNoInternetLayout.setVisibility(View.GONE);//不显示无网络连接状态
        }
        initDataView();
        addData();
    }
    /**
     * 初始化数据布局
     */

    public void initDataView(){}

    /**
     * 添加显示数据
     */
    public void addData(){}
}
