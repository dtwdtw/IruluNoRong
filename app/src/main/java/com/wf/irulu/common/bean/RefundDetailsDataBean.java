package com.wf.irulu.common.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/16. 售后回话的 data字段
 */
public class RefundDetailsDataBean implements Serializable {
    private RefundDetailsQuestionBean question;
    private ArrayList<RefundDetailsListBean> list;

    public RefundDetailsQuestionBean getQuestion() {
        return question;
    }

    public void setQuestion(RefundDetailsQuestionBean question) {
        this.question = question;
    }

    public ArrayList<RefundDetailsListBean> getList() {
        return list;
    }

    public void setList(ArrayList<RefundDetailsListBean> list) {
        this.list = list;
    }
}
