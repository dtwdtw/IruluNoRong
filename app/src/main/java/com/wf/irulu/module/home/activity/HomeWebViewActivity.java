package com.wf.irulu.module.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @描述: 主页的第四种类型跳转的WebView
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.home.adapter
 * @类名:HomeWebViewActivity
 * @作者: 左杰
 * @创建时间:2016/1/15 15:51
 */
public class HomeWebViewActivity extends BaseActivity {

    private WebView mWebView;
    private String mUrl;
    private ImageView mTitleBack;
    private TextView mTitleCenterTxt;
    private ImageView mTitleCancel;
//    private ArrayList<String> loadHistoryUrls = new ArrayList<String>();
    private ArrayList<String> historyTitles = new ArrayList<String>();
    private ProgressBar mProgressBar;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home_webview_layout);
    }

    @Override
    public void initView() {
        mUrl = getIntent().getStringExtra("url");
//        mUrl = "https://m.taobao.com";//测试用
        mWebView = (WebView) findViewById(R.id.home_wb);
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleCenterTxt = (TextView) findViewById(R.id.title_center_txt);
        mTitleCancel = (ImageView) findViewById(R.id.title_cancel);
        mProgressBar = (ProgressBar) findViewById(R.id.web_pb_bar);
        mProgressBar.setMax(100);
        mTitleBack.setOnClickListener(this);
        mTitleCancel.setOnClickListener(this);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);

    }

    @Override
    public void initData() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                historyTitles.add(title);
                mTitleCenterTxt.setText(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                loadHistoryUrls.add(url);
                if (mWebView.canGoBack()) {
                    mTitleCancel.setVisibility(View.VISIBLE);
                } else {
                    mTitleCancel.setVisibility(View.GONE);
                }
                HashMap<String, String> map = parseUrl(url);

                if (map != null) {
                    int catgoryType = Integer.parseInt(map.get("actionCategory"));
                    String content = map.get("content");
                    if (catgoryType == 1) {
                        Intent intent_classify = new Intent(HomeWebViewActivity.this, CategoryActivity.class);
                        startActivity(intent_classify);
                        return true;
                    } else if (catgoryType == 2) {
                        ProductDetailActivity.startProductDetailActivity(HomeWebViewActivity.this, content);
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
                        mWebView.loadUrl(content);
                        return false;
                    }
                }
//                mWebView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 配置HTTPS协议
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        mWebView.loadUrl(mUrl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                if(mWebView.canGoBack()){
                    setTitleBack();
                    mWebView.goBack();
                }else {
                    finish();
                }
                break;
            case R.id.title_cancel:
                finish();
                break;
        }
    }

    public static void startProductDetailActivity(Activity activity,String url){
        Intent intent = new Intent(activity, HomeWebViewActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            setTitleBack();
            return true;
        }

        //捕获按下返回键事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "HomeWebViewActivity");
                ExitionUtil.getInstance().setEnable();
            } /** else {
             仍然将之前的页面设置为Exit Page
             } **/
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置返回后的Title
     */
    public void setTitleBack(){
        historyTitles.remove(historyTitles.size() - 1);
        mTitleCenterTxt.setText(historyTitles.get(historyTitles.size() - 1));
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
    protected void onResume() {
        super.onResume();

        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "HomeWebViewActivity");
    }
}
