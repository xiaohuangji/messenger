package com.dajie.message.model.message;

/**
 * Created by wills on 5/14/14.
 */
public class UserBlock extends SystemContent {

    private String text="您的消息被对方拒收了";

    public UserBlock(String text) {
        this.text = text;
    }

    public UserBlock() {

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
