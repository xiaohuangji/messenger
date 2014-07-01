package com.dajie.message.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.constants.returncode.AccountResultCode;
import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.dao.BlackListDAO;
import com.dajie.message.dao.FriendRequestsDAO;
import com.dajie.message.dao.FriendsDAO;
import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IRelationService;
import com.dajie.message.service.IUserProfileService;

/**
 * Created by John on 4/16/14.
 */
@Component("relationService")
public class RelationServiceImpl implements IRelationService {

	@Autowired
	private FriendRequestsDAO friendRequestsDAO;

	@Autowired
	private FriendsDAO friendsDAO;

	@Autowired
	private BlackListDAO blackListDAO;

	@Autowired
	private IUserProfileService userProfileService;

	@Autowired
	private IPushService pushService;
	
	@Override
	public int sendFriendRequest(int _userId, int toId) {
		if (_userId == toId)
			return CommonResultCode.OP_FAIL;
		
		if (friendsDAO.isFriend(_userId, toId) > 0)
			return CommonResultCode.OP_FAIL;
		
		if (blackListDAO.isBlocked(toId, _userId) > 0)
			// 黑名单
			return CommonResultCode.OP_FAIL;
		
		if (friendRequestsDAO.sendFriendRequest(_userId, toId)) {
			pushService.sendCardRequest(_userId, toId);
			return CommonResultCode.OP_SUCC;
		} else {
			return CommonResultCode.OP_FAIL;
		}
	}

	@Override
	public List<SimpleUserView> getFriendRequests(int _userId, int pageNo,
			int pageSize) {
		List<Integer> friendRequests = friendRequestsDAO.getFriendRequests(
				_userId, (pageNo - 1) * pageSize, pageSize);
		List<SimpleUserView> view = new ArrayList<SimpleUserView>();
		/**
		 * There seems to be a performance problem, but premature optimization
		 * is the root of all evil.
		 **/
		for (Integer userId : friendRequests) {
			SimpleUserView user = userProfileService.getSimpleUserView(userId);
			view.add(user);
		}
		return view;
	}

	@Override
	public int acceptOrIgnore(int _userId, int fromId, int accept) {
		if (accept == 1) {
			if (friendRequestsDAO.delFriendRequest(_userId, fromId)) {
				if (friendsDAO.addFriend(_userId, fromId)
						&& friendsDAO.addFriend(fromId, _userId)) {
					pushService.sendCardAgree(_userId, fromId);
					return CommonResultCode.OP_SUCC;
				} else {
					friendRequestsDAO.sendFriendRequest(fromId, _userId);
					return CommonResultCode.OP_FAIL;
				}
			} else {
				return CommonResultCode.OP_FAIL;
			}
		} else {
			return friendRequestsDAO.delFriendRequest(_userId, fromId) ? CommonResultCode.OP_SUCC
					: CommonResultCode.OP_FAIL;
		}
	}

	@Override
	public List<SimpleUserView> getFriends(int _userId, int pageNo, int pageSize) {
		List<Integer> friends = friendsDAO.getFriends(_userId, (pageNo - 1)
				* pageSize, pageSize);
		List<SimpleUserView> view = new ArrayList<SimpleUserView>();
		/**
		 * There seems to be a performance problem, but premature optimization
		 * is the root of all evil.
		 **/
		for (Integer userId : friends) {
			SimpleUserView user = userProfileService.getSimpleUserView(userId);
			view.add(user);
		}
		return view;
	}

	@Override
	public int delFriend(int _userId, int friendId) {
		if (friendsDAO.delFriend(_userId, friendId)
				&& friendsDAO.delFriend(friendId, _userId)) {
			pushService.sendUnFriend(_userId, friendId);
			return CommonResultCode.OP_SUCC;
		} else if (friendsDAO.isFriend(_userId, friendId) == 0 || friendsDAO.isFriend(friendId, _userId) == 0){
			return AccountResultCode.HAS_DELETED;
		}	
		else {
			return CommonResultCode.OP_FAIL;
		}
	}

}
