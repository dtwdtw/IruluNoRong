package com.wf.irulu.module.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.view.ClearEditText;
import com.wf.irulu.logic.db.RecentSearchDbHelper;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.product.adapter.HotSearchAdapter;
import com.wf.irulu.module.product.adapter.RecentSearchAdapter;
import com.wf.irulu.module.product.adapter.SuggestSearchAdapter;
import com.wf.irulu.module.product.listener.SearchWordListener;
import com.wf.irulu.module.product.service.HotSearchSpaceDecoration;

import java.util.ArrayList;
/* 搜索界面，通过用户输入的关键字自动提示热门搜索，历史纪录*/
public class SearchActivity extends CommonTitleBaseActivity implements ServiceListener, TextWatcher ,SearchWordListener{

    /**
     * 搜索框
     */
    private ClearEditText mSearchCetWord;
    /**
     * 热门搜索
     */
    private RecyclerView mSearchRvHot;
    /**
     * 搜索关键字列表
     */
    private ListView mSearchLvSuggest;
    /**
     * 最近搜索
     */
    private ListView mSearchLvRecent;
    /**
     * 热门搜索适配器
     */
    private HotSearchAdapter mAdapter;
    /**
     * 热门搜索词语
     */
    private ArrayList<String> mHotSearch;
    /**
     * 最近搜索适配器
     */
    private RecentSearchAdapter mRecentSearchAdapter;
    /**
     * 最近搜索词语
     */
    private ArrayList<String> mRecentSearch;
    /**
     * 关键词猜想适配器
     */
    private SuggestSearchAdapter mSuggestSearchAdapter;
    /** 关键词猜想列表*/
    private ArrayList<String> mSuggestSearch;

    @Override
    protected String setWindowTitle() {
        return null;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initView() {
        mSearchCetWord = bindView(R.id.search_cet_keyword);
        mSearchLvSuggest = bindView(R.id.search_lv_suggest);
        mSearchLvRecent = bindView(R.id.search_lv_recent);
        bindView(R.id.search_tv_cancel).setOnClickListener(this);
        // 获取热门搜索布局
        View view = bindView(R.id.search_v_hot);
        mSearchRvHot = (RecyclerView) view.findViewById(R.id.search_rv_hot);
        // 设置热门搜索列表的方向及间距
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSearchRvHot.setLayoutManager(manager);
        mSearchRvHot.addItemDecoration(new HotSearchSpaceDecoration(15));
        // 将热门搜索布局设置成搜索关键词猜想列表的空布局
        mSearchLvSuggest.setEmptyView(view);
    }

    @Override
    public void initData() {
        // 获取热门搜索
        controller.getServiceManager().getProductService().getHotSearch(this);
        // 从数据库获取最近搜索列表
        mRecentSearch = new RecentSearchDbHelper(controller).queryRecentSearch();
        // 设置最近搜索的数据适配器
        mRecentSearchAdapter = new RecentSearchAdapter(mRecentSearch, this);
        mSearchLvRecent.setAdapter(mRecentSearchAdapter);
        // 设置edittext的动态输入监听器
        mSearchCetWord.addTextChangedListener(this);
        // 设置最近搜索的点击事件
        mSearchLvRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchForSelect(mRecentSearch.get(position));
            }
        });
        // 设置猜想关键词列表的点击事件
        mSearchLvSuggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchForSelect(mSuggestSearch.get(position));
            }
        });
        // 初始化参数
        mSuggestSearchAdapter = new SuggestSearchAdapter(mSuggestSearch,this);
        mSearchLvSuggest.setAdapter(mSuggestSearchAdapter);
        // 设置搜索按钮
        mSearchCetWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWrod = mSearchCetWord.getText().toString();
                    if (!TextUtils.isEmpty(keyWrod.trim())) {
//                        controller.getServiceManager().getProductService().getSuggestSearch(keyWrod,SearchActivity.this);
                        searchForSelect(keyWrod);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(R.string.search_hint);
                                mSearchCetWord.requestFocus();
                                mSearchCetWord.setFocusableInTouchMode(true);
                            }
                        });
                    }
                }
                return false;
            }
        });
        // 接触焦点的抢占事件
        mSearchLvSuggest.setFocusable(false);
        mSearchLvRecent.setFocusable(false);
        mSearchRvHot.setFocusable(false);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("searchWord"))){
            mSearchCetWord.setText(getIntent().getStringExtra("searchWord"));
            mSearchCetWord.setSelectAllOnFocus(false);
            mSearchCetWord.setCursorVisible(true);
            mSearchCetWord.setFocusable(true);
            mSearchCetWord.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_tv_cancel:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case HOT_SEARCH:
                mHotSearch = (ArrayList<String>) returnObj;
                mAdapter = new HotSearchAdapter(this, mHotSearch,this);
                mSearchRvHot.setAdapter(mAdapter);
                break;
            case SUGGEST_SEARCH:
                if (TextUtils.isEmpty(mSearchCetWord.getText().toString().trim())){
                    if (mSuggestSearch != null) {
                        mSuggestSearch.clear();
                    }
                    mSuggestSearchAdapter.refresh(mSuggestSearch);
                }else {
                    mSuggestSearch = (ArrayList<String>) returnObj;
                    mSuggestSearchAdapter.refresh(mSuggestSearch);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        switch (action) {
            case HOT_SEARCH:
                break;
            case SUGGEST_SEARCH:
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String word = intent.getStringExtra("key");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = mSearchCetWord.getText().toString();
        // 设置清除按钮是否可见（标准未输入内容，若输入空字符串则同样视为已输入内容）
        mSearchCetWord.setClearIconVisible(s.length() > 0);
        // 判断内容是否为空且有效
        if (TextUtils.isEmpty(content.trim())) {
            if (mSuggestSearch != null && mSuggestSearchAdapter != null){
                // 无效
                mSuggestSearch.clear();
                mSuggestSearchAdapter.refresh(mSuggestSearch);
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = s.toString();
        // 当搜索内容有效时进行关键词猜想
        if (!TextUtils.isEmpty(content.trim())){
            controller.getServiceManager().getProductService().getSuggestSearch(content,this);
        }else {
            if (mSuggestSearch != null && mSuggestSearchAdapter != null) {
                mSuggestSearch.clear();
                mSuggestSearchAdapter.notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public void searchForSelect(String word) {
//        // 更新最近搜索数据库
//        RecentSearchDbHelper dbHelper = new RecentSearchDbHelper(controller);
//        dbHelper.insertKeyWord(word);
//        // 获取最近搜索关键字列表
//        mRecentSearch = dbHelper.queryRecentSearch();
//        // 刷新最近搜索列表
//        mRecentSearchAdapter.refresh(mRecentSearch);
//        // 设置页面初始化
//        mSearchCetWord.setText(null);
//        if (mSuggestSearch != null) {
//            mSuggestSearch.clear();
//            mSuggestSearchAdapter.refresh(mSuggestSearch);
//        }
        // 跳转搜索结果页面
        SearchResultActivity.startSearchResultActivity(this, word);
        finish();
    }

    public static void startSearchActivity(Activity activity,String searchWord){
        Intent intent = new Intent(activity,SearchActivity.class);
        intent.putExtra("searchWord",searchWord);
        activity.startActivity(intent);
    }
}