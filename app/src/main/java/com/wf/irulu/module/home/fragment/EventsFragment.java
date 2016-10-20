package com.wf.irulu.module.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.wf.irulu.R;
import com.wf.irulu.common.bean.HomeProduct;
import com.wf.irulu.common.utils.CommonUtil;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.wf.irulu.common.view.pulltorefresh.PullToRefreshWebView;
import com.wf.irulu.framework.BaseContentFragment;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.home.activity.HomeWebViewActivity;
import com.wf.irulu.common.bean.HomeBean;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @描述: Events(活动)页面
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.home.fragment
 * @类名:EventsFragment
 * @作者: 左杰
 * @创建时间:2016/1/15 14:40
 */
public class EventsFragment extends BaseContentFragment implements OnRefreshListener<WebView>{
    private ArrayList<HomeProduct> mHomeProductList;
    private HomeBean homeBean;
    private int position;
    private WebView mWebView;
    private String mUrl;
    private ProgressBar mProgressBar;
    private PullToRefreshWebView mPullToRefreshWebView;

    @Override
    protected View init() {
        if (mWebView == null) {
            setContentView(R.layout.fragment_home_events_layout);
            initView();
            initData();
        }
        return mMainView;
    }

    @Override
    public void loadData(HomeBean homeBean) {
        this.homeBean = homeBean;
        this.position = position;
    }

    private void initView() {
        mPullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.events_wb);
        mWebView = mPullToRefreshWebView.getRefreshableView();
        mProgressBar = (ProgressBar) findViewById(R.id.web_pb_bar);
        mProgressBar.setMax(100);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        mPullToRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshWebView.setOnRefreshListener(this);
    }

    private void initData() {
        if (null != mHomeProductList) {
            HomeProduct homeProduct = mHomeProductList.get(position);
            String type = homeProduct.getType();
            if ("4".equals(type)) {
                mUrl = homeProduct.getUrl();
                skipSettings();
                if (CommonUtil.isNetworkAvailable()) {//有网络
                    mWebView.loadUrl(mUrl);
                } else {//无网络
                    setContentView(R.layout.fragment_no_net_conn_layout);
                    Button button = (Button) findViewById(R.id.no_data_bt_again);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setContentView(R.layout.fragment_home_events_layout);
                            initView();
                            initData();
                        }
                    });
                }
            }
        }
    }

    /**
     * WebView的内部跳转设置
     */
    private void skipSettings() {
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                HashMap<String, String> map = parseUrl(url);

                if (map != null) {
                    int catgoryType = Integer.parseInt(map.get("actionCategory"));
                    String content = map.get("content");
                    if (catgoryType == 1) {
                        Intent intent_classify = new Intent(mActivity, CategoryActivity.class);
                        startActivity(intent_classify);
                        return true;
                    } else if (catgoryType == 2) {
                        ProductDetailActivity.startProductDetailActivity(mActivity, content);
                        return true;
                    } else if (catgoryType == 4) {//跳转浏览器
                        try {
                            content = URLDecoder.decode(content, "UTF-8");
                            Intent intent_web = new Intent(Intent.ACTION_VIEW);
                            intent_web.setData(Uri.parse(content));
                            startActivity(intent_web);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else if (catgoryType == 3) {
                        HomeWebViewActivity.startProductDetailActivity(mActivity, content);
                        return true;
                    }
                }
                HomeWebViewActivity.startProductDetailActivity(mActivity, url);
//                return super.shouldOverrideUrlLoading(view, mUrl);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                // 内置跳转结束后停止死循环加载,shouldOverrideUrlLoading方法不再继续
                view.stopLoading();
                mPullToRefreshWebView.onRefreshComplete();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 配置HTTPS协议
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    /**
     * 解析URL
     *
     * @param url
     * @return 参数键值对
     */
    public HashMap<String, String> parseUrl(String url) {
        HashMap<String, String> map = new HashMap<String, String>();
        String body[] = url.split("\\?");
        if (body[0] == null || !body[0].startsWith(ConstantsUtils.URL_PROTOCOL)) {
            return null;
        }
        String[] split = body[1].split("&");
        for (int i = 0; i < split.length; i++) {
            String[] catgory = split[i].split("=");
            map.put(catgory[0], catgory[1]);
        }
        return map;
    }

    @Override
    public void onRefresh(PullToRefreshBase<WebView> refreshView) {
        initData();
    }
}
