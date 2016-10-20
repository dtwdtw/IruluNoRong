package com.wf.irulu.module.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.WishInfo;
import com.wf.irulu.common.bean.WishList;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenu;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuCreator;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuItem;
import com.wf.irulu.common.view.swipemenulistview.SwipeMenuListView;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.adapter.WishAdapter;

import java.util.ArrayList;

public class WishActivity extends CommonTitleBaseActivity implements ServiceListener,SwipeMenuListView.OnMenuItemClickListener {

    /** 可侧滑删除listview*/
    private SwipeMenuListView mWishSmlvList;
    /** wish列表*/
    private ArrayList<WishInfo> mWishList = null;
    /** wish列表适配器*/
    private WishAdapter mAdapter = null;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.wish_list);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.loading_multiple_waiting);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        controller.getServiceManager().getProductService().getWishList(this);
    }

    @Override
    public void initDataView() {
        mWishSmlvList = bindView(R.id.wish_smlv_list);
        // 找到空视图
        View emptyView = bindView(R.id.wish_view_empty);
        // 设置空视图按钮的点击事件
        bindView(R.id.wish_bt_empty_go_shopping).setOnClickListener(this);

        View headerView = View.inflate(this, R.layout.view_split_gray, null);
        mWishSmlvList.addHeaderView(headerView);

        // 设置空视图
        mWishSmlvList.setEmptyView(emptyView);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 创建删除菜单
                SwipeMenuItem delItem = new SwipeMenuItem(WishActivity.this);
                // 设置侧滑菜单背景
                delItem.setBackground(new ColorDrawable(getResources().getColor(R.color.wish_delete)));
                // 设置宽度
                delItem.setWidth(UIUtils.dip2px(70));
                // 设置图片
                delItem.setIcon(R.drawable.wish_delete_selector);
                // 设置标题
                delItem.setTitle(R.string.wish_delete);
                // 设置标题颜色
                delItem.setTitleColor(getResources().getColor(R.color.white));
                // 设置标题字体大小
                delItem.setTitleSize(12);
                //加入到侧滑菜单中
                menu.addMenuItem(delItem);
            }
        };
        // 添加菜单
        mWishSmlvList.setMenuCreator(creator);
        // 设置菜单的点击事件
        mWishSmlvList.setOnMenuItemClickListener(this);
    }

    @Override
    public void addData() {
        getWishList();
    }

    /**
     * 展示/刷新wish列表
     */
    public void getWishList(){
        // 第一次加载
        if (mAdapter == null) {
            mAdapter = new WishAdapter(WishActivity.this, mWishList);
            mWishSmlvList.setAdapter(mAdapter);
            mWishSmlvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //这里的position-1是因为ListView有个头
                    ProductDetailActivity.startProductDetailActivity(WishActivity.this,String.valueOf(mWishList.get(position - 1).getPid()));
                }
            });
        }else{
            // 刷新列表
            mAdapter.refresh(mWishList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // wish列表为空，跳转购物界面
            case R.id.wish_bt_empty_go_shopping:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action){
            case WISH_LIST:
                mWishList = (ArrayList<WishInfo>) returnObj;
                refreshDataView(R.layout.activity_wish);
                break;
            case DEL_WISH:

                WishList wishList = (WishList) returnObj;
                ArrayList<WishList> list = wishList.getList();
                int size = list.size();
                CacheUtils.setLong(this, "wishListNum", size);
                controller.postWishListCountCallback(size);

                String productId = bandObj.toString();
                int pid = Integer.parseInt(productId);
                WishInfo wishInfo = new WishInfo(pid);
                // 安全操作，判断是否包含当前删除的条目
                if (mWishList.contains(wishInfo)){
                    mWishList.remove(wishInfo);
                    mAdapter.refresh(mWishList);
                    showToast(R.string.delete_wish_list);
                }
                //更新首页的添加WishList图标的状态
                controller.postIsAddWishListCallback(productId, "0");//"0"代表删除，"1"代表添加
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action){
            case WISH_LIST:
                displayNoDataView(R.layout.no_data_multiple_title_page);
                break;
            case DEL_WISH:
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        switch (action){
            case WISH_LIST:
                break;
            case DEL_WISH:
                break;
            default:
                break;
        }
    }

    public static void startWishActivity(Activity pActivity){
        Intent intent = new Intent(pActivity,WishActivity.class);
        pActivity.startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        // 安全判断
        if (mWishList.size() > position) {
            WishInfo info = mWishList.get(position);
            // 删除
            controller.getServiceManager().getShoppingService().deleteToWishList("",String.valueOf(info.getPid()), this);
        }
        return false;
    }

    @Override
    public void onSingleClick(int position) {

    }
}
