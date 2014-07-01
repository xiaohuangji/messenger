package com.dajie.message.service.impl;

import java.util.UUID;

import com.dajie.message.mcp.model.UserPassport;
import com.dajie.message.mcp.passport.TicketUtils;
import com.dajie.message.mcp.service.IPassportService;
import com.dajie.message.service.cache.IRedisCache;
import com.dajie.message.service.cache.SigCache;
import com.dajie.message.util.log.LoggerInformation;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("passportService")
public class PassportServiceImpl implements IPassportService {

    @Autowired
    private IRedisCache redisCache;

    public static final String PASSPORT_PREFIX = "passport";
    
    public static final int EXPIRE = 30*2*24*60*60*24;/**两年 **/
    
    public static final Logger logger = Logger.getLogger(IPassportService.class);
    
    @Autowired
    public SigCache sigCache;

	public UserPassport getPassportByTicket(String ticket) {
		UserPassport userPassport = null;
		if (StringUtils.isEmpty(ticket)) {
			return userPassport;
		}
		int uid = TicketUtils.decryptTicket(ticket);
		if (uid <= 0) {
			return userPassport;
		}		
		userPassport = redisCache.get(PASSPORT_PREFIX, uid,UserPassport.class);
		return userPassport;
	}

	public String createTicket(UserPassport userPassport) {
		String ticket = null;
		if (userPassport == null || userPassport.getUserId() == 0) {
			return ticket;
		}
		ticket = TicketUtils.generateTicket(userPassport.getUserId());
		userPassport.setTicket(ticket);
		if (!StringUtils.isEmpty(ticket)) {
			redisCache.set(PASSPORT_PREFIX, userPassport.getUserId(), userPassport,EXPIRE);
		}
		return ticket;
	}

	@Override
	public void removeTicket(int userId) {
		LoggerInformation.LoggerErr(logger,"remove ticket"+userId,new Exception());
		redisCache.del(PASSPORT_PREFIX, userId);
	}

	@Override
	public Integer getSig(String sig) {
		return sigCache.getSig(sig);
	}

	@Override
	public void addSig(String sig, Integer value) {
		sigCache.addsig(sig, value);
	}

	@Override
	public UserPassport createPassport(UserPassport userPassport) {
		String ticket = null;
		
		if (userPassport == null || userPassport.getUserId() == 0) {
			return null;
		}
		ticket = TicketUtils.generateTicket(userPassport.getUserId());
		userPassport.setTicket(ticket);
		userPassport.setUserSecretKey(generateSecretKey());
		if (!StringUtils.isEmpty(ticket)) {
			redisCache.set(PASSPORT_PREFIX, userPassport.getUserId(), userPassport,EXPIRE);
		}
		return userPassport;
	}
	
	   /**
     * 生成密钥
     * 
     * @return
     */
    public static String generateSecretKey() {
        UUID uuid = UUID.randomUUID();
        long now = System.currentTimeMillis();
        return DigestUtils.md5Hex(uuid.toString() + now);
    }
}