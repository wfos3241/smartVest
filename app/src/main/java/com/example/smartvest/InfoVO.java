package com.example.smartvest;

public class InfoVO {

    private String title;
    private String date;

    public InfoVO(String title, String date) {
        this.title = title;
        this.date = date;
    }
    public InfoVO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
