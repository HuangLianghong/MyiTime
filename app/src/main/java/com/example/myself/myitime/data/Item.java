package com.example.myself.myitime.data;

import java.io.Serializable;

/**
 * Created by Myself on 2019/11/4.
 */

public class Item implements Serializable {
    private String title;
    private String remark;
    private String date;
    private String leftTime;
    private byte[] pic;
    private int years;
    private int months;
    private int days;
    private int hours;
    private int minutes;

    public int getHours() {
        return hours;
    }

    public String getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(String leftTime) {
        this.leftTime = leftTime;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Item(String title, String remark, byte[] pic, int years, int months, int days,int hours,int minutes) {
        this.title = title;
        this.remark = remark;
        this.pic = pic;
        this.years = years;
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes=minutes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }
}