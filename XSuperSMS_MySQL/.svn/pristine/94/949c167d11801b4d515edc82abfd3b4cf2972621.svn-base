package com.xuesi.sms;

import android.content.Context;
import android.os.Environment;

import com.xuesi.mos.app.MosApplication;
import com.xuesi.mos.crash.CrashHandler;
import com.xuesi.mos.device.MosDevice;
import com.xuesi.mos.device.MosPhone;
import com.xuesi.mos.device.Screen;
import com.xuesi.mos.net.volley.MosVolleyUtil;
import com.xuesi.mos.util.MosLog;
import com.xuesi.sms.util.SmsDir;

public class SmsApplication extends MosApplication {
	/** LOG */
	private final String LOG_TAG = SmsApplication.class.getSimpleName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "XUESI-SMS");

	/** 获取实例 */
	private static SmsApplication instance;

	/** 单例模式中获取唯一的SmsApplication实例 */
	public static SmsApplication getInstance() {
		if (null == instance) {
			instance = new SmsApplication();
		}
		return instance;
	}

	public static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();

		// 初始化设备类
		MosDevice.getInstance().init(this);
		// 初始化屏幕类
		Screen.getInstance().init(this);
		// 初始化手机类
		MosPhone.getInstance().init(this);
		// 创建volley队列
		MosVolleyUtil.getInstance(this);
		// 创建应用存储目录
		SmsDir.getInstance().init(this, "sdcard/YunSMS/temp",
				Environment.getExternalStorageDirectory() + "/YunSMS/download",
				"sdcard/YunSMS/crash", "sdcard/YunSMS/cache");
		if (SmsConfig.isDebug) {
			// 设置MosLog输出等级
			MosLog.setLOGLEVEL(1);
		} else {
			// 设置MosLog输出等级
			MosLog.setLOGLEVEL(8);
			// 本地保存崩溃日志
			CrashHandler.getInstance().init(this, "sdcard/YunSMS/crash");
		}
	}

}