package com.wf.irulu.common.bean;

import java.io.Serializable;

public class NewVersionInfors implements Serializable {
    private String content;
    private int updateType;
    private String url;
    private String version;
    private String size;

    public NewVersionInfors() {
    }

    public NewVersionInfors(String content, int updateType, String url, String version, String size) {
        this.content = content;
        this.updateType = updateType;
        this.url = url;
        this.version = version;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    @Override
    public String toString() {
        return "NewVersionInfors{" +
                "content='" + content + '\'' +
                ", updateType=" + updateType +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
