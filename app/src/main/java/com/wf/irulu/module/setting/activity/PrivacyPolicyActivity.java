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
 * 声明 privacypolicy 页面
 */
public class PrivacyPolicyActivity extends CommonTitleBaseActivity {

    private WebView wv;
    private ProgressBar pp_pb;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.privacy_policy);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_privacy_policy);
    }

    @Override
    public void initView() {
        wv = (WebView) findViewById(R.id.privacy_wv);
        pp_pb = (ProgressBar) findViewById(R.id.pp_pb);
    }

    /**
     * 加载webview
     */
    @Override
    public void initData() {
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        String did = CommonUtil.getDeviceId();
        if (did == null) {
            did = CommonUtil.getLocalMacAddress();
        }

        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pp_pb.setProgress(newProgress);
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
                pp_pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pp_pb.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        wv.loadUrl(SettingManager.URL_PRIVACY_POLICY + "?" + did);
    }

    @Override
    public void onClick(View view) {

    }
}
