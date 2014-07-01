package com.dajie.message.constants.returncode;

public class AccountResultCode {

	// AccountService 通用错误返回码
	// 3rd platform connect error
	public static final int INVALID_TOKEN = 1000;
	public static final int UNKNOWN_PLATFORM = 1001;
	public static final int DJ_ACCOUNT_NOT_EXIST = 1002;
	public static final int DJ_ACCOUNT_PASSWORD_ERR = 1003;
	public static final int DJ_ACCOUNT_CONNECT_FAIL = 1004;
	public static final int DJ_ACCOUNT_DISABLE = 1005;

	// param error
	public static final int PARAM_MOBILE_INVALID = 1011;
	public static final int PARAM_PASSWORD_INVALID = 1012;
	public static final int PARAM_VERIFI_CODE_ERROR = 1013;
	public static final int PARAM_NAME_INVALID = 1014;
	public static final int PARAM_NAME_SENSITIVE = 1015;

	/* ========================================================= */
	// AccountService接口特定返回码
	// smscode
	public static final int SMS_SEND_RETRY_OUT_LIMIT = 1100;
	public static final int SMS_SEND_UNKNOWN_TYPE = 1101;
	public static final int SMS_SEND_NOT_REG_MOBILE = 1102;
	public static final int SMS_SEND_MOBILE_IS_USED = 1103;

	// register
	public static final int REG_MOBILE_IS_USED = 1110;

	// login
	public static final int LOGIN_ACCOUNT_NOT_EXIST = 1120;
	public static final int LOGIN_PASSWORD_ERROR = 1121;
	public static final int LOGIN_ACCOUNT_FREEZE = 1122;

	// modify password
	public static final int MOD_PASSWORD_USER_NOT_EXIST = 1130;

	// login with dj
	public static final int LOGIN_DJ_ACCOUNT_NOT_REG = 1140;
	public static final int REG_DJ_IS_USED = 1141;

	// login with OAuth2.0
	public static final int LOGIN_3rd_ACCOUNT_NOT_REG = 1150;
	public static final int REG_3rd_ACCOUNT_IS_USED = 1151;

	// bind account
	public static final int BIND_3rd_ACCOUNT_USED = 1160;
	public static final int BIND_HAS_BIND = 1161;

	// unbind account
	public static final int UNBIND_LAST_ONE = 1170;
	public static final int UNBIND_NOT_EXIST = 1171;

	// change mobile
	public static final int CHANGE_MOBILE_IS_USED = 1180;
	
	// RelationService 错误码
	public static final int BLOCKED = 2000;	
	public static final int HAS_DELETED = 2001;
	
	// ProfileService 错误码
	public static final int CORP_NEED_AUDIT = 3001;  // 用户输入的公司名称不在大街网公司数据库里，需要人工审核后添加

}
