package com.dajie.message.service;

import java.util.List;

import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEducation;
import com.dajie.message.model.user.UserLabel;

/**
 * Created by John on 4/14/14. inner use, please don't expose it as REST bean
 */

public interface IUserProfileService {

	/**
	 * 创建用户基本信息
	 * 
	 * @param userBase
	 *            userbase 里面如果有password，已完成md5(md5(password),salt)
	 * @return
	 */
	int createUserBase(UserBase userBase);

	/**
	 * 获取用户基本信息
	 * 
	 * @param userId
	 * @return
	 */
	UserBase getUserBase(int userId);

	/**
	 * 更新用户姓名
	 * 
	 * @param userId
	 * @Param name
	 * @return
	 */
	boolean updateName(int userId, String name);

	/**
	 * 更新用户性别
	 * 
	 * @param userId
	 * @Param gender
	 * @return
	 */
	boolean updateGender(int userId, int gender);

	/**
	 * 修改用户头像
	 * 
	 * @param userId
	 * @Param avatar
	 * @Param avatarMask
	 * @return
	 */
	boolean updateAvatar(int userId, String avatar, String avatarMask);

	/**
	 * 创建用户教育背景
	 * 
	 * @param userEducation
	 * @return
	 */
	boolean createUserEducation(UserEducation userEducation);

	/**
	 * 获取用户教育背景
	 * 
	 * @param userId
	 * @return
	 */
	UserEducation getUserEducation(int userId);

	/**
	 * 更新用户教育背景
	 * 
	 * @param userEducation
	 * @return
	 */
	boolean updateUserEducation(UserEducation userEducation);

	/**
	 * 创建用户职场背景
	 * 
	 * @param userCareer
	 * @return
	 */
	boolean createUserCareer(UserCareer userCareer);

	/**
	 * 获取用户职场背景
	 * 
	 * @param userId
	 * @return
	 */
	UserCareer getUserCareer(int userId);

	/**
	 * 更新用户职场背景
	 * 
	 * @param userCareer
	 * @return
	 */
	boolean updateUserCareer(UserCareer userCareer);

	/**
	 * 获取用户的认证状态
	 * 
	 * @param userId
	 * @return
	 */
	int getVerification(int userId);

	/**
	 * 判断用户是否已经通过认证
	 * 
	 * @param userId
	 * @return
	 */
	boolean isVerified(int userId);

	/**
	 * 判断用户的最近一次认证是否失败
	 * 
	 * @param userId
	 * @return
	 */
	boolean isVerifyFailed(int userId);

	/**
	 * 更新用户认证状态
	 * 
	 * @param userId
	 * @param verification
	 * @return
	 */
	boolean updateVerification(int userId, int verification);

	/**
	 * 获取学校标签
	 * 
	 * @param school
	 * @return
	 */
	String getSchoolLabel(String school);

	/**
	 * 根据标签id获取标签
	 * 
	 * @param labelId
	 * @return
	 */
	UserLabel getLabelById(int labelId);

	/**
	 * 获取用户的所有标签
	 * 
	 * @param userId
	 * @return
	 */
	List<UserLabel> getLabels(int userId);

	/**
	 * 判断是否好友
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	boolean isFriend(int userId, int friendId);

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	SimpleUserView getSimpleUserView(int userId);

	/**
	 * 通守手机号获取用户
	 * 
	 * @param mobile
	 * @return
	 */
	public UserBase getUserBaseByMobile(String mobile);

	/**
	 * 更新用户密码
	 * 
	 * @param userId
	 * @param password
	 *            ,已完成md5(md5(password),salt)
	 * @param salt
	 * @return
	 */
	public boolean updatePassword(int userId, String password, String salt);

	/**
	 * 是否是一个正常的手机用户，能用手机号登陆
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isMobileUser(int userId);

	/**
	 * 完成注册时必要信息
	 * 
	 * @param userId
	 * @param name
	 * @param gender
	 * @param avatar
	 * @param avatarMask
	 * @return
	 */
	public boolean completeRegInfo(int userId, String name, int gender,
			String avatar, String avatarMask);

	/**
	 * 修改手机号和密码
	 * 
	 * @param userId
	 * @param mobile
	 * @param password
	 *            已完成md5(md5(password),salt)
	 * @param salt
	 * @return
	 */
	public boolean updateMobileAndPassword(int userId, String mobile,
			String password, String salt);

	/**
	 * 修改手机号
	 * 
	 * @param userId
	 * @param mobile
	 * @return
	 */
	boolean updateMobile(int userId, String mobile);

	/**
	 * 冻结用户
	 * 
	 * @param userId
	 * @return
	 */
	boolean freezeUser(int userId);

	/**
	 * 解冻用户
	 * 
	 * @param userId
	 * @return
	 */
	boolean unfreezeUser(int userId);

	/**
	 * 用户姓名未通过审核
	 * 
	 * @param userId
	 * @return
	 */
	int auditUserNameNotPass(int userId);

	/**
	 * 清除用户姓名审核结果
	 * 
	 * @param userId
	 * @return
	 */
	int clearUserNameAudit(int userId);

	/**
	 * 用户教育经历未通过审核
	 * 
	 * @param userId
	 * @return
	 */
	int auditUserEducationNotPass(int userId);

	/**
	 * 清除用户教育经历审核结果
	 * 
	 * @param userId
	 * @return
	 */
	int clearUserEducationAudit(int userId);

	/**
	 * 用户工作经历未通过审核
	 * 
	 * @param userId
	 * @return
	 */
	int auditUserCareerNotPass(int userId);

	/**
	 * 清除用户工作经历审核结果
	 * 
	 * @param userId
	 * @return
	 */
	int clearUserCareerAudit(int userId);

	/**
	 * 更新用户最近登录时间为当前时间
	 * 
	 * @param userId
	 * @return
	 */
	boolean updateLastLogin(int userId);

}
