package com.wf.irulu.module.homenew1_3_0.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseFragment;
import com.wf.irulu.common.receiver.NetworkChangedReceive;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.HorizontalViewPager;
import com.wf.irulu.common.view.astuetz.PagerSlidingTabStrip;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshListView;
import com.wf.irulu.component.slidingmenu.SlidingMenu;
import com.wf.irulu.framework.BaseContentFragment;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.listener.NoInternetConnListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.logic.listener.ShoppingCartCountListener;
import com.wf.irulu.module.home.FragmentFactory;
import com.wf.irulu.module.homenew1_3_0.DiscoverFragment.DiscoverFragment1_3_0;
import com.wf.irulu.common.bean.HomeBean;
import com.wf.irulu.module.homenew1_3_0.HomeLoadingFragment.HomeLoadingFragment;
import com.wf.irulu.module.homenew1_3_0.HotSalesFragment.HotSalesFragment1_3_0;
import com.wf.irulu.module.product.activity.SearchActivity;
import com.wf.irulu.module.shoppingcart.activity.ShoppingCartEditActivity;


/**
 * Created by dtw on 16/4/21.
 */
public class  ContentFragment1_3_0 extends BaseFragment implements View.OnClickListener, ServiceListener, ShoppingCartCountListener, NoInternetConnListener, HomeDataListener {



    protected Button mButtonSide;
    protected ImageView mImageView;
    protected Button mButtonShoppingcart;
    protected Button mButtonFilter_btn;
    private TextView shoppingcart_num_txt;
    private PullToRefreshListView mListView;
    private RelativeLayout no_internet_rl;// 无网络连接状态布局
    private LinearLayout no_internet_ll;// 无网络连接状态差按钮
    private PagerSlidingTabStrip mTabStrip;
    private HorizontalViewPager mViewPager;
    private String[] mTitles;
    //    private ArrayList<HomeProduct> mHomeProductList;
    HomeBean homeBean;
    private MainFragmentPagerAdapter mMainFragmentPagerAdapter;

    HomePresenter homePresenter;
    Handler handler=new Handler();

    @Override
    public void noInternetConn(boolean b) {
        ILog.i("NetworkChangedReceive", "无网络连接-显示与隐藏");
        if (b) {
            ILog.i("NetworkChangedReceive", "无网络连接-显示");
            no_internet_rl.setVisibility(View.VISIBLE);// 显示无网络连接状态
        } else {
            ILog.i("NetworkChangedReceive", "无网络连接-隐藏");
            no_internet_rl.setVisibility(View.GONE);// 不显示无网络连接状态
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.side_btn:// 设置主页面的左上角的菜单按钮的点击事件
                // 点击时打开或是关闭菜单
                SlidingMenu menu = ((HomeActivity) getActivity()).getSlidingMenu();
                menu.toggle();// 里面有一个参数，默认为true表示带动画,false表示不带动画
                break;
            case R.id.filter_btn:// 设置主页面的右上角的菜单按钮的搜索点击事件
                MobclickAgent.onEvent(mActivity, "num_of_home_search");
                Intent intent = new Intent(mActivity, SearchActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.shoppingcart_btn:// 设置主页面的右上角的菜单按钮的购物车点击事件
                MobclickAgent.onEvent(mActivity, "num_of_home_cart");
                Intent intent1 = new Intent(mActivity, ShoppingCartEditActivity.class);
                mActivity.startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {

    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if (null != returnObj) {
            UIUtils.getToastShort(mActivity, returnObj.toString());
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    public void shoppongCartCount(int count) {
        if (count == 0 || mButtonShoppingcart.getVisibility() == View.GONE) {
            shoppingcart_num_txt.setVisibility(View.GONE);
        } else if (count > 99) {
            shoppingcart_num_txt.setVisibility(View.VISIBLE);
            shoppingcart_num_txt.setText("99+");
        } else {
            shoppingcart_num_txt.setVisibility(View.VISIBLE);
            shoppingcart_num_txt.setText(count + "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.activity_main);

        homePresenter = HomePresenter.getIntance();
        homePresenter.setHomeDataListener(this);
        homePresenter.pullHomeData();

        initView();
        initData();
        return mMainView;
    }

    private void initView() {
        mButtonSide = (Button) findViewById(R.id.side_btn);
        mImageView = (ImageView) findViewById(R.id.logo_img);
        shoppingcart_num_txt = (TextView) findViewById(R.id.shoppingcart_num_txt);
        mButtonShoppingcart = (Button) findViewById(R.id.shoppingcart_btn);
        mButtonFilter_btn = (Button) findViewById(R.id.filter_btn);
        no_internet_rl = (RelativeLayout) findViewById(R.id.no_internet_rl);// 无网络连接状态布局
        no_internet_ll = (LinearLayout) findViewById(R.id.no_internet_ll);// 无网络连接状态差按钮
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_tabs);
        mViewPager = (HorizontalViewPager) findViewById(R.id.main_pager);

        //设置指针的文本的颜色
        int normalColor = UIUtils.getColor(R.color.tab_text_normal);
        int selectedColor = UIUtils.getColor(R.color.tab_text_selected);
        mTabStrip.setTextColor(normalColor, selectedColor);
//        mTabStrip.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);//设置字体相关的属性
//        mTabStrip.setTabBackground(getResources().getColor(R.color.transparent));//取消点击Tab时的背景色，这里在xml中设置了

        // 设置主页面的左上角的菜单按钮的点击事件
        mButtonSide.setOnClickListener(this);

        // 设置主页面的右上角的菜单按钮的搜索点击事件
        mButtonFilter_btn.setOnClickListener(this);

        // 设置主页面的右上角的菜单按钮的购物车点击事件
        mButtonShoppingcart.setOnClickListener(this);

        // 注册购物车数量的监听器
        controller.registShoppongCartCountListenert(this);
        // 注册无网络连接状态的监听器
        controller.registNoInternetConnListener(this);
        // 网络检测广播注册
        NetworkChangedReceive.beginListenNetworkChange();
        // 无网络连接差的点击事件
        no_internet_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.postNoInternetConnCallback(false);
            }
        });
    }

    private void initData() {
//        controller.getServiceManager().getHomeService().getHomeData(this);

        //初始化Titlte数据
        mTitles = UIUtils.getStringArray(R.array.main_titles);
//        mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(getActivity().getSupportFragmentManager());
//        //1、给Viewpager设置adapter
//        mViewPager.setAdapter(mMainFragmentPagerAdapter);
//        //2、给tabstrip设置viewpager
//        mTabStrip.setViewPager(mViewPager);
//        setAdapter();
        /**Title标签的选着事件*/
        mTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onHomeBean(HomeBean homeBean) {
        this.homeBean=homeBean;
        handler.post(new Runnable() {
            @Override
            public void run() {
                setAdapter();
            }
        });
    }

    private void setAdapter(){
        if(mMainFragmentPagerAdapter == null){
            mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(getActivity().getSupportFragmentManager());
            mViewPager.setAdapter(mMainFragmentPagerAdapter);
            mTabStrip.setViewPager(mViewPager);
        }else {
            mMainFragmentPagerAdapter.notifyDataSetChanged();
            mTabStrip.notifyDataSetChanged();
        }
    }


    class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

        SparseArrayCompat<BaseContentFragment> mCaches = new SparseArrayCompat<BaseContentFragment>();

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseContentFragment fragment = null;
            BaseContentFragment baseContentFragment = mCaches.get(position);
            if (null == baseContentFragment) {
                if (null != homeBean) {

                    switch (position) {
                        case 0:// Special Offers
                            fragment = new DiscoverFragment1_3_0();
                            break;
                        case 1:// Most Popular
                            fragment = new HotSalesFragment1_3_0();
                            break;
                        default:
                            fragment = new HomeLoadingFragment();
                            break;
                    }
                    mCaches.put(position,fragment);
                }else{
                    fragment = FragmentFactory.getFragment("");
                }
            }else{
                fragment = baseContentFragment;
            }
            fragment.sendBundleData(homeBean);
            return fragment;
        }
        @Override
        public int getCount () {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
//            if (mHomeProductList.size() > 0) {
//                return POSITION_NONE;
//            }
            return super.getItemPosition(object);
        }

        /**
         * 获得Title文本(如果没有这个方法，就会报空指针)
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Discover";
                case 1:
                    return "Hot Sales";
            }
            return super.getPageTitle(position);
        }
    }
}
