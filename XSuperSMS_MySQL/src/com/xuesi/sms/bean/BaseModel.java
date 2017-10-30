package com.xuesi.sms.bean;

/**
 * 抽取出来最基础的模型。
 * 
 * @author COODER
 * 
 */
public class BaseModel {
	/**
	 * 结果代码 返回0表示无异常
	 */
	protected int resultCode;
	/**
	 * 信息
	 */
	protected String info;

	/**
	 * 当前用户名
	 */
	protected String __account;

	public String get__account() {
		return __account;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getInfo() {
		return info;
	}

}
