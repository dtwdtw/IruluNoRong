package com.wf.irulu.module.home.activity;

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
import android.widget.ProgressBar;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * @描述: 轮播图的图片跳转到内部WebView的activity
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.content.activity
 * @类名:ShufflingFigureActivity
 * @作者: 左杰
 * @创建时间:2015/11/27 18:50
 */
public class ShufflingFigureActivity extends CommonTitleBaseActivity {

    private WebView mShufflingFigureWb;
    private ProgressBar mShufflingFigureProgress;
    private String mTitle;

    @Override
    protected String setWindowTitle() {
        return mTitle;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_shuffling_figure);
        mTitle = getIntent().getStringExtra("title");
    }

    @Override
    public void initView() {
        mShufflingFigureWb = (WebView) findViewById(R.id.shuffling_figure_wb);
        mShufflingFigureProgress = (ProgressBar) findViewById(R.id.shuffling_figure_progress);

    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra("url");
        WebSettings settings = mShufflingFigureWb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        //设置进度条的值
        mShufflingFigureWb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mShufflingFigureProgress.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        // 加载网页
        mShufflingFigureWb.loadUrl(url);
        mShufflingFigureWb.setWebViewClient(new WebViewClient() {
            /**不让跳转到系统的浏览器*/
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                HashMap<String, String> map = parseUrl(url);
                if (null == map) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                int catgoryType = Integer.parseInt(map.get("actionCategory"));
                String content = map.get("content");
                String name = "name";
                if (map.containsKey(name)) {
                    name = map.get(name);
                }
                if (catgoryType == 1) {
                    Intent intent_classify = new Intent(ShufflingFigureActivity.this, CategoryActivity.class);
                    startActivity(intent_classify);
                    return true;
                } else if (catgoryType == 2) {
                    ProductDetailActivity.startProductDetailActivity(ShufflingFigureActivity.this, content);
                    return true;
                } else if (catgoryType == 3) {
                    try {
                        content = URLDecoder.decode(content, "UTF-8");
                        loadUrl(content);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
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
                    return false;
                } else if (catgoryType == 5) {
//                    Intent intent_recommendation = new Intent(ShufflingFigureActivity.this, RecommendationActivity.class);
//                    intent_recommendation.putExtra("id", Integer.parseInt(content));
//                    startActivity(intent_recommendation);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mShufflingFigureProgress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mShufflingFigureProgress.setVisibility(View.GONE);
                // 内置跳转结束后停止死循环加载,shouldOverrideUrlLoading方法不再继续
                view.stopLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 配置HTTPS协议
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    @Override
    public void onClick(View v) {

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

    /**
     * 加载网页
     *
     * @param url
     */
    private void loadUrl(String url) {
        mShufflingFigureWb.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mShufflingFigureWb.canGoBack()) {
            mShufflingFigureWb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
