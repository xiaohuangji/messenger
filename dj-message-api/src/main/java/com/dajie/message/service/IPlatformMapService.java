package com.dajie.message.service;

import java.util.List;

import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.PlatformMap;

public interface IPlatformMapService {
	/**
	 * 获取用户的绑定信息
	 * @param userId
	 * @return
	 */
	public List<PlatformMap> getPlatformMapsByUserId(int userId);

	/**
	 * 获取某个第三方帐号的绑定信息
	 * @param type
	 * @param platformUid
	 * @return
	 */
	public PlatformMap getPlatformMap(PlatformEnum type, String platformUid);

	/**
	 * 获取用户指定类型的绑定信息
	 * @param userId
	 * @param type
	 * @return
	 */
	public PlatformMap getPlatformMapByUserIdAndType(int userId,
			PlatformEnum type);

	/**
	 * 第三方帐号是否被占用
	 * @param type
	 * @param platformUid
	 * @return
	 */
	public boolean isAccountUsed(PlatformEnum type, String platformUid);

	/**
	 * 用户是否绑定指定类型帐号
	 * @param userId
	 * @param type
	 * @return
	 */
	public boolean hasBind(int userId, PlatformEnum type);

	/**
	 * 插入
	 * @param platformMap
	 * @return
	 */
	public int insertPlatformMap(PlatformMap platformMap);

	/**
	 * 删除
	 * @param platformMap
	 * @return
	 */
	public int deletePlatformMap(PlatformMap platformMap);

	/**
	 * 绑定第三方帐号
	 * @param source
	 * @param userId
	 * @param type
	 * @param platformUid
	 * @param accessToken
	 * @return
	 */
	public int bind3rdPlatformMap(String source, int userId, PlatformEnum type,
			String platformUid, String accessToken);

	/**
	 * 解除指定类型的绑定信息
	 * @param userId
	 * @param type
	 * @return
	 */
	public int unbind3rdPlatformMap(int userId, PlatformEnum type);

	/**
	 * 更新用户指定类型的token
	 * @param userId
	 * @param accessToken
	 * @param type
	 * @return
	 */
	public int refreshToken(int userId, String accessToken,
			PlatformEnum type);

	/**
	 * 更新用户特定类型的绑定信息
	 * @param userId
	 * @param platformUid
	 * @param accessToken
	 * @param type
	 * @return
	 */
	int updateByUserIdAndType(int userId, String platformUid,
			String accessToken, PlatformEnum type);

}
