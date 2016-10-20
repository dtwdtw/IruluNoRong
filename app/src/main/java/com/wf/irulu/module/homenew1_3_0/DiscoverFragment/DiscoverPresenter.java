package com.wf.irulu.module.homenew1_3_0.DiscoverFragment;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.manager.SettingManager;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.common.bean.HomeBean;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dtw on 16/5/7.
 */
public class DiscoverPresenter extends BaseService{
    private String URL_DISCOVER = SettingManager.URL_PRODUCT_SYSTEM + "/product/discover";
    private DiscoverDataListener discoverDataListener;
    private static DiscoverPresenter discoverPresenter;
    private DiscoverPresenter(){}
    static DiscoverPresenter getInstence(){
        if(discoverPresenter==null){
            discoverPresenter=new DiscoverPresenter();
        }
        return discoverPresenter;
    }
    public void setDiscoverDataListener(DiscoverDataListener discoverDataListener){
        this.discoverDataListener=discoverDataListener;
    }
    public void pullData(){
        final Gson gson = new Gson();

        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("id", id);
        params.put("page", "1");
        params.put("num", "20");
        IruluController controller= IruluController.getInstance();
        Headers headers = addHeadersFromOKHttp(controller);
        Request rq = HttpUtil.getRequest(URL_DISCOVER, headers, null);


        client.newCall(rq).enqueue(new RequestCallBack(URL_DISCOVER) {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                Log.e("hellolove",bean.data.toString());
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    HomeBean.DiscoverBean discoverBean = null;
                    try {
                        discoverBean = gson.fromJson(bean.data.toString(), HomeBean.DiscoverBean.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    discoverDataListener.onDiscoverBean(discoverBean);
//                    handleServiceSuccess(listener, ServiceListener.ActionTypes.HOME, null, homeList);
                } else {
//                    handleServiceFailure(listener, ServiceListener.ActionTypes.HOME, bean.msg, bean.code);
                }
            }
        });
    }
}
