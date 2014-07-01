package com.dajie.message.model.message;

/**
 * Created by wills on 3/24/14.
 */
public class Image extends ChatContent {

    private String imgUrl;

    private int width;

    private int heigth;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }
}
