package com.dajie.push.netty.filter;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * IOSpush
 * 如果是正式环境，同时推送两种证书的push.pro和enterprise
 * 如果是非正式环境，只推送sanbox
 *
 * enpPushManager是新增的，管理enterprise的push
 *
 */
public class ApplePushClient  {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplePushClient.class);

	private static ApplePushClient pushClient = null;

	public final String PASSWORD = "123456";

	private static final String pro_apnsKey = "/Certificates_gouda_dis_push.p12";

	private static final String sandbox_apnsKey = "/Certificates_gouda_sandbox_push.p12";

    private static final String enterprise_apnsKey = "/Certificates_gouda_house_push.p12";

    private static boolean isProEnv=false;

	private static PushNotificationManager pushManager = new PushNotificationManager();

    private static PushNotificationManager enpPushManager;

	public static ApplePushClient getInstance() {
		if (pushClient == null) {
            if(System.getProperty("env").equals("Pro")){
                //如果是正式环境，初始化企业版pushManager
                isProEnv=true;
                enpPushManager=new PushNotificationManager();
            }
			pushClient = new ApplePushClient();
		}
		return pushClient;
	}

	private ApplePushClient() {
		try {
            if(isProEnv){//正式环境
                String certificateFile=this.getClass().getResource(pro_apnsKey).getFile();
                pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificateFile, PASSWORD, true));

                String enterpriseCertificateFile=this.getClass().getResource(enterprise_apnsKey).getFile();
                enpPushManager.initializeConnection(new AppleNotificationServerBasicImpl(enterpriseCertificateFile,PASSWORD,true));
            }else{
                String certificateFile=this.getClass().getResource(sandbox_apnsKey).getFile();
                pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificateFile, PASSWORD, false));
            }


		} catch (CommunicationException e) {
            LOGGER.warn("initialzePushManager fail",e);
		} catch (KeystoreException e) {
            LOGGER.warn("initialzePushManager fail",e);
		}

	}

	public boolean push(String token,Map map) {

		PushNotificationPayload payload = new PushNotificationPayload();
		try {
			// basic info这里组装显示的push
			payload.addAlert((String)map.get("content")); // 消息内容
			payload.addSound("default"); // 声音提示文件
			payload.addBadge(1); // iphone应用图标上小红圈上的数值

			// custom info
			payload.addCustomDictionary("to",map.get("to"));
			payload.addCustomDictionary("from", map.get("from"));
			payload.addCustomDictionary("time",String.valueOf(map.get("time")));
			payload.addCustomDictionary("msgType", String.valueOf(map.get("msgType")));
            payload.addCustomDictionary("contentType", String.valueOf(map.get("contentType")));

			int Maxlength = payload.getMaximumPayloadSize();
			int length = payload.getPayloadSize();

			// push长度限制
			if (length <= Maxlength) {

				// 设备
				Device device = new BasicDevice();
				device.setToken(token);

				PushedNotification NotificationResult = pushManager.sendNotification(device, payload, false);
				if (NotificationResult.isSuccessful()) {
					LOGGER.debug("ios push message success.");
				} else {
                    LOGGER.warn("ios push message failed : "+NotificationResult.toString());
				}

                /**
                 * 如果正式环境，多发一条企业版证书push
                 */
                if(isProEnv){
                    PushedNotification result =enpPushManager.sendNotification(device,payload,false);
                    if(!result.isSuccessful()){
                        LOGGER.warn("enterprise ios push failed : "+NotificationResult.toString());
                    }
                }
                //-------------------------------------

			} else {
                LOGGER.error("Can't send push message success for total payload size beyond maxSize:256");
			}
		} catch (CommunicationException e) {
            LOGGER.error("ios push communicationException:", e);
		} catch (KeystoreException e) {
            LOGGER.error("ios push keystoreException:", e);
		} catch (Exception e) {
            LOGGER.error("ios push exception:", e);
		}

		return true;
	}

}
