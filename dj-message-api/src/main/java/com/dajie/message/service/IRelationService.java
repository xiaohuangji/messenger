package com.dajie.message.service;

import java.util.List;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.user.SimpleUserView;



/**
 * Created by John on 4/14/14.
 */

@RestBean
public interface IRelationService {
	
	/**
	 * 发送名片，请求加好友
	 * 
	 * @param _userId
	 * @param toId
	 * @return
	 */
	
	int sendFriendRequest(int _userId, int toId);

	/**
	 * 获取好友请求
	 * 
	 * @param _userId
	 * @param pageNo 
	 * @param pageSize
	 * @return
	 */
	List<SimpleUserView> getFriendRequests(int _userId, int pageNo, int pageSize);


	/**
	 * 接受或忽略好友请求
	 * 
	 * @param _userId
	 * @param fromId
	 * @param accept
	 * @return
	 */
	int acceptOrIgnore(int _userId, int fromId, int accept);


	/**
	 * 获取好友列表
	 * 
	 * @param _userId
	 * @param pageNo 
	 * @param pageSize
	 * @return
	 */
	List<SimpleUserView> getFriends(int _userId, int pageNo, int pageSize);


	/**
	 * 删除好友
	 * 
	 * @param _userId
	 * @return
	 */
	int delFriend(int _userId, int friendId);

}
