package com.dajie.message.service;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.user.SimpleUserView;

import java.util.List;

/**
 * Created by wills on 5/4/14.
 */
@RestBean
public interface IBlackListService {

    public int addToBlackList(int _userId,int blockId);

    public int removeFromBlackList(int _userId,int blockId);

    public List<SimpleUserView> getBlackList(int _userId);
}
