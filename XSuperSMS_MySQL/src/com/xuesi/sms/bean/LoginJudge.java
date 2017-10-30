package com.xuesi.sms.bean;

public class LoginJudge extends BaseVo {
	/**
	 * I/[http://192.168.1.232:8088/XSuperMES/api/User/login](314): <br>
	 * {"resultCode":0,<br>
	 * "factoryCode":"0000000000000000000000",<br>
	 * "passport":"lSD2FNZ_M8M6N2t0bkz4rWS4UL_lsOa8x3teTf1Ms2F",<br>
	 * "millCode":"0000000000000000000000",<br>
	 * "__account":"admin",<br>
	 * "__userKey":"r65Y3h25m77fE5PJD1fSUB",<br>
	 * "info":"Success"}<br>
	 * 
	 */

	/** 当前加密的密码 */
	private String __userKey;
	/** cookie权限GID */
	private String millCode;
	/** cookie通行证 */
	private String passport;
	/** 接口传入参数：分厂号 */
	private String factoryCode;
	/**
	 * 当前用户所属公司
	 */
	protected String COMP;

	public String getCOMP() {
		return COMP;
	}

	public void setCOMP(String COMP) {
		this.COMP = COMP;
	}

	public String get__userKey() {
		return __userKey;
	}

	public String getMillCode() {
		return millCode;
	}

	public String getPassport() {
		return passport;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

}
