package com.wf.irulu.common.base;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.receiver.NetworkChangedReceive;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.CommonUtil;
import com.wf.irulu.common.utils.CustomerAnalystic;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.logic.listener.NoInternetConnListener;
import com.wf.irulu.logic.listener.ShoppingCartCountListener;
import com.wf.irulu.module.product.activity.SearchActivity;
import com.wf.irulu.module.shoppingcart.activity.ShoppingCartEditActivity;

/**
 * @描述: 携带标题的Activity
 * @项目名: iRULU
 * @包名: com.wf.irulu.common.base
 * @类名: CommonTitleBaseActivity
 * @作者: Yuki
 * @创建时间: 2015.10.21 11:19
 */
public abstract class CommonTitleBaseActivity extends BaseActivity implements ShoppingCartCountListener, NoInternetConnListener {

    /**
     * 购物车上面的数量
     */
    public TextView mCommonTitleCartNum = null;
    /**
     * 无网络连接状态布局
     */
    public RelativeLayout mNoInternetLayout = null;
    public ImageView commonTitleBack;
    public TextView commonTitleLeftText;
    public TextView commonTitleRightText;
    public Button commonTitleCart;
    public Button commonTitleSearch;
    public View lines;
    public RelativeLayout rl_title;

    private String currentPage;
    /**
     * 设置公用标题栏
     */
    @Override
    protected void setView() {
        super.setView();
        TextView commonTitleText = (TextView) findViewById(R.id.commontitle_tv_text);//Title中间的标题
        commonTitleBack = (ImageView) findViewById(R.id.commontitle_iv_back);//Title左边的返回键
        commonTitleLeftText = (TextView) findViewById(R.id.commontitle_tv_left_text);//Title左边的文本
        commonTitleRightText = (TextView) findViewById(R.id.commontitle_tv_right_text);//Title右边的文本

        commonTitleCart = (Button) findViewById(R.id.commontitle_bt_cart);//购物车
        mCommonTitleCartNum = (TextView) findViewById(R.id.commontitle_tv_cartnum);//Title购物车上面的数量
        commonTitleSearch = (Button) findViewById(R.id.commontitle_bt_search);//搜索
        ImageView commonLoadingIvAnim = (ImageView) findViewById(R.id.loading_iv_anim);//加载动画
        Button commonBtTryAgain = (Button) findViewById(R.id.no_data_bt_again);//加载动画
        mNoInternetLayout = (RelativeLayout) findViewById(R.id.no_internet_rl);// 无网络连接状态布局
        LinearLayout cancelNoInternet = (LinearLayout) findViewById(R.id.no_internet_ll);//无网络连接状态差按钮
        rl_title = (RelativeLayout) findViewById(R.id.commontitle_rl_bar);
        lines = findViewById(R.id.common_multiple_title_line);
        if (commonTitleText != null) {
            commonTitleText.setText(setWindowTitle());

        }
        // 右标题
        if (commonTitleRightText != null) {
            commonTitleRightText.setText(setRightText());
        }
        // 搜索
        if (commonTitleSearch != null) {
            commonTitleSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomerAnalystic.analystic(CustomerEvent.num_of_home_search,mLoger);
                    Intent intent = new Intent(CommonTitleBaseActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            });
        }
        // 购物车
        if (commonTitleCart != null) {
            //初始化购物车数量的显示
            if (mCommonTitleCartNum != null) {
                long cartNum = CacheUtils.getLong(this, "cartNum");
                shoppongCartCount((int) cartNum);
                //注册购物车数量的监听器
                controller.registShoppongCartCountListenert(this);
            }
            commonTitleCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomerAnalystic.analystic(CustomerEvent.num_of_home_shopping_cart,mLoger);
                    Intent intent = new Intent(CommonTitleBaseActivity.this, ShoppingCartEditActivity.class);
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
                    if (isFinish()) {
                        finish();
                    }
                }
            });
        }
        if (commonTitleLeftText != null) {
            commonTitleLeftText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        // loading动画效果
        if (commonLoadingIvAnim != null){
            AnimationDrawable animationDrawable = (AnimationDrawable) commonLoadingIvAnim.getBackground();
            animationDrawable.start();
        }
        // try again的按钮事件
        if (commonBtTryAgain != null) {
            commonBtTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtil.isNetworkAvailable()) {
                        setContentView();
                        setView();
                        initData();
                    }
                }
            });
        }
    }

    /**
     * 设置Title右边的文本，有，子类就复写这个方法
     *
     * @return
     */
    protected String setRightText() {
        return "";
    }

    /**
     * 设置标题
     *
     * @return
     */
    protected abstract String setWindowTitle();

    protected boolean isFinish() {
        return true;
    }

    @Override
    public void shoppongCartCount(int pCount) {
        if (mCommonTitleCartNum != null) {
            if (pCount == 0) {
                mCommonTitleCartNum.setVisibility(View.GONE);
            } else if (pCount > 99) {
                mCommonTitleCartNum.setVisibility(View.VISIBLE);
                mCommonTitleCartNum.setText("99+");
            } else {
                mCommonTitleCartNum.setVisibility(View.VISIBLE);
                mCommonTitleCartNum.setText(pCount + "");
            }
        }
    }

    @Override
    public void noInternetConn(boolean pIsNotConnect) {
        if (mNoInternetLayout != null) {
            if (pIsNotConnect) {
                mNoInternetLayout.setVisibility(View.VISIBLE);//显示无网络连接状态
            } else {
                mNoInternetLayout.setVisibility(View.GONE);//不显示无网络连接状态
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.unRegistShoppongCartCounListenert(this);
        controller.unRegistNoInternetConnListener(this);
        NetworkChangedReceive.finishListenNetworkChange();
    }

    /**
     * 展示无数据界面
     * @param viewId
     */
    public void displayNoDataView(int viewId){
        setContentView(viewId);
        TextView mNoDataView = (TextView) findViewById(R.id.no_data_tv_tip);
        if (mNoDataView != null){
            if(CommonUtil.isNetworkAvailable()){
                mNoDataView.setText(getString(R.string.data_wrong));
            }else{
                mNoDataView.setText(getString(R.string.connections__offline));
            }
        }
        setView();
    }

    /**
     * 刷新数据界面
     * @param viewId
     */
    public void refreshDataView(int viewId){
        setContentView(viewId);
        setView();
        ILog.d("TB","mNoInternetLayout===>"+mNoInternetLayout);
        ILog.d("TB","refreshDataView===>"+CommonUtil.isNetworkAvailable());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获按下返回键事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), currentPage);
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

        currentPage = setWindowTitle();

        if (null == currentPage || "".equals(currentPage)) {
            currentPage = setRightText();
        }

        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), currentPage);
    }
}
