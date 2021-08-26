package com.example.smartvest;

public class QrVO {

    private String tv_company, tv_w_name, tv_work, tv_edu, tv_in_out;

    public QrVO(String tv_company, String tv_w_name, String tv_work, String tv_edu, String tv_in_out) {
        this.tv_company = tv_company;
        this.tv_w_name = tv_w_name;
        this.tv_work = tv_work;
        this.tv_edu = tv_edu;
        this.tv_in_out = tv_in_out;
    }

    public QrVO() {
    }

    public String getTv_company() {
        return tv_company;
    }

    public void setTv_company(String tv_company) {
        this.tv_company = tv_company;
    }

    public String getTv_w_name() {
        return tv_w_name;
    }

    public void setTv_w_name(String tv_w_name) {
        this.tv_w_name = tv_w_name;
    }

    public String getTv_work() {
        return tv_work;
    }

    public void setTv_work(String tv_work) {
        this.tv_work = tv_work;
    }

    public String getTv_edu() {
        return tv_edu;
    }

    public void setTv_edu(String tv_edu) {
        this.tv_edu = tv_edu;
    }

    public String getTv_in_out() {
        return tv_in_out;
    }

    public void setTv_in_out(String tv_in_out) {
        this.tv_in_out = tv_in_out;
    }
}
