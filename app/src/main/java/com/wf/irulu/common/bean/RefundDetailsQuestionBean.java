package com.wf.irulu.common.bean;

import com.wf.irulu.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16. 售后回话的question
 */
public class RefundDetailsQuestionBean implements Serializable {
    private String id;
    private String orderId;
    private int userid;
    private int pskuid;
    private int serviceType;
    private String reason;
    private String content;
    private ArrayList<String> image;
    private int status;
    private  int time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPskuid() {
        return pskuid;
    }

    public void setPskuid(int pskuid) {
        this.pskuid = pskuid;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
