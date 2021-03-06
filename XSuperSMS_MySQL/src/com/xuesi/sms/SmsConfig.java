package com.xuesi.sms;

public class SmsConfig {

	/** SmsConfig实例对象 */
	private static SmsConfig instance;

	/** 单例模式中获取唯一的SmsConfig实例 */
	public static SmsConfig getInstance() {
		if (null == instance) {
			instance = new SmsConfig();
		}
		return instance;
	}

	// ********* 程序配置信息 *********/
	/** 测试状态 */
	public static boolean isDebug = false;
	/** 是否体验版 true:体验版本 */
	public static boolean isTest = false;
	/** 蓝牙 */
	public static boolean connFlag = false;

	/*** 测试分功能 **/

	/** ---- 分页 pageSize设置------- */
	public static final int pageSize = 500;
	public static final int pageSize_s = 0;
	/** ----行车是否勾选---- */
	public static boolean isCraneCheck = true;
	/** -----小数点位数----- */
	public static int dotNum = 1;

	// ******* 程序运行的设备信息 ******/
	/** KT45pda的标识位(2015-12-07) */
	public static final String TAG_KT45 = "kt45";
	/** TT35pda的标识位 (2015-11-26此设备已不用待删除) */
	public static final String TAG_TT35 = "omap3evm";
	/** PAD(UROVO)pad的标识位(2015-11-26) */
	public static final String TAG_UROVO = "I8000";
	/** CDF_550标识位（2017-4-17） */
	public static final String TAG_550 = "Android Handheld Terminal";

}
