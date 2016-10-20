package com.wf.irulu.module.setting.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.CommonUtil;
import com.wf.irulu.logic.manager.SettingManager;

/**
 * Created by daniel on 2015/11/2.
 * 声明 termsservice 页面
 */
public class TermsServiceActivity extends CommonTitleBaseActivity {

    private WebView wv;
    public ProgressBar ts_pb;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.terms_of_service);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_terms_service);
    }

    @Override
    public void initView() {
        wv = (WebView) findViewById(R.id.terms_wv);
        ts_pb = (ProgressBar) findViewById(R.id.ts_pb);

    }

    /**
     * 加载webview
     */
    @Override
    public void initData() {
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        String did = CommonUtil.getDeviceId();
        if(did==null){
            did = CommonUtil.getLocalMacAddress();
        }
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ts_pb.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ts_pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ts_pb.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });


        wv.loadUrl(SettingManager.URL_TERMS_OF_SERVICE+"?"+did);
    }

    @Override
    public void onClick(View view) {

    }
}
