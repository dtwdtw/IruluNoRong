package com.wf.irulu.common.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16. 售后回话的list字段
 */
public class RefundDetailsListBean implements Serializable {
    private String id;
    private String content;
    private ArrayList<String> image;
    private int time;
    private RefundDetailsMerchantInfoBean merchantInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public RefundDetailsMerchantInfoBean getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(RefundDetailsMerchantInfoBean merchantInfo) {
        this.merchantInfo = merchantInfo;
    }
}
