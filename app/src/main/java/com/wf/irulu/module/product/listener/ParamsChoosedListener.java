package com.wf.irulu.module.product.listener;

/**
 * Created by XImoon on 15/11/5.
 */
public interface ParamsChoosedListener {

    /**
     * 改变参数选择
     * @param pKey   参数名称
     * @param pValue 参数选择值
     */
    void changeParams(String pKey ,String pValue);
}
