package com.dajie.message.service;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.MCPInteger;
import com.dajie.message.model.user.ProfileView;
import com.dajie.message.model.user.SendEmailResult;
import com.dajie.message.model.user.VerificationView;

/**
 * Created by John on 4/14/14.
 */
@RestBean
public interface IProfileService {

	/**
	 * 获取个人主页
	 * 
	 * @param _userId
	 * @param hostId
	 * @return
	 */
	ProfileView get(int _userId, int hostId);

	/**
	 * 修改姓名
	 * 
	 * @param _userId
	 * @param name
	 * @return
	 */
	int modifyName(int _userId, String name);

	/**
	 * 修改性别
	 * 
	 * @param _userId
	 * @param gender
	 * @return
	 */
	int modifyGender(int _userId, int gender);

	/**
	 * 修改头像
	 * 
	 * @param _userId
	 * @param avatar
	 * @param avatarMask
	 * @return
	 */
	int modifyAvatar(int _userId, String avatar, String avatarMask);

	/**
	 * 修改教育背景
	 * 
	 * @param _userId
	 * @param major
	 * @param school
	 * @param degree
	 * @return
	 */
	int modifyEducation(int _userId, String major, String school, int degree);

	/**
	 * 修改工作背景
	 * 
	 * @param _userId
	 * @param positionName
	 * @param positionType
	 * @param corpName
	 * @param corpId
	 * @param industry
	 * @param experience
	 * @param education
	 * @return
	 */
	int modifyCareer(int _userId, String positionName, int positionType,
			String corpName, int corpId, int industry, int experience,
			int education);

	/**
	 * 验证公司邮箱
	 * 
	 * @param _userId
	 * @param corpName
	 * @param email
	 * @return
	 */
	SendEmailResult verifyCorpEmail(int _userId, String corpName, String email);
	
	/**
	 * 获取最近的认证状态
	 * 
	 * @param _userId
	 * @param hostId
	 * @return
	 */
	VerificationView getLatestVerification(int _userId);

	/**
	 * 添加个人标签
	 * 
	 * @param _userId
	 * @param content
	 * @return
	 */
	MCPInteger addLabel(int _userId, String content);

	/**
	 * 修改个人标签
	 * 
	 * @param _userId
	 * @param labelId
	 * @param content
	 * @return
	 */
	MCPInteger modifyLabel(int _userId, int labelId, String content);

	/**
	 * 删除个人标签
	 * 
	 * @param _userId
	 * @param labelId
	 * @return
	 */
	int delLabel(int _userId, int labelId);

	/**
	 * 手动点赞
	 * 
	 * @param _userId
	 * @param labelId
	 * @return
	 */
	int likeLabel(int _userId, int labelId);

	/**
	 * 取消赞
	 * 
	 * @param _userId
	 * @param labelId
	 * @return
	 */
	int unlikeLabel(int _userId, int labelId);

}
