package com.example.myself.myitime.data;

/**
 * Created by Myself on 2019/11/11.
 */

public class AddSetting {
    private int pictureId;
    private String title;
    private String remark;

    public AddSetting(int pictureId, String title, String remark) {
        this.pictureId = pictureId;
        this.title = title;
        this.remark = remark;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
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
}
