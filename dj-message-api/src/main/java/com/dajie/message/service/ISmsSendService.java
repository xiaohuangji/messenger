package com.dajie.message.service;

public interface ISmsSendService {
	/**
	 * 短信下发
	 * 
	 * @param mobile
	 * @param msg
	 * @return
	 */
	@Deprecated
	int smsSend(String mobile, String msg);

	/**
	 * 下发验证码
	 * 
	 * @param mobile
	 * @param code
	 * @return
	 */
	int smsSendVerifyCode(String mobile, String code);

	/**
	 * 下发推荐信息 "Hi,name~你发布的工作机会有personCount个人回应了"
	 * 
	 * @param mobile
	 * @param name
	 * @param personCount
	 * @return
	 */
	boolean smsSendRecommend(String mobile, String name, String personCount);
}
