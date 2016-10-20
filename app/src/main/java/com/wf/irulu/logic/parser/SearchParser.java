package com.wf.irulu.logic.parser;

import com.google.gson.Gson;
import com.wf.irulu.common.bean.WishInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.parser
 * @类名:HotSearchParser
 * @作者: Yuki
 * @创建时间:2015/11/14
 */
public class SearchParser {

    public static ArrayList<String> parserHotSearch(Object obj){
        ArrayList<String> mSearchHot = new ArrayList<String>();
        if (obj == null){
            return mSearchHot;
        }
        JSONArray jsonArray = null;
        try {
            if (obj instanceof JSONArray){
                jsonArray = (JSONArray) obj;
            }else if(obj instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) obj;
                jsonArray = jsonObject.getJSONArray("list");
            }
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                String hotWord = jsonArray.getString(i);
                mSearchHot.add(hotWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSearchHot;
    }

    public static ArrayList<WishInfo> parserSearchProductList(Object obj){
        ArrayList<WishInfo> mSearchProductInfo = new ArrayList<WishInfo>();
        if (obj == null){
            return mSearchProductInfo;
        }
        JSONArray jsonArray = null;
        try {
            if (obj instanceof JSONArray){
                jsonArray = (JSONArray) obj;
            }else if(obj instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) obj;
                jsonArray = jsonObject.getJSONArray("list");
            }
            int length = jsonArray.length();
            Gson gson = new Gson();
            WishInfo info = null;
            for (int i = 0; i < length; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                info = gson.fromJson(jsonObject.toString(), WishInfo.class);
                mSearchProductInfo.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSearchProductInfo;
    }
}
