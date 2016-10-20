package com.wf.irulu.module.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.MainActivity;
import com.wf.irulu.R;
import com.wf.irulu.common.base.BaseActivity;
import com.wf.irulu.common.bean.OrderDetail;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.manager.SettingManager;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.home.activity.HomeWebViewActivity;
import com.wf.irulu.module.order.activity.OrderDetailActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by dtw on 16/3/25.
 */
public class WebPaymentActivity extends BaseActivity {

    IruluController iruluController;
    WebView webView;
    OrderDetail mOrderDetail;
    ImageView mTitleCancel;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_webpay);

    }

    @Override
    public void initView() {
        webView= (WebView) findViewById(R.id.order_view);
        mTitleCancel= (ImageView) findViewById(R.id.title_cancel);

    }

    @Override
    public void initData() {
        iruluController=IruluController.getInstance();
        mOrderDetail = (OrderDetail) getIntent().getParcelableExtra("orderDetail");
        webView.loadUrl(SettingManager.URL_WEBPAY_SYSTEM + mOrderDetail.getOrderId() + ".html", BaseService.getWebHead(iruluController));
        final WebSettings webSettings=webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (webView.canGoBack()) {
                    mTitleCancel.setVisibility(View.VISIBLE);
                } else {
                    mTitleCancel.setVisibility(View.GONE);
                }

//                if(url.contains("http://payment.irulusales.top")||url.contains("http://m.irulusales.top")||url.contains("https://m.irulu.com")||url.contains("http://payment.irulu.com")){
                if(url.contains("//m.irulusales.top")||url.contains("//m.irulu.com")){
                    webView.loadUrl(url,BaseService.getWebHead(iruluController));
                }
                else if(url.startsWith("openapp.wfmobilemall")){
                    switch (Integer.valueOf(Uri.parse(url).getQueryParameter("actionCategory"))){
                        case 1:
                            Intent intent_classify = new Intent(WebPaymentActivity.this, CategoryActivity.class);
                            startActivity(intent_classify);
                            break;
                        case 2:
                            ProductDetailActivity.startProductDetailActivity(WebPaymentActivity.this, Uri.parse(url).getQueryParameter("content"));
                            break;
                        case 3:
                            webView.loadUrl(url);
                            break;
                        case 4://跳转浏览器
                                Intent intent_web = new Intent(Intent.ACTION_VIEW);
                                intent_web.setData(Uri.parse( Uri.parse(url).getQueryParameter("content")));
                                startActivity(intent_web);
                            break;
                        case 5:
                            break;
                        case 6:
                            OrderDetailActivity.startOrderDetailActivity(WebPaymentActivity.this,Uri.parse(url).getQueryParameter("content"));
                            finish();
                            break;
                        case 7:
                            break;
                        case 8:
                            Intent intent=new Intent(WebPaymentActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                    }
                    controller.postOrderListCallback();
                }
                else{
                    webView.loadUrl(url);
                }
                ILog.e("hellolove",url);
                return true;
            }

        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
                break;
            case R.id.title_cancel:
                finish();
                break;
        }
    }

    public static void startWebPaymentActivity(Context context,OrderDetail orderDetail){
        Intent intent=new Intent(context,WebPaymentActivity.class);
        intent.putExtra("orderDetail",orderDetail);
        context.startActivity(intent);
    }
}
