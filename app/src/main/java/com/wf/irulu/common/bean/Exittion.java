package com.wf.irulu.common.bean;

/**
 * Created by imaster on 16/1/20.
 */
public class Exittion {

    int no;
    String currentPage;
    String exitPage;
    String details;
    int status;

    public void setNo(int no) {
        this.no = no;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public void setExitPage(String exitPage) {
        this.exitPage = exitPage;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNo() {
        return no;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public String getExitPage() {
        return exitPage;
    }

    public String getDetails() {
        return details;
    }

    public int getStatus() {
        return status;
    }
}
