package com.wf.irulu.common.bean;

import java.io.Serializable;

/**
 * Created by daniel on 2015/11/16. 售后回话的MerchantInfo字段
 */
public class RefundDetailsMerchantInfoBean implements Serializable{
    private int  mid;
    private String name;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
