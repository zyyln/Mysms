package com.xuesi.sms.util;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.xuesi.mos.util.MosLog;

public class SmsDir {
	private final String LOG_TAG = SmsDir.class.getName();
	private final MosLog mLog = MosLog.getInstance(LOG_TAG, "MosHelper");

	private static SmsDir instance;

	/**
	 * TEMP_DIR default:"sdcard/com.xuesi.sms/temp"
	 * 
	 * @since 1.0.0
	 */
	private String temp_dir;
	/**
	 * DOWNLOAD_DIR default:"sdcard/com.xuesi.sms/download"
	 * 
	 * @since 1.0.0
	 */
	private String download_dir;
	/**
	 * CRASH_DIR default:"sdcard/com.xuesi.sms/crash"
	 * 
	 * @since 1.0.0
	 */
	private String crash_dir;
	/**
	 * CACHE_DIR default:"sdcard/com.xuesi.sms/cache"
	 * 
	 * @since 1.0.0
	 */
	private String cache_dir;

	public String getTemp_dir() {
		return temp_dir;
	}

	public String getDownload_dir() {
		return download_dir;
	}

	public String getCrash_dir() {
		return crash_dir;
	}

	public String getCache_dir() {
		return cache_dir;
	}

	public static SmsDir getInstance() {
		if (instance == null) {
			instance = new SmsDir();
		}
		return instance;
	}

	public void init(Context context, String temp_dir, String download_dir,
			String crash_dir, String cache_dir) {
		
		this.temp_dir = temp_dir;
		this.download_dir = download_dir;
		this.crash_dir = crash_dir;
		this.cache_dir = cache_dir;
		if (temp_dir == null) {
			throw new NullPointerException("Constants.temp_dir can not be null");
		} else if (download_dir == null) {
			throw new NullPointerException(
					"Constants.download_dir can not be null");
		} else if (crash_dir == null) {
			throw new NullPointerException(
					"Constants.crash_dir can not be null");
		} else if (cache_dir == null) {
			throw new NullPointerException(
					"Constants.cache_dir can not be null");
		} else {
			File f = new File(temp_dir);
			if ((!f.exists()) || (f.isFile())) {
				f.mkdirs();
			}

			f = new File(download_dir);
			if ((!f.exists()) || (f.isFile())) {
				f.mkdirs();
			}

			f = new File(crash_dir);
			if ((!f.exists()) || (f.isFile())) {
				f.mkdirs();
			}
		}
	}

}
