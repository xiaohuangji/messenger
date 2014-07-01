package com.dajie.message.enums.returncode;


public enum AccountResult implements ReturnCodeEnum {
	SMS_SEND_MOBILE_INVALID(1001),
	SMS_SEND_RETRY_OUT_LIMIT(1002),
	SMS_SEND_UNKNOWN_TYPE(1003),

	// register
	REG_VERIFY_CODE_ERROR(1101),
	REG_MOBILE_INVALID(1102),
	REG_PASSWORD_FORMAT_ERROR(1103),
	REG_MOBILE_IS_USED(1104),
	REG_NAME_FORMAT_ERROR(1105),
	REG_AVATAR_FORMAT_ERROR(1106),

	// login
	LOGIN_ACCOUNT_NOT_EXIST(1201),
	LOGIN_PASSWORD_ERROR(1202),
	LOGIN_ACCOUNT_FREEZW(1203),

	// modify password
	MOD_PASSWORD_VERIFY_CODE_ERR(1301),
	MOD_PASSWORD_PASSWORD_FORMAT_ERR(1302),
	MOD_PASSWORD_USER_NOT_EXIST(1303),

	// login with dj
	LOGIN_DJ_ACCOUNT_NOT_REG(1401),
	LOGIN_DJ_PASSWORD_ERR(1402),
	LOGIN_DJ_ACCOUNT_NOT_EXIST(1403),
	LOGIN_DJ_LOGIN_FAIL(1404),

	// login with OAuth2.0
	LOGIN_3rd_ACCOUNT_NOT_REG(1501),
	LOGIN_TOKEN_INVALID(1502),
	LOGIN_UNKNOWN_PLATFORM(1503),

	// bind account
	BIND_3rd_ACCOUNT_USED(1601),
	BIND_HAS_BIND(1602),
	BIND_TOKEN_INVALID(1603),
	BIND_DJ_ACCOUNT_NOT_EXIST(1604),
	BIND_DJ_PASSWORD_ERR(1605),
	BIND_DJ_LOGIN_FAIL(1606),

	// unbind account
	UNBIND_LAST_ONE(1701), UNBIND_NOT_EXIST(1702);

	private int code;

	private String msg;
	
	private AccountResult(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}
