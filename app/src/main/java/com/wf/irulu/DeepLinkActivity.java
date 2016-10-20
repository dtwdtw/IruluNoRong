package com.wf.irulu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.facebook.FacebookSdk;
import com.wf.irulu.common.utils.ExitionUtil;
import com.wf.irulu.logic.db.DbHelper;
import com.wf.irulu.module.product.activity.ProductDetailActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import bolts.AppLinks;

public class DeepLinkActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);


        Intent intent =getIntent();
        HashMap<String,String> mParams = new HashMap<String,String>();
        FacebookSdk.sdkInitialize(this);
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl == null){
            targetUrl = AppLinks.getTargetUrl(getIntent());
        }
        if (targetUrl != null){
            String url = null;
            try {
               url =  URLDecoder.decode(targetUrl.toString(),"UTF-8");
               targetUrl = Uri.parse(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            url = targetUrl.getQuery();
            String[] params = url.split("&");
            for (int i = 0; i < params.length;i++){
                String[] value = params[i].split("=");
                if (value.length == 2) {
                    try {
                        mParams.put(value[0].trim(), URLDecoder.decode(value[1].trim(),"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            if ("2".equals(mParams.get("actionCategory"))){
                intent = new Intent(this,ProductDetailActivity.class);
                try {
                    intent.putExtra("id", Integer.parseInt(mParams.get("content")));
                }catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(intent);
            }else if("3".equals(mParams.get("actionCategory"))){
//                intent = new Intent(this,PromotionWebViewActivity.class);
                intent.putExtra("moreUrl",mParams.get("content"));
                intent.putExtra("title",mParams.get("name"));
                startActivity(intent);
            }
            finish();
        }
        //fasdfasfasdf
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbHelper.getInstance().updateCurrentPage(IruluApplication.getInstance().getNo(), "DeepLinkActivity");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获按下返回键事件
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //判断在此页面之前是否有页面
            if (!ExitionUtil.getInstance().isRecoedAble()) { //当前页面为最深度的页面或者前一个被返回的页面已超时无需再记录
                //设置此页为Exit Page
                DbHelper.getInstance().updateExitPage(IruluApplication.getInstance().getNo(), "DeepLinkActivity");
                ExitionUtil.getInstance().setEnable();
            } /** else {
                仍然将之前的页面设置为Exit Page
            } **/
        }

        return super.onKeyDown(keyCode, event);
    }
}
