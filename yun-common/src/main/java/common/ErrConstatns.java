package common;

import java.util.HashMap;

public class ErrConstatns {
	/*
	 * Common
	 */
	public static final Integer E_ACCOUNT_ALREADY_EXIST=100001;
	public static final Integer E_ACCOUNT_PASSWORD_ERROR=100002;


	/*
	 * API v3
	 */
	public static final Integer API3_PARAMETER_ERROR=300001;
	public static final Integer API3_SERVER_ERROR=300002;
	public static final Integer API3_USER_NOTEXISTS=300003;
	public static final Integer API3_RESOURCE_NOTEXISTS=300005;
	public static final Integer API3_PARAMETER_LENGTH_ERROR=300004;
	public static final Integer API3_USER_STATUS_ERROR=300006;
	public static final Integer API3_OVER_LIMIT=300007;

	public static final Integer API3_SAVE_FAIL=399999;
	public static final Integer API3_AUTH_FAIL=388888;
	public static final Integer API3_OTHER_ERROR=388887;
	public static final Integer API3_AUTH_EXPIRATION_TIME_ERROR=388886;
	public static final Integer API3_ACCOUNT_EXIST=399995;
	public static final Integer API3_CCLIVESTATUS_ERROR=113005;


	
	public static HashMap<Integer, String> errCodeMap = new HashMap<Integer, String>(); 
	static {
		errCodeMap.put(API3_PARAMETER_ERROR, "parameters lost or error");
		errCodeMap.put(API3_SERVER_ERROR, "server error");
		errCodeMap.put(API3_USER_NOTEXISTS, "user not exists");
		errCodeMap.put(E_ACCOUNT_ALREADY_EXIST, "account already exist");
		errCodeMap.put(API3_RESOURCE_NOTEXISTS, "resource not exists");
		errCodeMap.put(API3_PARAMETER_LENGTH_ERROR, "parameter length error");
		errCodeMap.put(API3_USER_STATUS_ERROR, "user status error");
		errCodeMap.put(API3_OVER_LIMIT, "over limit");
		errCodeMap.put(E_ACCOUNT_PASSWORD_ERROR, "account or password error");

		errCodeMap.put(API3_SAVE_FAIL, "fail");
		errCodeMap.put(API3_AUTH_FAIL, "auth fail");
		errCodeMap.put(API3_OTHER_ERROR, "other error");
		errCodeMap.put(API3_AUTH_EXPIRATION_TIME_ERROR, "auth expiration time error");
		errCodeMap.put(API3_ACCOUNT_EXIST, "account already exist");
		errCodeMap.put(API3_CCLIVESTATUS_ERROR, "update live status error");
	}
	
	public static String getCodeMessage(Integer code) {
		String mString = "";
		if (errCodeMap.containsKey(code)) {
			mString = errCodeMap.get(code);
		}
		return mString;
		
	}
}
