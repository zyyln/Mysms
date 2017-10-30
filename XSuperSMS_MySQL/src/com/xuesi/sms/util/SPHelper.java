package com.xuesi.sms.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xuesi.sms.SmsApplication;

/**
 * SharedPreferences管理类<br>
 * 
 * @author XS-PC014
 * 
 */
public class SPHelper {

	/** 获取实例 */
	private static SPHelper instance = null;

	private SPHelper() {
	}

	/** 单例模式中获取唯一的SmsUtil实例 */
	public static SPHelper getInstance() {
		if (null == instance) {
			instance = new SPHelper();
		}
		return instance;
	}

	private final Context mContext = SmsApplication.appContext;

	/** SharedPreferences文件名 */
	private final String SPName = "user";
	/** 是否记住密码 */
	public static final String KEY_IS_REMPSD = "is_rem_psd";
	/** 用户名 */
	public static final String KEY_ACCOUNT = "login_act";
	/** 密码 */
	public static final String KEY_PASSWORD = "login_psd";
	/** ip */
	public static final String KEY_IP = "cur_ip_address";
	/** 端口 */
	public static final String KEY_PORT = "cur_ip_port";

	/**
	 * save string to sp<br>
	 * 可写<br>
	 */
	public boolean saveValueToSp(String key, String value) {
		// 取得sharedPreference对象
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);

		// 保存string值到SP文件
		return preferences.edit().putString(key, value).commit();
	}

	/**
	 * save int to sp
	 */
	public boolean saveValueToSp(String key, int value) {
		// 取得sharedPreference对象
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);

		// 保存int值到SP文件
		return preferences.edit().putInt(key, value).commit();
	}

	/**
	 * save boolean to sp
	 */
	public boolean saveValueToSp(String key, Boolean value) {
		// 取得sharedPreference对象
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);

		// 保存boolean值到SP文件
		return preferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * -1为默认值
	 * 
	 * @param key
	 * @return
	 */
	public int getIntFromSp(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);
		return preferences.getInt(key, -1);
	}

	public String getStringFromSp(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}

	public boolean getBooleanFromSp(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, false);
	}

	public void removeValueFromSp(String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);
		preferences.edit().remove(key);
	}

}
