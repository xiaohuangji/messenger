package com.dajie.message.model.message;

/**
 * Created by wills on 3/24/14.
 */
public class Text extends ChatContent {

    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
