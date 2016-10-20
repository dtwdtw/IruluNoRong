package com.wf.irulu.module.mycoupon.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.HttpConstantUtils;
import com.wf.irulu.logic.service.AASService;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 我的优惠券
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.module.mycoupon.activity
 * @类名:MyCouponActivity
 * @作者: 左杰
 * @创建时间:2015/11/20 20:00
 */
public class MyCouponActivity extends CommonTitleBaseActivity{

    private WebView mWebView;
    private ProgressBar mMyCouponProgress;

    @Override
    protected String setWindowTitle() {
        return "My Coupon";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_coupon);
    }

    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.mycoupon_wv);
        mWebView.getSettings().setSupportZoom(true);
        mMyCouponProgress = (ProgressBar) findViewById(R.id.mycoupon_progress);
    }

    @Override
    public void initData() {
        String url = AASService.URL_COUPON;//请求网络的URL(http://account.irulu.big188.com/coupon)
        //添加请求头
        Map<String,String> extraHeaders = new HashMap<String,String>();
        LoginUser loginUser = controller.getCacheManager().getLoginUser();
        extraHeaders.put(HttpConstantUtils.X_APP_DEVICE, HttpConstantUtils.DEVICE);
        extraHeaders.put(HttpConstantUtils.X_APP_VERSION, HttpConstantUtils.VERSION);
        extraHeaders.put(HttpConstantUtils.X_API_VERSION, HttpConstantUtils.API_VERSION);
        extraHeaders.put(HttpConstantUtils.X_DID, HttpConstantUtils.DID);
        extraHeaders.put(HttpConstantUtils.X_UTM, HttpConstantUtils.getMetaData());
        extraHeaders.put(HttpConstantUtils.USER_AGENT,"iRulu/"+HttpConstantUtils.VERSION+"("+HttpConstantUtils.DEVICE+";"+HttpConstantUtils.SYSTEM+" "+android.os.Build.VERSION.SDK_INT + ") " + HttpConstantUtils.getLocation() + " NetType/"+ ConstantsUtils.NETWORK_TYPE);
        extraHeaders.put(HttpConstantUtils.X_UID,String.valueOf(loginUser.getUid()));
        extraHeaders.put(HttpConstantUtils.X_TOKEN,String.valueOf(loginUser.getToken()));
        extraHeaders.put(HttpConstantUtils.X_USER_TYPE, String.valueOf(loginUser.getUserType()));
        //设置进度条的值
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mMyCouponProgress.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient(){
            /**不让跳转到系统的浏览器*/
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mMyCouponProgress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mMyCouponProgress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        //加载网页
        mWebView.loadUrl(url,extraHeaders);
    }

    @Override
    public void onClick(View v) {

    }
}
