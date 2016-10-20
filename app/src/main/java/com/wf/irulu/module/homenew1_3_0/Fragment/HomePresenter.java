package com.wf.irulu.module.homenew1_3_0.Fragment;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.FileUtils;
import com.wf.irulu.common.utils.HttpUtil;
import com.wf.irulu.logic.IruluController;
import com.wf.irulu.logic.listener.RequestCallBack;
import com.wf.irulu.logic.manager.CacheManager;
import com.wf.irulu.logic.manager.SettingManager;
import com.wf.irulu.logic.service.BaseService;
import com.wf.irulu.common.bean.HomeBean;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by dtw on 16/4/21.
 */
public class HomePresenter extends BaseService {
    String URL_HOME = SettingManager.URL_PRODUCT_SYSTEM + "/product/appInit";
    HomeDataListener homeDataListener;



    private static HomePresenter homePresenter;
    private HomePresenter(){}
    public static HomePresenter getIntance(){
        if(homePresenter==null){
            homePresenter=new HomePresenter();
        }
        return homePresenter;
    }

    public void setHomeDataListener(HomeDataListener homeDataListener){
        this.homeDataListener=homeDataListener;
    }

    public void pullHomeData(){
        final Gson gson = new Gson();
        final String savePath = CacheManager.getJsonCacheMyVisitorPath();

        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("id", id);
        params.put("page", "1");
        params.put("num", "20");
        final IruluController controller= IruluController.getInstance();
        Headers headers = addHeadersFromOKHttp(controller);
        Request rq = HttpUtil.getRequest(URL_HOME, headers, params);

        String jsonStr = FileUtils.readFileToString(savePath);
        if (!TextUtils.isEmpty(jsonStr)) {
            HomeBean homeBean = null;
            try {
                homeBean = gson.fromJson(jsonStr, HomeBean.class);
                homeDataListener.onHomeBean(homeBean);
                Log.v("hellolove",jsonStr);
            } catch (JsonSyntaxException je) {
                je.printStackTrace();
            }

        }

        client.newCall(rq).enqueue(new RequestCallBack(URL_HOME) {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(CommonServiceBean bean) {
                Log.e("hellolove",bean.data.toString());
                if (bean.code == ErrorCodeUtils.CODE_SUCCESS) {
                    HomeBean homeBean = null;
                    try {
                        homeBean = gson.fromJson(bean.data.toString(), HomeBean.class);
                        LoginUser loginUser = controller.getCacheManager().getLoginUser();

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    homeDataListener.onHomeBean(homeBean);
//                    handleServiceSuccess(listener, ServiceListener.ActionTypes.HOME, null, homeList);
                    FileUtils.saveFileByString(bean.data.toString(), savePath);
                } else {
//                    handleServiceFailure(listener, ServiceListener.ActionTypes.HOME, bean.msg, bean.code);
                }
            }
        });
    }
}
