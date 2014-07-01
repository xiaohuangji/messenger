package com.dajie.message.model.message;

import com.dajie.message.model.user.SimpleUserView;

/**
 * Created by wills on 5/14/14.
 */
public class LikeLabel extends SystemContent {

    private SimpleUserView userView;

    private String labelName;

    public LikeLabel(SimpleUserView userView,String labelName) {
        this.userView = userView;
        this.labelName=labelName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public SimpleUserView getUserView() {
        return userView;
    }

    public void setUserView(SimpleUserView userView) {
        this.userView = userView;
    }
}
