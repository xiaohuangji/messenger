package com.dajie.message.service.impl;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dajie.corp.api.enums.CorpInfoItem;
import com.dajie.corp.api.service.CorpService;
import com.dajie.corp.enums.common.AuthenticateStatusEnum;
import com.dajie.corp.enums.common.CorpAuthEmailSrcTypeEnum;
import com.dajie.corp.enums.common.CorpResult;
import com.dajie.corp.enums.common.SourceTypeEnum;
import com.dajie.corp.info.model.CorpAuthenticateEmail;
import com.dajie.corp.info.model.CorpBase;
import com.dajie.message.dao.UserEmailVerifyDAO;
import com.dajie.message.mcp.passport.AES;
import com.dajie.message.mcp.passport.AzDGCrypt;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEmailVerify;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IUserProfileService;

@Controller("ICheckService")
@RequestMapping("mail")
public class CheckServiceImpl {

	private static Logger logger = LoggerFactory
			.getLogger(CheckServiceImpl.class);

	@Autowired
	private IUserProfileService userProfileService;

	@Autowired
	private UserEmailVerifyDAO userEmailVerifyDAO;

	@Autowired
	private CorpService corpService;

	@Autowired
	private IPushService pushService;
	
	private static final long ONE_HOUR = 3600000; 

	@RequestMapping("check")
	public ModelAndView check(@RequestParam("userId") int userId,
			@RequestParam("token") String token, @RequestParam("sig") String sig) {
		
		/**
		 * Decrypt sig to get genUrlTime
		 */
		long genUrlTime = 0;
		try {
			genUrlTime = NumberUtils.toLong(AzDGCrypt.decrypt(sig, String.valueOf(ProfileServiceImpl.GOUDA_UID)));
		} catch(Exception e) {
			logger.error("Decrypt genUrlTime failed, sig:" + sig, e);
			userEmailVerifyDAO.delUserEmailVerifyByUserId(userId);
			return new ModelAndView("mail/urlexpired");
		}
		long currentTime = System.currentTimeMillis();
		boolean expired = (genUrlTime > currentTime || genUrlTime + ONE_HOUR < currentTime);
		if (expired) {
			userEmailVerifyDAO.delUserEmailVerifyByUserId(userId);
			return new ModelAndView("mail/urlexpired");
		}		
		
		/**
		 * Decode token to get user email verify
		 */
		UserEmailVerify userEmailVerify = null;
		try {
			String decryptText = null;
			AES.setKey(String.valueOf(ProfileServiceImpl.GOUDA_UID));
			AES.decrypt(token.trim());
			decryptText = AES.getDecryptedString();
			logger.info(decryptText);
			userEmailVerify = (UserEmailVerify) JSONObject.toBean(
					JSONObject.fromObject(decryptText), UserEmailVerify.class);
		} catch (Exception e) {
			logger.error("userEmailVerify对象解码失败", e);
			return new ModelAndView("mail/check");
		}
		if (userEmailVerify == null) {
			logger.info("userEmailVerify is null, userId:" + userId);
			return new ModelAndView("mail/check");
		}
		UserCareer userCareer = userProfileService.getUserCareer(userId);
		if (userCareer == null) {
			logger.info("userCareer is null, userId:" + userId);
			return new ModelAndView("mail/check");
		}	
		
		if(userEmailVerifyDAO.getVerifyDate(userEmailVerify) != null) {
			logger.info("UserCareer has clicked the link, userId:" + userId + ", email:" + userEmailVerify.getEmail());
			return new ModelAndView("mail/check");
		}
		userEmailVerifyDAO.updateVerifyDate(new Date(), userEmailVerify);
		String corpName = userCareer.getCorpName();
		/**
		 * Check corpName exists or not, if not, invoke CorpService to
		 * addCorpBase
		 */
		CorpBase corpBase = corpService.getCorpBaseByName(corpName);
		if (corpBase == null) {
			corpBase = new CorpBase();
			corpBase.setName(corpName);
			corpBase.setSrcType(SourceTypeEnum.GOUDA_APP.getType());
			CorpResult corpResult = corpService.addCorpBase(corpBase);
			logger.info("corpService.addCorpBase(" + corpName + "):"
					+ corpResult);
			return new ModelAndView("mail/check");
		}
		/**
		 * Check emailDomain exists or not, if not, invoke CorpService to
		 * addCorpAuthenticateEmail
		 */
		corpBase = corpService.getCorpBaseById(corpBase.getId(),
				CorpInfoItem.EmailDomain);
		String email = userEmailVerify.getEmail();	
		String emailDomain = email.substring(email.indexOf('@') + 1);
		List<String> emailDomainList = corpBase.getEmailDomains();
		if (emailDomainList != null && emailDomainList.contains(emailDomain)) {
			userProfileService.updateVerification(userId, 1);
			logger.info("[ClickEmailLink] userId:" + userId + ", email:" + email
					+ ", pushMessage: verify passed");
			try {
				pushService.sendTextMessage(userId, "恭喜你，你已经通过认证，可以开始发布机会啦");
			} catch (Exception e) {
				logger.error("push verify success failed", e);
			}
			userEmailVerifyDAO.delUserEmailVerify(userEmailVerify);
			return new ModelAndView("mail/check");
		} else {
			CorpAuthenticateEmail authEmail = new CorpAuthenticateEmail();
			authEmail.setEmail(email);
			authEmail.setUid(ProfileServiceImpl.GOUDA_UID);
			authEmail.setCorpId(corpBase.getId());
			authEmail.setStatus(AuthenticateStatusEnum.EMAIL_AUTH_WAIT_VERIFY
					.getStatus());
			authEmail.setSrcType(CorpAuthEmailSrcTypeEnum.DAJIE.getType());
			int result = corpService.addCorpAuthenticateEmail(authEmail);
			logger.info("corpService.addCorpAuthenticateEmail(" + email + "):"
					+ result);
			return new ModelAndView("mail/check");
		}

	}
}
