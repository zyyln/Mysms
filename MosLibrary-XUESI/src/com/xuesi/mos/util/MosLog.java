/**
 * MosLog.java
 * com.xuesi.mos.util
 *
 * Function�� TODO 
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2015-10-11 	zqLi
 *
 * Copyright (c) 2015, TNT All Rights Reserved.
 */

package com.xuesi.mos.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

/**
 * ClassName:MosLog
 * 
 * Function: TODO ADD FUNCTION
 * 
 * Reason: TODO ADD REASON
 * 
 * @author lzq
 * @version
 * @since Ver 1.1
 * @Date 2015-10-11
 */
public class MosLog {
	private static MosLog instance;
	private static boolean hasLogFile = false;
	private String logTag;
	private String logUser;

	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;
	private static int LOGLEVEL = 1;

	static {
		if (Environment.getExternalStorageState().equals("mounted"))
			hasLogFile = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/logfile").exists();
	}

	public static MosLog getInstance(String logTag, String logUser) {
		if (instance == null)
			instance = new MosLog();

		instance.init(logTag, logUser);
		return instance;
	}

	public void init(String logTag, String logUser) {
		if (logTag == null) {
			instance.setLogTag();
		} else {
			instance.setLogTag(logTag);
		}

		if (logUser == null)
			instance.markLogUser();
		else
			instance.markLogUser(logUser);
	}

	private void setLogTag(String logTag) {
		this.logTag = "[" + logTag + "]";
	}

	private void setLogTag() {
		this.logTag = "[AppName]";
	}

	private void markLogUser(String logUser) {
		this.logUser = "@" + logUser + "@" + " ";
	}

	private void markLogUser() {
		this.logUser = "@default@ ";
	}

	public static int getLOGLEVEL() {
		return LOGLEVEL;
	}

	/**
	 * >=8 所有等级log都不打印
	 * <p>
	 * <=1所有等级log都打印
	 * <p>
	 * 默认为 0
	 * 
	 * @param lOGLEVEL
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public static void setLOGLEVEL(int lOGLEVEL) {
		LOGLEVEL = lOGLEVEL;
		Log.i("MosLog", "current log level to: " + LOGLEVEL);
	}

	private String getFunctionInfo() {
		StackTraceElement[] arrayOfStackTraceElement1;
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();

		if (sts == null)
			return null;

		int j = (arrayOfStackTraceElement1 = sts).length;
		for (int i = 0; i < j; ++i) {
			StackTraceElement st = arrayOfStackTraceElement1[i];
			if (st.isNativeMethod())
				continue;

			if (st.getClassName().equals(Thread.class.getName()))
				continue;

			if (st.getClassName().equals(super.getClass().getName()))
				continue;

			return this.logUser + getCurTime() + " [ "
					+ Thread.currentThread().getName() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + " "
					+ st.getMethodName() + " ]";
		}
		return null;
	}

	@SuppressLint("SimpleDateFormat")
	private String getCurTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String date = sdf.format(new Date());
		return date;
	}

	public void v(Object str) {
		String name = getFunctionInfo();
		if (VERBOSE >= LOGLEVEL) {
			if (name != null)
				Log.v(this.logTag, name + "\n -- " + str);
			else
				Log.v(this.logTag, str.toString());
		}
	}

	public void d(Object str) {
		String name = getFunctionInfo();
		if (DEBUG >= LOGLEVEL) {
			if (name != null)
				Log.d(this.logTag, name + "\n -- " + str);
			else
				Log.d(this.logTag, str.toString());
		}
	}

	public void i(Object str) {
		String name = getFunctionInfo();
		if (INFO >= LOGLEVEL) {
			if (name != null)
				Log.i(this.logTag, name + "\n -- " + str);
			else
				Log.i(this.logTag, str.toString());
		}
	}

	public void w(Object str) {
		String name = getFunctionInfo();
		if (WARN >= LOGLEVEL) {
			if (name != null)
				Log.w(this.logTag, name + "\n -- " + str);
			else
				Log.w(this.logTag, str.toString());
		}
	}

	public void e(Object str) {
		String name = getFunctionInfo();
		if (ERROR >= LOGLEVEL) {
			if (name != null)
				Log.e(this.logTag, name + "\n -- " + str);
			else
				Log.e(this.logTag, str.toString());
		}
	}

	public void e(Exception ex) {
		Log.e(this.logTag, "error", ex);
	}

	public void e(String log, Throwable tr) {
		String line = getFunctionInfo();
		Log.e(this.logTag, "{Thread:" + Thread.currentThread().getName() + "}"
				+ "[" + this.logUser + line + ":] " + log + "\n", tr);
	}

	public void i(Object str, Boolean mode) {
		if (mode.booleanValue()) {
			String name = getFunctionInfo();
			if (name != null)
				Log.i(this.logTag, name + "\n -- " + str);
			else
				Log.i(this.logTag, str.toString());
		}
	}

	public void d(Object str, Boolean mode) {
		if (mode.booleanValue()) {
			String name = getFunctionInfo();
			if (name != null)
				Log.d(this.logTag, name + "\n -- " + str);
			else
				Log.d(this.logTag, str.toString());
		}
	}

	public void v(Object str, Boolean mode) {
		if (mode.booleanValue()) {
			String name = getFunctionInfo();
			if (name != null)
				Log.v(this.logTag, name + "\n -- " + str);
			else
				Log.v(this.logTag, str.toString());
		}
	}

	public void w(Object str, Boolean mode) {
		if (mode.booleanValue()) {
			String name = getFunctionInfo();
			if (name != null)
				Log.w(this.logTag, name + "\n -- " + str);
			else
				Log.w(this.logTag, str.toString());
		}
	}

	public void e(Object str, Boolean mode) {
		if (mode.booleanValue()) {
			String name = getFunctionInfo();
			if (name != null)
				Log.e(this.logTag, name + "\n -- " + str);
			else
				Log.e(this.logTag, str.toString());
		}
	}

	public void e(Exception ex, Boolean mode) {
		if (mode.booleanValue()) {
			Log.e(this.logTag, "error", ex);
		}
	}

	public void e(String log, Throwable tr, Boolean mode) {
		if (mode.booleanValue()) {
			String line = getFunctionInfo();
			Log.e(this.logTag, "{Thread:" + Thread.currentThread().getName()
					+ "}" + "[" + this.logUser + line + ":] " + log + "\n", tr);
		}
	}

}