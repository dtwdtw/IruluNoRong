package com.wf.irulu.module.product.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;

/**
 * Created by XImoon on 15/11/9.
 */
public class ProductDescriptionActivity extends CommonTitleBaseActivity{

    /** 取消返回按钮*/
    private ImageView mDescriptionIvCancel;
    /** 进度条*/
    private ProgressBar mDescriptionPbProgress;
    /** webview加载页面*/
    private WebView mDescriptionWvDetail;
    /** 加载地址*/
    private String mUrl;

    @Override
    protected String setWindowTitle() {
        return null;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_product_description);
    }

    @Override
    public void initView() {
        mDescriptionIvCancel = bindView(R.id.description_iv_cancel);
        mDescriptionPbProgress = bindView(R.id.description_pb_progress);
        mDescriptionWvDetail = bindView(R.id.description_wb_detail);
        mDescriptionIvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mUrl = getIntent().getStringExtra("url");
        // 设置进度条最大进度
        mDescriptionPbProgress.setMax(100);
        // 设置等待过程
        mDescriptionWvDetail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 设置加载进度
                mDescriptionPbProgress.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        // 配置webview
        mDescriptionWvDetail.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 设置跳转模式(应用内)
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 显示进度条
                mDescriptionPbProgress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 取消进度条
                mDescriptionPbProgress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 配置HTTPS协议
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        // 设置放大缩小
        mDescriptionWvDetail.getSettings().setSupportZoom(true);
        mDescriptionWvDetail.getSettings().setBuiltInZoomControls(true);
        mDescriptionWvDetail.getSettings().setJavaScriptEnabled(true);
        mDescriptionWvDetail.getSettings().setDisplayZoomControls(true);
        // 判空加载
        if(!TextUtils.isEmpty(mUrl)) {
            mDescriptionWvDetail.loadUrl(mUrl);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.description_iv_cancel:
                mDescriptionWvDetail.stopLoading();
                finish();
                break;
            default:
                break;
        }
    }

    public static void startProductDescriptionActivity(Activity pActivity,String pUrl){
        Intent intent = new Intent(pActivity,ProductDescriptionActivity.class);
        intent.putExtra("url",pUrl);
        pActivity.startActivity(intent);
    }
}
