package com.dajie.messageadmin.model;

import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.model.user.UserLabel;

import java.util.List;

/**
 * Created by wills on 6/7/14.
 */
public class ExtUserView  {

    private SimpleUserView simpleUserView;

    private List<UserLabel> userLabelList;

    public SimpleUserView getSimpleUserView() {
        return simpleUserView;
    }

    public void setSimpleUserView(SimpleUserView simpleUserView) {
        this.simpleUserView = simpleUserView;
    }

    public List<UserLabel> getUserLabelList() {
        return userLabelList;
    }

    public void setUserLabelList(List<UserLabel> userLabelList) {
        this.userLabelList = userLabelList;
    }
}
