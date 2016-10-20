package com.wf.irulu.logic.listener;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wf.irulu.common.bean.CommonServiceBean;
import com.wf.irulu.common.utils.CommonJsonParser;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.logic.listener.ServiceListener.ActionTypes;

import java.io.IOException;

/**
 * OKHTTP 响应回调接口类
 */
public abstract class RequestCallBack implements Callback {

    private String url;
    private ActionTypes actionTypes;

    private static final String TAG = RequestCallBack.class.getCanonicalName();

    public RequestCallBack(String url) {
        this.url = url;
    }

    public RequestCallBack(String url, ActionTypes actionTypes) {
        this.url = url;
        this.actionTypes = actionTypes;
    }

    @Override
    public void onFailure(Request request, IOException e){
        onFailure(request,actionTypes);
    }

    /**
     * 响应成功后回调方法
     * @param bean 回调内容
     */
    public void onResponse(CommonServiceBean bean){}

    public void onResponse(CommonServiceBean bean,ActionTypes actionTypes){}

    public void onFailure(Request request, ActionTypes actionTypes){}

    @Override
    public void onResponse(Response response) throws IOException {
        String result = response.body().string();
        ILog.e(TAG, "url ==> " + url);
         int n=result.length()/500+1;
        for(int i=0;i<n;i++){
            if(i==n-1){
                Log.d("TB",result.substring(500*i,result.length()));
            }else{
                Log.d("TB",result.substring(500*i,500*(i+1)));
            }
        }

        CommonServiceBean bean = CommonJsonParser.commonParser(result);
        onResponse(bean);
        onResponse(bean,actionTypes);
    }
}
