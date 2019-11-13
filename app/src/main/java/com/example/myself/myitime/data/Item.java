package com.example.myself.myitime.data;

import java.io.Serializable;

/**
 * Created by Myself on 2019/11/4.
 */

public class Item implements Serializable {
    private String title;
    private String remark;
    private String date;
    private byte[] pic;

    public Item(String title, String remark, String date, byte[] pic) {
        this.title = title;
        this.remark = remark;
        this.date = date;
        this.pic = pic;
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