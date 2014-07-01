package com.dajie.message.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.dao.PlatformMapDAO;
import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.PlatformMap;
import com.dajie.message.service.IPlatformMapService;

@Component("platformMapService")
public class PlatformMapServiceImpl implements IPlatformMapService {

	@Autowired
	private PlatformMapDAO platformMapDAO;

	@Override
	public List<PlatformMap> getPlatformMapsByUserId(int userId) {
		return platformMapDAO.getPlatformMapsByUserId(userId);
	}

	@Override
	public PlatformMap getPlatformMapByUserIdAndType(int userId,
			PlatformEnum type) {
		return platformMapDAO.getPlatformMapByUserIdAndType(userId,
				type.getCode());
	}

	@Override
	public PlatformMap getPlatformMap(PlatformEnum type, String platformUid) {
		return platformMapDAO.getPlatformMap(type.getCode(), platformUid);
	}

	@Override
	public boolean isAccountUsed(PlatformEnum type, String platformUid) {
		PlatformMap platformMap = getPlatformMap(type, platformUid);
		return null != platformMap;
	}

	@Override
	public boolean hasBind(int userId, PlatformEnum type) {
		PlatformMap platformMap = getPlatformMapByUserIdAndType(userId, type);
		return null != platformMap;
	}

	@Override
	public int bind3rdPlatformMap(String source, int userId, PlatformEnum type,
			String platformUid, String accessToken) {
		PlatformMap platformMap = new PlatformMap();
		platformMap.setUserId(userId);
		platformMap.setPlatformType(type.getCode());
		platformMap.setAccessToken(accessToken);
		platformMap.setPlatformUid(platformUid);
		platformMap.setSource(source);
		platformMap.setCreateTime(new Date());
		return platformMapDAO.Insert(platformMap);
	}

	@Override
	public int unbind3rdPlatformMap(int userId, PlatformEnum type) {
		return platformMapDAO.delete(userId, type.getCode());
	}

	@Override
	public int insertPlatformMap(PlatformMap platformMap) {
		return platformMapDAO.Insert(platformMap);
	}

	@Override
	public int deletePlatformMap(PlatformMap platformMap) {
		return platformMapDAO.delete(platformMap.getUserId(),
				platformMap.getPlatformType());
	}

	@Override
	public int refreshToken(int userId, String accessToken, PlatformEnum type) {
		return platformMapDAO.updateToken(userId, accessToken, type.getCode());
	}

	@Override
	public int updateByUserIdAndType(int userId, String platformUid,
			String accessToken, PlatformEnum type) {
		return platformMapDAO.updateByUserIdAndType(userId, platformUid,
				accessToken, type.getCode());
	}

}
