package com.wf.irulu.common.bean;

import java.io.Serializable;

/**
 * Created by daniel on 2015/11/6.
 */
public class QiNiuReturnBean implements Serializable{
    private String url;
    private String saveKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaveKey() {
        return saveKey;
    }

    public void setSaveKey(String saveKey) {
        this.saveKey = saveKey;
    }
}
