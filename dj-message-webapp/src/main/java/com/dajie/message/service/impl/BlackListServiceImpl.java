package com.dajie.message.service.impl;

import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.dao.BlackListDAO;
import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.service.IBlackListService;
import com.dajie.message.service.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/4/14.
 */
@Component("blackListService")
public class BlackListServiceImpl implements IBlackListService {

    @Autowired
    private BlackListDAO blackListDAO;

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public int addToBlackList(int _userId, int blockId) {
        try{
            blackListDAO.add(_userId,blockId,new Date());
        }catch(Exception e){
           //不处理，全部返回成
//            return CommonResultCode.OP_FAIL;

        }
        return  CommonResultCode.OP_SUCC;
    }

    @Override
    public int removeFromBlackList(int _userId, int blockId) {
        try{
            blackListDAO.remove(_userId,blockId);
        }catch(Exception e){
            //不处理，全部返回成
//            return CommonResultCode.OP_FAIL;
        }
        return  CommonResultCode.OP_SUCC;
    }

    @Override
    public List<SimpleUserView> getBlackList(int _userId) {
        List<Integer> userIds=blackListDAO.getList(_userId);
        if(userIds==null){
            return null;
        }
        List<SimpleUserView> userViews=new ArrayList<SimpleUserView>();
        for(Integer id:userIds){
            SimpleUserView userView=userProfileService.getSimpleUserView(id);
            userViews.add(userView);
        }
        return userViews;
    }
}
