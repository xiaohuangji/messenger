package com.dajie.push.netty.filter;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * android push ,采用个推
 * 
 * @author li.hui
 * 
 */
public class AndroidPushClient {

	private static final Logger LOGGER = Logger
			.getLogger(AndroidPushClient.class);

	private static final String APP_ID = "WS9s2FYJc16I7moRNKTjs3";

	private static final String APP_KEY = "OC2TXsoIaEAG4RyPfgNuq2";

	private static final String APP_SECRET = "JS2HLr5Yhq6F2pSgvdl5R2";

	private static final String MASTER_SECRET = "NgRVHZhJjlAj3gWnOjx6k8";

	private static final String API = "http://sdk.open.api.igexin.com/apiex.htm";

	private IIGtPush push = new IGtPush(API, APP_KEY, MASTER_SECRET);

	private static AndroidPushClient androidPushClient = null;

	private AndroidPushClient() {
	}

	public static AndroidPushClient getInstance() {
		if (null == androidPushClient) {
			androidPushClient = new AndroidPushClient();
		}
		return androidPushClient;
	}

	public void push(String clientId, Map<String, Object> jsonMap) {
		try {
			// init message template,采用透传的方式，客户自定义行为
			TransmissionTemplate template = new TransmissionTemplate();
			template.setAppId(APP_ID);
			template.setAppkey(APP_KEY);
			String json = JSON.toJSONString(jsonMap);
			template.setTransmissionContent(json);
			template.setTransmissionType(2); // 1:收到消息立即启动应用，2：广播等待客户端自启动
			// init a singel message 推送单条消息
			SingleMessage message = new SingleMessage();
			message.setData(template);
			message.setOffline(true); // 用户不在线时是否离线存储
			message.setOfflineExpireTime(24 * 3600 * 1000); // 离线存储有效期

			// target init
			Target target = new Target();
			target.setAppId(APP_ID);
			target.setClientId(clientId);
			IPushResult ret = push.pushMessageToSingle(message, target);
			if ("ok".equals(ret.getResponse().get("result"))) {
				LOGGER.info("android push send suc, clientId=" + clientId
						+ ",content: " + json);
			} else {
				LOGGER.info("android push send fail,clientId=" + clientId
						+ ",content: " + json);
			}
		} catch (Exception e) {
			LOGGER.warn("sendAndroidPush exception", e);
		}
	}
	
}
