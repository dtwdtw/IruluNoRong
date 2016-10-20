package com.wf.irulu.common.bean;

import java.io.Serializable;

/**
 * Created by daniel on 2015/11/6.
 */
public class UploadPicKeyTokenBean implements Serializable{
    private String key;
    private String token;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
