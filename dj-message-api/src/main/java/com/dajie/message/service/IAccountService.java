package com.dajie.message.service;

import java.util.Map;

import com.dajie.message.annotation.mcp.NoTicket;
import com.dajie.message.annotation.mcp.ParamNullable;
import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.enums.SmsSendTypeEnum;
import com.dajie.message.model.wrapper.LoginReturn;

/**
 * Account interface define
 * 
 * @author li.hui
 * 
 */
@RestBean
public interface IAccountService {

	/**
	 * 注册 ,不需要ticket
	 * 
	 * @param mobile
	 * @param verifyCode
	 * @param password 传输的password，已做md5(password).
	 * @return
	 */
	@NoTicket
	LoginReturn register(String mobile, String verifyCode, String password);

	/**
	 * 完善注册
	 * 
	 * @param _userId
	 * @param name
	 * @param gender
	 * @param avatar
	 * @param maskAvatar
	 * @return
	 */
	int completeRegister(int _userId, String name, int gender, String avatar,
			@ParamNullable(true) String avatarMask);

	/**
	 * 大街注册，第一方登陆时需调用
	 * 
	 * @param _appId
	 * @param account
	 * @param password 大街提供的login是明文，这里传过来的也是明文
	 * @param name
	 * @param gender
	 * @param avatar
	 * @param avatarMask
	 * @return
	 */
	@NoTicket
	LoginReturn registerByDJ(String _appId, String account, String password,
			String name, int gender, String avatar,
			@ParamNullable(true) String avatarMask);

	/**
	 * 第三方平台注册 OAuth2.0方式接入
	 * 
	 * @param _appId
	 * @param accessToken
	 * @param type
	 * @see{com.dajie.message.enum.PlatformEnum
	 * @param name
	 * @param gender
	 * @param avatar
	 * @param avatarMask
	 * @return
	 */
	@NoTicket
	LoginReturn registerBy3rdPlatform(String _appId, String accessToken,
			int type, String name, int gender, String avatar,
			@ParamNullable(true) String avatarMask);

	/**
	 * 登陆
	 * 
	 * @param mobile
	 * @param password 传输的password，已做md5(password).
	 * @return
	 */
	@NoTicket
	LoginReturn login(String mobile, String password);

	/**
	 * 大街登陆
	 * 
	 * @param account
	 * @param password 大街提供的login是明文，这里传过来的也是明文
	 * @return
	 */
	@NoTicket
	LoginReturn loginWithDJ(String account, String password);

	/**
	 * 第三方平台登陆，OAuth2.0方式
	 * 
	 * @param accessToken
	 * @param type
	 * @see{com.dajie.message.enum.PlatformEnum}
	 * @return
	 */
	@NoTicket
	LoginReturn loginWith3rdPlatform(String accessToken, int type);

	/**
	 * 发送验证码
	 * 
	 * @param mobile
	 * @param type
	 * @see{com.dajie.message.enums.SmsSendTypeEnum
	 * @return
	 */
	@NoTicket
	int sendSmsCode(String mobile, int type);

	/**
	 * 通过短信验证码重置密码
	 * 
	 * @param mobile
	 * @param newPassword  已做md5(password).
	 * @param verifyCode 
	 * @return
	 */
	@NoTicket
	int modifyPasswordByMobile(String mobile, String newPassword,
			String verifyCode);

	/**
	 * 手机号是否已被占用
	 * 
	 * @param mobile
	 * @return
	 */
	boolean isMobileUsed(String mobile);

	/**
	 * 绑定第三方信息
	 * 
	 * @param _appId
	 * @param _userId
	 * @param accessToken
	 * @param type
	 * @see{com.dajie.message.enums.SmsSendTypeEnum
	 * @return
	 */
	int bindAccount(String _appId, int _userId, String accessToken, int type);

	/**
	 * 重新加载第三方绑定
	 * 与bindAccount的区别：在一个用户已绑定#{type}类型的帐号时，bindAccount提示已绑定错误，reload会更新token或更新第三方信息
	 * @param _appId
	 * @param _userId
	 * @param accessToken
	 * @param type
	 * @return
	 */
	int reload3rdAccount(String _appId,int _userId,String accessToken,int type);

	/**
	 * 绑定大街帐号
	 * 
	 * @param _appId
	 * @param _userId
	 * @param account
	 * @param password
	 * @return
	 */
	int bindDJAccount(String _appId, int _userId, String account,
			String password);

	/**
	 * 解绑第三方帐号
	 * 
	 * @param _userId
	 * @param type
	 * @see{com.dajie.message.enum.PlatformEnum
	 * @return
	 */
	int unbindAccount(int _userId, int type);

	/**
	 * 校验验证码
	 * 
	 * @param mobile
	 * @param typeEnum
	 *            验证码业务类型 @see{com.dajie.message.enums.SmsSendTypeEnum}
	 * @param code
	 * @return
	 */
	boolean validateMobileCode(String mobile, SmsSendTypeEnum typeEnum,
			String code);

	/**
	 * 登出
	 * 
	 * @param _userId
	 * @return
	 */
	int logout(int _userId);

	/**
	 * 修改手机号 ，两个情况 1.已经有手机号和密码(手机号注册，已添加过的手机号的),只传一个参数:mobile。password可传null,""。
	 * 2.第三方帐号进入，没添加过手机的。传两个参数：mobile,password.这种情况下会设置这个手机的登陆密码
	 * 
	 * @param verifyCode
	 * @param _userId
	 * @param mobile
	 * @param password 已做md5(password).
	 * @return
	 */
	int changeMobile(int _userId, String verifyCode, String mobile,
			@ParamNullable(true) String password);

	/**
	 * 导入一个大街帐号
	 * 
	 * @param djUid
	 * @return 正常导入或已导入返回userId,失败返回-1
	 */
	int importDJUser(int djUid);
	
	/**
	 * 获取第三方绑定信息
	 * @param userId
	 * @return key:第三方类型{com.dajie.message.enum.PlatformEnum}.name; value：第三方平台信息
	 */
	Map<String, Map<String, String>> getBindInfo(int _userId);

}
