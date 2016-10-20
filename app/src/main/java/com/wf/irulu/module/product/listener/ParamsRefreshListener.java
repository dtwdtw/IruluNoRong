package com.wf.irulu.module.product.listener;

import java.util.HashMap;

/**
 * Created by XImoon on 15/11/5.
 */
public interface ParamsRefreshListener {

    /**
     * 设置参数列表的选择项
     * @param params 已选参数类型
     * @param key 正在改变的参数类型名称
     */
    void setChoosed(HashMap<String,String> params,String key);
}
