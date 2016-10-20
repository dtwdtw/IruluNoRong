package com.wf.irulu.module.category.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.CategoryDataBean;
import com.wf.irulu.common.bean.CategoryDataSortBean;
import com.wf.irulu.common.bean.CategoryProductListBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshListView;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.category.adapter.CategoryPhoneAdapter;

import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/18.
 */
public class CategoryPhoneActivity extends CommonTitleBaseActivity implements ServiceListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    private PullToRefreshListView phone_lv;
    private RadioButton best_match_rb;
    private RadioButton price_rb;
    private RadioButton most_popular_rb;
    private int id;
    private ArrayList<CategoryDataSortBean> sortList;
    private CategoryDataSortBean cdsb;
    private int select = 2;
    private ImageView phone_price_iv;
    private ArrayList<CategoryProductListBean> bmList;
    private ArrayList<CategoryProductListBean> psList;
    private ArrayList<CategoryProductListBean> pjList;
    private ArrayList<CategoryProductListBean> mpList;
    private int load_more_ps = 1;
    private int load_more_pj = 1;
    private int load_more_bm = 1;
    private int load_more_mp = 1;
    private boolean fresh = false;
    private CategoryPhoneAdapter adapter;
    private boolean isLoadData = true;

    @Override
    protected String setWindowTitle() {        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        id = intent.getIntExtra("id", 0);
        if (TextUtils.isEmpty(name)) {
            return "Phone";
        }
        return name;


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
        isLoadData = true;
        controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, "default", -1, 1, this);
    }

    @Override
    public void initDataView() {
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
        best_match_rb = (RadioButton) bindView(R.id.category_phone_best_mactch_rb);
        price_rb = (RadioButton) bindView(R.id.category_phone_price_rb);
        most_popular_rb = (RadioButton) bindView(R.id.category_phone_most_popular_rb);
        phone_lv = (PullToRefreshListView) bindView(R.id.category_phone_lv);
        phone_price_iv = (ImageView) bindView(R.id.category_phone_price_pic_iv);
        best_match_rb.setChecked(true);
        best_match_rb.setOnClickListener(this);
        price_rb.setOnClickListener(this);
        most_popular_rb.setOnClickListener(this);
        phone_price_iv.setOnClickListener(this);
        phone_lv.setMode(PullToRefreshBase.Mode.BOTH);
        phone_lv.setOnRefreshListener(this);
    }

    @Override
    public void addData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_phone_best_mactch_rb:
                phone_price_iv.setImageResource(R.drawable.search_arrow_032x);
                select = 2;
                if (bmList != null) {
                    adapter = new CategoryPhoneAdapter(this, bmList);
                    phone_lv.setAdapter(adapter);
                }
                break;
            case R.id.category_phone_price_rb:
            case R.id.category_phone_price_pic_iv:
                /**
                 *  id 从上个页面传过来的 标记(phone) 1 页码  20 页面显示个数, price.getsort() price.标记 1 表示升序 (-1 表示降序 )
                 *  1(自定义标记用来区分是请求哪一种数据的 1 more match 2 price 3 most pipular)
                 */
                if (select == 2 || select == 3) {
                    phone_price_iv.setImageResource(R.drawable.search_arrow_022x);
                    select = -1;
                    if (psList != null) {
                        adapter = new CategoryPhoneAdapter(this, psList);
                        phone_lv.setAdapter(adapter);
                    } else {
                        if (sortList != null && sortList.size() > 1) {
                            cdsb = sortList.get(1);
                            controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, cdsb.getSort(), 1, 2, this);
                        } else {
                        }
                    }


                } else if (select == -1) {
                    phone_price_iv.setImageResource(R.drawable.search_arrow_012x);
                    select = 1;
                    if (pjList != null) {
                        adapter = new CategoryPhoneAdapter(this, pjList);
                        phone_lv.setAdapter(adapter);
                    } else {
                        if (sortList != null && sortList.size() > 1) {

                            cdsb = sortList.get(1);
                            controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, cdsb.getSort(), -1, 2, this);
                        }
                    }

                } else {
                    phone_price_iv.setImageResource(R.drawable.search_arrow_022x);
                    select = -1;
                    if (psList != null) {
                        adapter = new CategoryPhoneAdapter(this, psList);
                        phone_lv.setAdapter(adapter);
                    }
                }
                break;
            case R.id.category_phone_most_popular_rb:
                phone_price_iv.setImageResource(R.drawable.search_arrow_032x);
                select = 3;
                if (mpList != null) {
                    adapter = new CategoryPhoneAdapter(this, mpList);
                    phone_lv.setAdapter(adapter);
                    return;
                }

                if (sortList != null && sortList.size() > 2) {
                    cdsb = sortList.get(2);
                    controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, cdsb.getSort(), cdsb.getSort_type(), 3, this);
                }
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {


        ArrayList<Integer> ali = (ArrayList<Integer>) bandObj;

        if (ali == null || ali.size() < 2) {
            return;
        }
        if (isLoadData) {
            refreshDataView(R.layout.activity_category_phone);
            isLoadData = false;
        }
        //用来关闭加载更多的刷新头
        if (6 == ali.get(0)) {
            phone_lv.onRefreshComplete();
            return;
        }
        switch (action) {
            case PRODUCT_CATEGORY_LIST:
                CategoryDataBean cdb = (CategoryDataBean) returnObj;
                if (cdb.getList() == null || cdb.getList().size() == 0) {//用来避免加载更多时候因页码不对出现空数据
                    return;
                }
                sortList = cdb.getSort();
                ArrayList<CategoryProductListBean> l = cdb.getList().get(0).getList();

                switch (select) {
                    case 1://price 的降序
                        if (pjList == null || fresh) {
                            pjList = l;
                            adapter = new CategoryPhoneAdapter(this, pjList);
                            phone_lv.setAdapter(adapter);
                        } else {
                            if (ali.get(1) > 1 && !pjList.containsAll(l)) {
                                pjList.addAll(l);
                                adapter.notifyDataSetChanged();
                            }
                        }


                        break;
                    case -1://price 的升序
                        if (psList == null || fresh) {
                            psList = l;
                            adapter = new CategoryPhoneAdapter(this, psList);
                            phone_lv.setAdapter(adapter);

                        } else {
                            if (ali.get(1) != 1 && !psList.containsAll(l)) {
                                psList.addAll(l);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case 2://best match 最匹配
                        if (bmList == null || fresh) {
                            bmList = l;
                            adapter = new CategoryPhoneAdapter(this, bmList);
                            phone_lv.setAdapter(adapter);
                        } else {
                            if (ali.get(1) != 1 && !bmList.containsAll(l)) {
                                bmList.addAll(l);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        break;
                    case 3://most popular 最流行的
                        if (mpList == null || fresh) {
                            mpList = l;
                            adapter = new CategoryPhoneAdapter(this, mpList);
                            phone_lv.setAdapter(adapter);

                        } else {
                            if (ali.get(1) != 1 && !mpList.containsAll(l)) {
                                mpList.addAll(l);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    default:
                        break;
                }

                //加载更多的时候关闭刷新头
                if (load_more_mp > 1 || load_more_pj > 1 | load_more_bm > 1 || load_more_ps > 1) {
                    phone_lv.onRefreshComplete();
                }
                //下拉刷新时候关闭刷新头
                if (fresh) {
                    phone_lv.onRefreshComplete();
                    fresh = false;
                }
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if (isLoadData) {
            displayNoDataView(R.layout.no_data_multiple_title_page);
            isLoadData = false;
        }
        ArrayList<String> msgs = (ArrayList<String>) returnObj;
        //加载更多数据 关闭刷新头 不用
        if (msgs != null && msgs.size() > 2) {

            if ("6".equalsIgnoreCase(msgs.get(1))) {
                phone_lv.onRefreshComplete();
                return;
            }
            //加载更多的时候关闭刷新头
            if (load_more_mp > 1 || load_more_pj > 1 || load_more_bm > 1 || load_more_ps > 1) {
                phone_lv.onRefreshComplete();
            }
            if (Integer.parseInt(msgs.get(0)) > 1) {
                switch (select) {
                    case -1:
                        load_more_ps--;
                        break;
                    case 1:
                        load_more_pj--;
                        break;
                    case 2:
                        load_more_bm--;
                        break;
                    case 3:
                        load_more_mp--;
                        break;
                    default:
                        break;
                }
            }
            //下拉刷新时候关闭刷新头
            if (fresh) {
                phone_lv.onRefreshComplete();
                fresh = false;
            }
            if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
                showToast(R.string.no_network);
                return;
            }

            showToast(msgs.get(2));
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
    }

    /**
     * 下拉刷新的逻辑
     *
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

        fresh = true;
        if (cdsb != null) {
            switch (select) {
                case -1:
                    if (sortList != null && sortList.size() > 1) {
                        cdsb = sortList.get(1);
                        controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, cdsb.getSort(), 1, 2, this);
                    }
                    break;
                case 1:
                    if (sortList != null && sortList.size() > 1) {
                        cdsb = sortList.get(1);
                        controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, cdsb.getSort(), -1, 2, this);
                    }
                    break;
                case 2:
                    controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, "default", -1, 1, this);
                    break;
                case 3:
                    if (sortList != null && sortList.size() > 2) {
                        load_more_mp = 1;
                        cdsb = sortList.get(2);
                        controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, cdsb.getSort(), cdsb.getSort_type(), 3, this);
                    }
                    break;
            }
        } else {//进入页面未点击的逻辑
            controller.getServiceManager().getProductService().getCategoryList(id, 1, 20, "default", -1, 1, this);
        }
    }

    /**
     * 加载更多时候的逻辑
     *
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (cdsb != null) {
            switch (select) {
                case -1:
                    if (psList != null && psList.size() != 0 && psList.size() % 20 == 0) {
                        load_more_ps++;
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_ps, 20, cdsb.getSort(), 1, 2, this);
                    } else {
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 6, this);
                    }
                    break;
                case 1:
                    if (pjList != null && pjList.size() != 0 && pjList.size() % 20 == 0) {
                        load_more_pj++;
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_pj, 20, cdsb.getSort(), -1, 2, this);
                    } else {
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 6, this);
                    }
                    break;
                case 2:
                    if (bmList != null && bmList.size() != 0 && bmList.size() % 20 == 0) {
                        load_more_bm++;
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 1, this);
                    } else {
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 6, this);
                    }
                    break;
                case 3:
                    if (mpList != null && mpList.size() != 0 && mpList.size() % 20 == 0) {
                        load_more_mp++;
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_mp, 20, cdsb.getSort(), cdsb.getSort_type(), 3, this);
                    } else {
                        controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 6, this);
                    }
                    break;
            }
        } else {//进入页面未点击的逻辑
            if (bmList != null && bmList.size() != 0 && bmList.size() % 20 == 0) {
                load_more_bm++;
                controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 1, this);
            } else {
                controller.getServiceManager().getProductService().getCategoryList(id, load_more_bm, 20, "default", -1, 6, this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCommonTitleCartNum.setVisibility(View.GONE);
        commonTitleCart.setVisibility(View.GONE);
    }
}
