package com.example.smartvest;


public class WeatherVO {

    double tempc;
    String time;
    String tweather;
    double humid;

    public WeatherVO(double tempc, String time, String tweather, double humid) {
        this.tempc = tempc;
        this.time = time;
        this.tweather = tweather;
        this.humid = humid;
    }

    public WeatherVO() {
    }

    public double getTempc() {
        return tempc;
    }

    public void setTempc(double tempc) {
        this.tempc = tempc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTweather() {
        return tweather;
    }

    public void setTweather(String tweather) {
        this.tweather = tweather;
    }

    public double getHumid() {
        return humid;
    }

    public void setHumid(double humid) {
        this.humid = humid;
    }
}