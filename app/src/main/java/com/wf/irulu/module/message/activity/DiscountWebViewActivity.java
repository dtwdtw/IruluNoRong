package com.wf.irulu.module.message.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.module.category.activity.CategoryActivity;
import com.wf.irulu.module.home.activity.HomeWebViewActivity;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * @描述: 折扣消息跳转到的WebView
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.module.message.activity
 * @类名:DiscountWebViewActivity
 * @作者: 左西杰
 * @创建时间:2015-8-17 下午4:09:23
 * 
 */
public class DiscountWebViewActivity extends CommonTitleBaseActivity {

	private WebView mDiscountWebview;
	private String mDetailUrl;
	private ProgressBar mBarWv;

	@Override
	public void onClick(View v) {

	}

	@Override
	protected String setWindowTitle() {
		Intent intent = getIntent();
		mDetailUrl = intent.getStringExtra("detailUrl");
//		mDetailUrl = "https://www.irulu.com/";
		String title = intent.getStringExtra("title");
		return title;
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_discount_webview);
	}

	@Override
	public void initView() {
		mDiscountWebview = (WebView) findViewById(R.id.discount_webview);
		mBarWv = (ProgressBar) findViewById(R.id.bar_wv);

		WebSettings settings = mDiscountWebview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDisplayZoomControls(true);
		settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		settings.setSupportZoom(true); // 支持缩放
	}

	@Override
	public void initData() {
		mDiscountWebview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mBarWv.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}
		});
		mDiscountWebview.loadUrl(mDetailUrl);

		mDiscountWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				HashMap<String, String> map = parseUrl(url);

				if (map != null) {
					int catgoryType = Integer.parseInt(map.get("actionCategory"));
					String content = map.get("content");
					if (catgoryType == 1) {
						Intent intent_classify = new Intent(DiscountWebViewActivity.this, CategoryActivity.class);
						startActivity(intent_classify);
						return true;
					} else if (catgoryType == 2) {
						ProductDetailActivity.startProductDetailActivity(DiscountWebViewActivity.this, content);
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
						HomeWebViewActivity.startProductDetailActivity(DiscountWebViewActivity.this, content);
						return true;
					}
				}
//				HomeWebViewActivity.startProductDetailActivity(DiscountWebViewActivity.this, url);
                return super.shouldOverrideUrlLoading(view, url);
//				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mBarWv.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mBarWv.setVisibility(View.GONE);
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
}
