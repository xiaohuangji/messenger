package com.dajie.message.mcp.model;

public class DispatchInterceptorConstant {

	/**
	 * http info
	 */
	public static final String DEFAULT_CHAR_SET = "utf-8";
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String DEFAULT_CONTENT_TYPE = JSON_CONTENT_TYPE;
	public static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

	/**
	 * app info
	 */
	public static final String TEST_APP_ID = "test";
	public static final String TEST_PASSWORD = "test";
	
	public static final String IOS_APP_ID = "ios";
	public static final String IOS_PASSWORD = "ios";
	
	public static final String ANDROID_APP_ID = "android";
	public static final String ANDROID_APP_PASSWORD = "android";
	
	public static final String QA_APP_ID = "qa";
	public static final String QA_APP_PASSWORD = "qa";
	
	//error code
	public static final int ACK_ERR_CLASS_NAME = 101;
	public static final String ACK_ERR_CLASS_NAME_MSG = "invoke class information error! check uri!";
	
	public static final int ACK_ERR_CLASS_METHOD = 102;
	public static final String ACK_ERR_CLASS_METHOD_MSG = "invoke method information error ! check uri!";
	
	public static final int ACK_ERR_CLASS_NAME_NO_MAP = 103;
	public static final String ACK_ERR_CLASS_NAME_NO_MAP_MSG = "service no class mapping url ! check uri!";

	
	public static final int ACK_ERR_CLASS_METHOD_NO_MAP = 104;
	public static final String ACK_ERR_CLASS_METHOD_NO_MAP_MSG = "service no method mapping url ! check uri!";
	
	//public static final int ACK_ERR_CLASS_METHOD_PARAM = 105;
	//public static final String ACK_ERR_CLASS_METHOD_PARAM_MSG = "service's method param error ! check http body!";
	
	public static final int ACK_ERR_SERVICE_INVOKE = 106;
	public static final String ACK_ERR_SERVICE_INVOKE_MSG = "service's invoke error ! check service!";
	
	public static final int ACK_ERR_CONTENT_TYPE = 107;
	public static final String ACK_ERR_CONTENT_TYPE_MSG = "http request content-type should not empty ,and only support application/json and application/x-www-form-urlencoded";

	public static final int ACK_ERR_REQUIRE_METHOD_PARAM = 108;
	public static final String ACK_ERR_REQUIRE_METHOD_PARAM_MSG = "request method parameter named {parameter} should not be null or ignored!";

	public static final int ACK_ERR_SERVICE_PARSE_JSON = 109;
	public static final String ACK_ERR_SERVICE_PARSE_JSON_MSG = "request body json parse error !";
	
	public static final int ACK_ERR_SERVICE_PARAM_TYPE = 110;
	public static final String ACK_ERR_SERVICE_PARAM_TYPE_MSG = "service method param type translate error ! param type must be primitive !";

	
	public static final int ACK_ERR_RET_NULL = 111;
	public static final String ACK_ERR_RET_NULL_MSG = "service return a null object ! ";
	
	

	
	public static final int ACK_ERR_APP_ID = 120;
	public static final String ACK_ERR_APP_ID_MSG = "request information for appId and password should not be null or empty";
	
	public static final int ACK_ERR_APP_ID_PASSWORD = 121;
	public static final String ACK_ERR_APP_ID_PASSWORD_MSG = "request appId password error !";
	
	public static final int ACK_ERR_APP_ID_NAME = 122;
	public static final String ACK_ERR_APP_ID_NAME_MSG = "request appId name error !";
	
	
	public static final int ACK_ERR_IP_NUMBER = 123;
	public static final String ACK_ERR_IP_NUMBER_MSG = "mobile ip or mobile number error !";	
	
	public static final int ACK_ERR_VERSION = 124;
	public static final String ACK_ERR_VERSION_MSG = "app version should not be null or empty !";
	
	public static final int ACK_ERR_TICKET = 125;
	public static final String ACK_ERR_TICKET_MSG = "user ticket should not null or empty !";
	
	public static final int ACK_ERR_TICKET_OUT_DATE = 126;
	public static final String ACK_ERR_TICKET_OUT_DATE_MSG = "user ticket out of date, please relogin !";

	public static final int ACK_ERR_SIG = 130;
	public static final String ACK_ERR_SIG_MSG = "sig check error !";

	public static final int ACK_ERR_FREQ = 131;
	public static final String ACK_ERR_FREQ_MSG = "error !";

	
	public static final int ACK_OK = 0;
	public static final String ACK_OK_MSG = "ok";
	

	/**
	 * request attribute 中的 params 属性的name
	 */
	public static final String REQUEST_PARAM_NAME = "com.dajie.message.mcp.params";
	
	
}
