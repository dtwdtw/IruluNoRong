package com.wf.irulu.logic.parser;

import com.google.gson.Gson;
import com.wf.irulu.common.bean.WishInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @描述: TODO
 * @项目名: irulu1.2
 * @包名:com.wf.irulu.logic.parser
 * @类名:WishParser
 * @作者: Yuki
 * @创建时间:2015/11/13
 */
public class WishParser {

    public static ArrayList<WishInfo> parserWishList(Object dataObj){
        // 初始化列表
        ArrayList<WishInfo> wishList = new ArrayList<WishInfo>();
        // 判空操作
        if (dataObj == null){
            return wishList;
        }
        JSONArray jsonArray = null;
        try {
            // 判断实例类型
            if (dataObj instanceof JSONObject){
                // 对象类型
                jsonArray = ((JSONObject) dataObj).getJSONArray("list");
            }else if(dataObj instanceof JSONArray){
                // 对象数组类型
                jsonArray = (JSONArray) dataObj;
            }
            int size = jsonArray.length();
            Gson gson = new Gson();
            for (int i = 0; i < size; i++){
                // 集合动态添加对象
                wishList.add((new Gson().fromJson(jsonArray.getJSONObject(i).toString(),WishInfo.class)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wishList;
    }
}
